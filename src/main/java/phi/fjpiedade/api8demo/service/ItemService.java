package phi.fjpiedade.api8demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import phi.fjpiedade.api8demo.domain.item.ItemModel;
import phi.fjpiedade.api8demo.repository.item.ItemRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService() {
    }

    @Inject
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemModel> getAllItems() {
        return itemRepository.findAll();
    }

    public ItemModel createItem(ItemModel item) {
        item.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        itemRepository.save(item);
        return item;
    }

    public ItemModel getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public ItemModel updateItem(Long id, ItemModel updatedItem) {
        ItemModel item = itemRepository.findById(id);
        if (item != null) {
            item.setName(updatedItem.getName());
            item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            itemRepository.update(item);
        }
        return item;
    }

    public boolean deleteItem(Long id) {
        ItemModel item = itemRepository.findById(id);
        if (item != null) {
            itemRepository.delete(item);
            return true;
        }
        return false;
    }
}
