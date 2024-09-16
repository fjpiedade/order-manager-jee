package phi.fjpiedade.apiorder.service;

import jakarta.inject.Inject;

import org.slf4j.LoggerFactory;
import phi.fjpiedade.apiorder.domain.order.OrderModel;
import phi.fjpiedade.apiorder.repository.order.OrderRepository;

import java.util.List;

import org.slf4j.Logger;
import phi.fjpiedade.apiorder.repository.user.UserRepository;

public class StockOrderService {
    @Inject
    private StockService stockService;

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailService emailService;

    Logger logger = LoggerFactory.getLogger(StockOrderService.class);

    public StockOrderService() {
    }

//    public void trySatisfyOrder(OrderModel order) {
//        int currentQuantityExistingOnStock = stockService.checkStockQuantity(order.getItem());
//
//        if (currentQuantityExistingOnStock > 0) {
//            if (currentQuantityExistingOnStock >= order.getQuantity()) {
//                order.setFulfilledQuantity(order.getQuantity());
//                stockService.reduceStockQuantity(order.getItem(), order.getQuantity());
//                orderCompleted(order);
//            } else if (currentQuantityExistingOnStock < order.getQuantity()) {
//                int quantityNeeded = order.getQuantity() - currentQuantityExistingOnStock;
//                if (quantityNeeded > 0) {
//                    int quantityToUse = (int) Math.min(quantityNeeded, currentQuantityExistingOnStock);
//                    order.setFulfilledQuantity(order.getFulfilledQuantity() + quantityToUse);
//                    stockService.reduceStockQuantity(order.getItem(), quantityToUse);
//                    logger.info("Order not completed, missing quantity of items. Pending: {}", order.getId());
//                }
//            }
//        } else {
//            order.setFulfilledQuantity(0);
//            logger.info("Order Pending, Stock doesn't have enough quantity: {}", order.getId());
//        }
//
//        orderRepository.save(order);
//    }

    public void trySatisfyOrder(OrderModel order) {
        int currentStock = stockService.checkStockQuantity(order.getItem());

        if (isStockAvailable(currentStock)) {
            processOrder(order, currentStock);
        } else {
            handleInsufficientStock(order);
        }

        orderRepository.save(order);
    }

    private boolean isStockAvailable(int currentStock) {
        return currentStock > 0;
    }

    private void processOrder(OrderModel order, int currentStock) {
        int orderQuantity = order.getQuantity();

        if (currentStock >= orderQuantity) {
            fulfillOrder(order, orderQuantity);
        } else {
            partiallyFulfillOrder(order, currentStock);
        }
    }

    private void fulfillOrder(OrderModel order, int quantity) {
        order.setFulfilledQuantity(quantity);
        stockService.reduceStockQuantity(order.getItem(), quantity);
        orderCompleted(order);
    }

    private void partiallyFulfillOrder(OrderModel order, int currentStock) {
        int remainingQuantity = order.getQuantity() - currentStock;

        if (remainingQuantity > 0) {
            int quantityToUse = (int) Math.min(remainingQuantity, currentStock);
            order.setFulfilledQuantity(order.getFulfilledQuantity() + quantityToUse);
            stockService.reduceStockQuantity(order.getItem(), quantityToUse);
            logger.info("Order not completed, missing quantity of items. Pending: {}", order.getId());
        }
    }

    private void handleInsufficientStock(OrderModel order) {
        order.setFulfilledQuantity(0);
        logger.info("Order Pending, Stock doesn't have enough quantity: {}", order.getId());
    }

    private void orderCompleted(OrderModel order) {
        String subject = "Your Order Completed!";
        String body = "Hi Dear " + userRepository.findById(order.getItem().getId()).getName() + ",\n\n" +
                "Your order of with ID: " + order.getId() + " and "+order.getQuantity()+" items was completed!.\n" +
                "Thanks for choosing us!";

        emailService.sendEmail(userRepository.findById(order.getItem().getId()).getEmail(), subject, body);

        logger.info("Order completed and email sent to user, Order ID: {}", order.getId());
    }

    public List<OrderModel> getPendingOrdersForItem(Long itemId) {
        logger.info("Get padding Order");
        return orderRepository.findIncompleteOrdersForItem(itemId);
    }
}
