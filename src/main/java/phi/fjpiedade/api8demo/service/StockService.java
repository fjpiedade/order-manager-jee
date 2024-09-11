package phi.fjpiedade.api8demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import phi.fjpiedade.api8demo.domain.item.ItemModel;
import phi.fjpiedade.api8demo.domain.stock.StockModel;
import phi.fjpiedade.api8demo.repository.stock.StockRepository;
import phi.fjpiedade.api8demo.repository.item.ItemRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class StockService {
    private StockRepository stockRepository;
    private ItemRepository itemRepository;

    public StockService() {
    }

    @Inject
    public StockService(StockRepository stockRepository, ItemRepository itemRepository) {
        this.stockRepository = stockRepository;
        this.itemRepository = itemRepository;
    }

    public List<StockModel> getAllStocks() {
        return stockRepository.findAll();
    }

    public StockModel getStockById(Long id) {
        return stockRepository.findById(id);
    }

    @Transactional
    public StockModel createStock(StockModel stock) {
        ItemModel item = itemRepository.findById(stock.getItem().getId());

        if (item == null) {
            throw new IllegalArgumentException("Item does not exist.");
        }

        StockModel existingStock = stockRepository.findByItemId(stock.getItem().getId());
        if (existingStock != null) {
            throw new IllegalArgumentException("Item already exists in the stock.");
        }

        stock.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        stockRepository.save(stock);

        return stock;
    }

    @Transactional
    public StockModel updateStock(Long id, StockModel updatedStock) {
        StockModel stock = stockRepository.findById(id);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found.");
        }

        stock.setQuantity(updatedStock.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        stockRepository.update(stock);

        return stock;
    }

    @Transactional
    public void deleteStock(Long id) {
        StockModel stock = stockRepository.findById(id);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found.");
        }

        stockRepository.delete(stock);
    }
}
