package phi.fjpiedade.apiorder.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class ItemService {
    @Inject
    private ItemRepository itemRepository;

    Logger logger = LoggerFactory.getLogger(ItemService.class);

    public ItemService() {
    }

    public List<ItemModel> getAllItems() {
        logger.info("Fetching all items.");
        return itemRepository.findAll();
    }

    public ItemModel createItem(ItemModel item) {
        item.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        itemRepository.save(item);
        logger.info("Creating new item.");
        return item;
    }

    public ItemModel getItemById(Long id) {
        logger.info("Fetching item by id.");
        return itemRepository.findById(id);
    }

    public ItemModel updateItem(Long id, ItemModel updatedItem) {
        ItemModel item = itemRepository.findById(id);
        if (item == null) {
            logger.error("Error occurred while updating item, Item not found to Update.");
            //throw new IllegalArgumentException("Error occurred while updating item.");
            return null;
        }

        item.setName(updatedItem.getName());
        item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        itemRepository.update(item);
        logger.info("Updated item.");
        return item;
    }

    public boolean deleteItem(Long id) {
        ItemModel item = itemRepository.findById(id);
        if (item == null) {
            logger.error("Item not Found to delete!");
            //throw new IllegalArgumentException("Error occurred while deleting item.");
            return false;
        }

        logger.info("Item deleted.");
        itemRepository.delete(item);
        return true;
    }
}
