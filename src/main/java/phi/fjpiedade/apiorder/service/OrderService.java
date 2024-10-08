package phi.fjpiedade.apiorder.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.domain.order.OrderModel;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;
import phi.fjpiedade.apiorder.repository.order.OrderRepository;
import phi.fjpiedade.apiorder.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@ApplicationScoped
public class OrderService {
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private ItemRepository itemRepository;
    @Inject
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService() {
    }

    public List<OrderModel> getAllOrders() {
        logger.info("Fetching order(s)");
        return orderRepository.findAll();
    }

    @Transactional
    public OrderModel createOrder(OrderModel order) {
        ItemModel item = itemRepository.findById(order.getItem().getId());
        if (item == null) {
            logger.error("Item does not exist.");
            //throw new IllegalArgumentException("Item does not exist.");
            return null;
        }

        UserModel user = userRepository.findById(order.getUser().getId());
        if (user == null) {
            logger.error("User does not exist.");
//            throw new IllegalArgumentException("User does not exist.");
            return null;
        }

        order.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        OrderModel orderCreated = orderRepository.save(order);
        logger.info("Create new Order");

        return orderCreated;
    }

    public OrderModel getOrderById(Long id) {
        logger.info("Fetching Order By ID");
        return orderRepository.findById(id);
    }

    @Transactional
    public OrderModel updateOrder(Long id, OrderModel updatedOrder) {
        OrderModel order = orderRepository.findById(id);
        if (order == null) {
            logger.error("Order not found.");
//            throw new IllegalArgumentException("Order not found.");
            return null;
        }


        order.setQuantity(updatedOrder.getQuantity());
        order.setFulfilledQuantity(updatedOrder.getFulfilledQuantity());
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        OrderModel orderUpdated = orderRepository.update(order);
        logger.info("Order updated");
        return orderUpdated;
    }

    @Transactional
    public boolean deleteOrder(Long id) {
        OrderModel order = orderRepository.findById(id);
        if (order == null) {
            logger.error("Order not found on the delete process.");
//            throw new IllegalArgumentException("Order not found.");
            return false;
        }

        logger.info("Order deleted");
        orderRepository.delete(order);
        return true;
    }

    public List<OrderModel> getAllCompletedOrders() {
        logger.info("Fetching completed order(s)");
        return orderRepository.findCompletedOrders();
    }
}
