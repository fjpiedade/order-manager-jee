package phi.fjpiedade.api8demo.repository.order;

import phi.fjpiedade.api8demo.domain.order.OrderModel;

import java.util.List;

public interface OrderRepository {
    List<OrderModel> findAll();
    OrderModel findById(Long id);
    void save(OrderModel order);
    void delete(OrderModel order);
}
