package phi.fjpiedade.apiorder.repository.item;

import phi.fjpiedade.apiorder.domain.item.ItemModel;

import java.util.List;

public interface ItemRepository {
    List<ItemModel> findAll();
    ItemModel findById(Long id);
    ItemModel save(ItemModel item);
    ItemModel update(ItemModel item);
    void delete(ItemModel item);
}
