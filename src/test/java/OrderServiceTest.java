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


import java.time.LocalDateTime;
import java.util.Arrays;
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
            MockitoAnnotations.openMocks(this);

            item = new ItemModel();
            item.setId(1L);
            item.setName("Item A");

            user = new UserModel();
            user.setId(1L);
            user.setEmail("test@example.com");

            order = new OrderModel();
            order.setId(1L);
            order.setItem(item);
            order.setUser(user);
            order.setQuantity(5);
            order.setFulfilledQuantity(3);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
        }

        @Test
        void testGetAllOrders() {
            when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

            List<OrderModel> result = orderService.getAllOrders();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(order, result.get(0));
            verify(orderRepository, times(1)).findAll();
        }

        @Test
        void testCreateOrder_Success() {
            when(itemRepository.findById(item.getId())).thenReturn(item);
            when(userRepository.findById(user.getId())).thenReturn(user);
            when(orderRepository.save(order)).thenReturn(order);

            OrderModel result = orderService.createOrder(order);

            assertNotNull(result);
            assertEquals(order, result);
            verify(itemRepository, times(1)).findById(item.getId());
            verify(userRepository, times(1)).findById(user.getId());
            verify(orderRepository, times(1)).save(order);
        }

        @Test
        void testCreateOrder_ItemNotFound() {
            when(itemRepository.findById(item.getId())).thenReturn(null);

            OrderModel result = orderService.createOrder(order);

            assertNull(result);
            verify(itemRepository, times(1)).findById(item.getId());
            verify(orderRepository, never()).save(any());
        }

        @Test
        void testCreateOrder_UserNotFound() {
            when(itemRepository.findById(item.getId())).thenReturn(item);
            when(userRepository.findById(user.getId())).thenReturn(null);

            OrderModel result = orderService.createOrder(order);

            assertNull(result);
            verify(itemRepository, times(1)).findById(item.getId());
            verify(userRepository, times(1)).findById(user.getId());
            verify(orderRepository, never()).save(any());
        }

        @Test
        void testGetOrderById() {
            when(orderRepository.findById(order.getId())).thenReturn(order);

            OrderModel result = orderService.getOrderById(order.getId());

            assertNotNull(result);
            assertEquals(order, result);
            verify(orderRepository, times(1)).findById(order.getId());
        }

        @Test
        void testUpdateOrder_Success() {
            OrderModel updatedOrder = new OrderModel();
            updatedOrder.setQuantity(10);
            updatedOrder.setFulfilledQuantity(7);

            when(orderRepository.findById(order.getId())).thenReturn(order);
            when(orderRepository.update(order)).thenReturn(order);

            OrderModel result = orderService.updateOrder(order.getId(), updatedOrder);

            assertNotNull(result);
            assertEquals(updatedOrder.getQuantity(), result.getQuantity());
            assertEquals(updatedOrder.getFulfilledQuantity(), result.getFulfilledQuantity());
            verify(orderRepository, times(1)).findById(order.getId());
            verify(orderRepository, times(1)).update(order);
        }

        @Test
        void testUpdateOrder_OrderNotFound() {
            when(orderRepository.findById(order.getId())).thenReturn(null);

            OrderModel result = orderService.updateOrder(order.getId(), order);

            assertNull(result);
            verify(orderRepository, times(1)).findById(order.getId());
            verify(orderRepository, never()).update(any());
        }

        @Test
        void testDeleteOrder_Success() {
            when(orderRepository.findById(order.getId())).thenReturn(order);

            boolean result = orderService.deleteOrder(order.getId());

            assertTrue(result);
            verify(orderRepository, times(1)).findById(order.getId());
            verify(orderRepository, times(1)).delete(order);
        }

        @Test
        void testDeleteOrder_OrderNotFound() {
            when(orderRepository.findById(order.getId())).thenReturn(null);

            boolean result = orderService.deleteOrder(order.getId());

            assertFalse(result);
            verify(orderRepository, times(1)).findById(order.getId());
            verify(orderRepository, never()).delete(any());
        }

        @Test
        void testGetAllCompletedOrders() {
            when(orderRepository.findCompletedOrders()).thenReturn(Arrays.asList(order));

            List<OrderModel> result = orderService.getAllCompletedOrders();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(order, result.get(0));
            verify(orderRepository, times(1)).findCompletedOrders();
        }
    }
