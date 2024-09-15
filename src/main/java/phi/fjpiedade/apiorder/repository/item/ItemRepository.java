package phi.fjpiedade.apiorder.repository.item;

import phi.fjpiedade.apiorder.domain.item.ItemModel;

import java.util.List;

public interface ItemRepository {
    List<ItemModel> findAll();
    ItemModel findById(Long id);
    void save(ItemModel item);
    void update(ItemModel item);
    void delete(ItemModel item);
}
