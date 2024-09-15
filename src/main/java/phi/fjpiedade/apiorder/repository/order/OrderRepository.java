package phi.fjpiedade.apiorder.repository.order;

import phi.fjpiedade.apiorder.domain.order.OrderModel;

import java.util.List;

public interface OrderRepository {
    List<OrderModel> findAll();
    OrderModel findById(Long id);
    void save(OrderModel order);
    void update(OrderModel order);
    void delete(OrderModel order);
    List<OrderModel> findIncompleteOrdersForItem(Long itemId);
}
