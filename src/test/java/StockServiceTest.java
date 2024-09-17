import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.domain.stock.StockModel;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;
import phi.fjpiedade.apiorder.repository.stock.StockRepository;
import phi.fjpiedade.apiorder.service.StockService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {
    @Mock
    private StockRepository stockRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private StockService stockService;

    private StockModel stock;
    private ItemModel item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        item = new ItemModel();
        item.setId(1L);
        item.setName("Test Item");

        stock = new StockModel();
        stock.setId(1L);
        stock.setItem(item);
        stock.setQuantity(10);
    }

    @Test
    void testGetAllStocks() {
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));

        List<StockModel> result = stockService.getAllStocks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getQuantity());

        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetStockById() {
        when(stockRepository.findById(1L)).thenReturn(stock);

        StockModel result = stockService.getStockById(1L);

        assertNotNull(result);
        assertEquals(10, result.getQuantity());

        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateStock() {
        when(itemRepository.findById(1L)).thenReturn(item);
        when(stockRepository.findByItemId(1L)).thenReturn(null);
        when(stockRepository.save(any(StockModel.class))).thenReturn(stock);

        StockModel createdStock = stockService.createStock(stock);

        assertNotNull(createdStock);
        assertEquals(10, createdStock.getQuantity());
        assertNotNull(createdStock.getCreatedAt());
        assertNotNull(createdStock.getUpdatedAt());

        verify(itemRepository, times(1)).findById(1L);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testCreateStockItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(null);

        StockModel result = stockService.createStock(stock);

        assertNull(result);
        verify(stockRepository, times(0)).save(any(StockModel.class));
    }

    @Test
    void testCreateStockItemAlreadyExists() {
        when(itemRepository.findById(1L)).thenReturn(item);
        when(stockRepository.findByItemId(1L)).thenReturn(stock);

        StockModel result = stockService.createStock(stock);

        assertNull(result);
        verify(stockRepository, times(0)).save(any(StockModel.class));
    }

    @Test
    void testUpdateStock() {
        StockModel updatedStock = new StockModel();
        updatedStock.setQuantity(20);

        when(stockRepository.findById(1L)).thenReturn(stock);
        when(stockRepository.update(any(StockModel.class))).thenReturn(stock);

        StockModel result = stockService.updateStock(1L, updatedStock);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        verify(stockRepository, times(1)).update(stock);
    }

    @Test
    void testUpdateStockNotFound() {
        when(stockRepository.findById(1L)).thenReturn(null);

        StockModel result = stockService.updateStock(1L, stock);

        assertNull(result);
        verify(stockRepository, times(0)).update(any(StockModel.class));
    }

    @Test
    void testDeleteStock() {
        when(stockRepository.findById(1L)).thenReturn(stock);
        doNothing().when(stockRepository).delete(stock);

        boolean result = stockService.deleteStock(1L);

        assertTrue(result);
        verify(stockRepository, times(1)).delete(stock);
    }

    @Test
    void testDeleteStockNotFound() {
        when(stockRepository.findById(1L)).thenReturn(null);

        boolean result = stockService.deleteStock(1L);

        assertFalse(result);
        verify(stockRepository, times(0)).delete(any(StockModel.class));
    }

    @Test
    void testCheckStockQuantity() {
        when(stockRepository.findByItemId(1L)).thenReturn(stock);

        int quantity = stockService.checkStockQuantity(item);

        assertEquals(10, quantity);
        verify(stockRepository, times(1)).findByItemId(1L);
    }

    @Test
    void testCheckStockQuantityNotFound() {
        when(stockRepository.findByItemId(1L)).thenReturn(null);

        int quantity = stockService.checkStockQuantity(item);

        assertEquals(-1, quantity);
    }

    @Test
    void testReduceStockQuantity() {
        when(stockRepository.findByItemId(1L)).thenReturn(stock);

        stockService.reduceStockQuantity(item, 2);

        assertEquals(8, stock.getQuantity());
        verify(stockRepository, times(1)).findByItemId(1L);
    }
}
