import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.domain.order.OrderModel;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;
import phi.fjpiedade.apiorder.repository.order.OrderRepository;
import phi.fjpiedade.apiorder.repository.user.UserRepository;
import phi.fjpiedade.apiorder.service.OrderService;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderModel order;
    private ItemModel item;
    private UserModel user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        item = new ItemModel();
        item.setId(1L);

        user = new UserModel();
        user.setId(1L);

        order = new OrderModel();
        order.setItem(item);
        order.setUser(user);
        order.setQuantity(10);
    }

    @Test
    public void testGetOrders() {
        List<OrderModel> orders = Collections.singletonList(new OrderModel());
        when(orderService.getAllOrders()).thenReturn(orders);

        List<OrderModel> result = orderService.getAllOrders();
        assertEquals(orders, result);
    }

    @Test
    void testCreateOrderSuccess() {
        when(itemRepository.findById(1L)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(user);
        when(orderRepository.save(any(OrderModel.class))).thenReturn(order);

        OrderModel createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(10, createdOrder.getQuantity());
        assertEquals(item, createdOrder.getItem());
        assertEquals(user, createdOrder.getUser());
        assertNotNull(createdOrder.getCreatedAt());
        assertNotNull(createdOrder.getUpdatedAt());

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCreateOrderWithInvalidItem() {
        when(itemRepository.findById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(order);
        });

        assertEquals("Item does not exist.", exception.getMessage());
    }

    @Test
    void testCreateOrderWithInvalidUser() {
        when(itemRepository.findById(1L)).thenReturn(item);
        when(userRepository.findById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(order);
        });

        assertEquals("User does not exist.", exception.getMessage());
    }

    @Test
    public void testUpdateOrder() {
        // Mock the existing order to update
        OrderModel existingOrder = new OrderModel();
        existingOrder.setId(1L);
        existingOrder.setQuantity(5);

        when(orderRepository.findById(1L)).thenReturn(existingOrder);
        doNothing().when(orderRepository).update(any(OrderModel.class));

        // Change some fields for the update
        existingOrder.setQuantity(10);

        orderService.updateOrder(existingOrder.getId(),existingOrder);

        // Verify that the repository was called correctly
        verify(orderRepository, times(1)).update(existingOrder);
        assertEquals(10, existingOrder.getQuantity());
    }

    @Test
    public void testDeleteOrder() {
        // Mock the existing order to delete
        OrderModel orderToDelete = new OrderModel();
        orderToDelete.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(orderToDelete);
        doNothing().when(orderRepository).delete(orderToDelete);

        // Perform delete
        orderService.deleteOrder(orderToDelete.getId());

        // Verify that the repository was called correctly
        verify(orderRepository, times(1)).delete(orderToDelete);
    }

    @Test
    public void testFindCompletedOrders() {
        List<OrderModel> completedOrders = Collections.singletonList(new OrderModel());

        when(orderRepository.findCompletedOrders()).thenReturn(completedOrders);

        List<OrderModel> result = orderService.getAllCompletedOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findCompletedOrders();
    }

//    @Test
//    public void testFindIncompleteOrdersForItem() {
//        Long itemId = 1L;
//        List<OrderModel> incompleteOrders = Collections.singletonList(new OrderModel());
//
//        when(orderRepository.findIncompleteOrdersForItem(itemId)).thenReturn(incompleteOrders);
//
//        List<OrderModel> result = orderService.findIncompleteOrdersForItem(itemId);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        verify(orderRepository, times(1)).findIncompleteOrdersForItem(itemId);
//    }



}
