package phi.fjpiedade.api8demo.repository.stock;

import phi.fjpiedade.api8demo.domain.stock.StockModel;

import java.util.List;

public interface StockRepository {
    List<StockModel> findAll();
    StockModel findById(Long id);
    StockModel findByItemId(Long itemId);
    void save(StockModel stock);
    void update(StockModel stock);
    void delete(StockModel stock);
}
