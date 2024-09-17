package phi.fjpiedade.apiorder.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.domain.stock.StockModel;
import phi.fjpiedade.apiorder.repository.stock.StockRepository;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StockService {
    @Inject
    private StockRepository stockRepository;
    @Inject
    private ItemRepository itemRepository;

    Logger logger = LoggerFactory.getLogger(StockService.class);

    public StockService() {
    }

    public List<StockModel> getAllStocks() {
        logger.info("Fetching stock(s)");
        return stockRepository.findAll();
    }

    public StockModel getStockById(Long id) {
        logger.info("Fetching user by ID {} ", id);
        return stockRepository.findById(id);
    }

    @Transactional
    public StockModel createStock(StockModel stock) {
        ItemModel item = itemRepository.findById(stock.getItem().getId());

        if (item == null) {
            logger.error("Item does not exist.");
            //throw new IllegalArgumentException("Stock does not exist.");
            return null;
        }

        StockModel existingStock = stockRepository.findByItemId(stock.getItem().getId());
        if (existingStock != null) {
            logger.error("Item already exists in the stock.");
            //throw new IllegalArgumentException("Item already exists in the stock.");
            return null;
        }

        stock.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        StockModel stockCreated = stockRepository.save(stock);

        logger.info("stock created.");
        return stockCreated;
    }

    @Transactional
    public StockModel updateStock(Long id, StockModel updatedStock) {
        StockModel stock = stockRepository.findById(id);
        if (stock == null) {
            logger.error("Stock not found.");
            //throw new IllegalArgumentException("Stock not found.");
            return null;
        }

        stock.setQuantity(updatedStock.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        StockModel stockUpdated = stockRepository.update(stock);
        logger.info("Stock Updated.");
        return stockUpdated;
    }

    @Transactional
    public boolean deleteStock(Long id) {
        StockModel stock = stockRepository.findById(id);
        if (stock == null) {
            logger.error("Stock not found on delete process.");
            //throw new IllegalArgumentException("Stock not found.");
            return false;
        }

        stockRepository.delete(stock);
        return true;
    }

    public int checkStockQuantity(ItemModel item) {
        logger.info("Stock checking quantity: {}", item.getId());
        Optional<StockModel> stockFounded = Optional.ofNullable(stockRepository.findByItemId(item.getId()));
        if (!stockFounded.isPresent()) return -1;

        if (stockFounded.get().getQuantity() <= 0) return 0;

        return stockFounded.get().getQuantity();
    }

    public void reduceStockQuantity(ItemModel item, int quantity) {
        logger.info("Stock reducing quantity: {}", item.getId());
        Optional<StockModel> stockEntity = Optional.ofNullable(stockRepository.findByItemId(item.getId()));
        int currentQuantity = stockEntity.get().getQuantity();
        stockEntity.ifPresent(entity -> entity.setQuantity(currentQuantity - quantity));
    }

}
