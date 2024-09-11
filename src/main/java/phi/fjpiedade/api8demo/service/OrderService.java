package phi.fjpiedade.api8demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.fjpiedade.api8demo.domain.item.ItemModel;
import phi.fjpiedade.api8demo.domain.order.OrderModel;
import phi.fjpiedade.api8demo.domain.user.UserModel;
import phi.fjpiedade.api8demo.repository.item.ItemRepository;
import phi.fjpiedade.api8demo.repository.order.OrderRepository;
import phi.fjpiedade.api8demo.repository.user.UserRepository;

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

    Logger logger = LoggerFactory.getLogger(StockOrderService.class);

    public OrderService() {
    }

//    @Inject
//    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, UserRepository userRepository) {
//        this.orderRepository = orderRepository;
//        this.itemRepository = itemRepository;
//        this.userRepository = userRepository;
//    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderModel createOrder(OrderModel order) {
        ItemModel item = itemRepository.findById(order.getItem().getId());

        logger.info("Order to be create: " + order);

        if (item == null) {
            logger.error("Item does not exist: " + order.getItem());
            throw new IllegalArgumentException("Item does not exist.");
        }

        UserModel user = userRepository.findById(order.getUser().getId());
        if (user == null) {
            logger.error("User does not exist: " + order.getUser());
            throw new IllegalArgumentException("User does not exist.");
        }

        order.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        orderRepository.save(order);
        logger.info("Create new Order: " + order.getId());

        return order;
    }

    public OrderModel getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public OrderModel updateOrder(Long id, OrderModel updatedOrder) {
        OrderModel order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        order.setQuantity(updatedOrder.getQuantity());
        order.setFulfilledQuantity(updatedOrder.getFulfilledQuantity());
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        orderRepository.update(order);

        return order;
    }

    @Transactional
    public void deleteOrder(Long id) {
        OrderModel order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        orderRepository.delete(order);
    }
}
