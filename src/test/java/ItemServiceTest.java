import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.repository.item.ItemRepository;
import phi.fjpiedade.apiorder.service.ItemService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemModel item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        item = new ItemModel();
        item.setId(1L);
        item.setName("Test Item");
    }

    @Test
    void testGetAllItems() {
        List<ItemModel> items = Collections.singletonList(item);
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemModel> result = itemService.getAllItems();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testCreateItem() {
        when(itemRepository.save(any(ItemModel.class))).thenReturn(item);

        ItemModel createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        assertEquals("Test Item", createdItem.getName());
        assertNotNull(createdItem.getCreatedAt());
        assertNotNull(createdItem.getUpdatedAt());

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testGetItemById() {
        when(itemRepository.findById(1L)).thenReturn(item);

        ItemModel result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals("Test Item", result.getName());

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateItem() {
        ItemModel updatedItem = new ItemModel();
        updatedItem.setName("Updated Item");

        when(itemRepository.findById(1L)).thenReturn(item);

        itemService.updateItem(1L, updatedItem);

        assertEquals("Updated Item", item.getName());
        verify(itemRepository, times(1)).update(item);
    }

    @Test
    void testUpdateItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(null);

        ItemModel result = itemService.updateItem(1L, item);

        assertNull(result);
        verify(itemRepository, times(0)).update(any(ItemModel.class));
    }

    @Test
    void testDeleteItem() {
        when(itemRepository.findById(1L)).thenReturn(item);
        doNothing().when(itemRepository).delete(item);

        boolean result = itemService.deleteItem(1L);

        assertTrue(result);
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void testDeleteItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(null);

        boolean result = itemService.deleteItem(1L);

        assertFalse(result);
        verify(itemRepository, times(0)).delete(any(ItemModel.class));
    }
}
