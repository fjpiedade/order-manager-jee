package phi.fjpiedade.apiorder.repository.stock;

import phi.fjpiedade.apiorder.domain.stock.StockModel;

import java.util.List;

public interface StockRepository {
    List<StockModel> findAll();
    StockModel findById(Long id);
    StockModel findByItemId(Long itemId);
    void save(StockModel stock);
    void update(StockModel stock);
    void delete(StockModel stock);
}
