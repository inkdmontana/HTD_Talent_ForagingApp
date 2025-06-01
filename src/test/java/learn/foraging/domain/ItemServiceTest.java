package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    ItemService service = new ItemService(new ItemRepositoryDouble());

    @Test
    void shouldNotSaveNullName() throws DataException {
        Item item = new Item(0, null, Category.EDIBLE, new BigDecimal("5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveBlankName() throws DataException {
        Item item = new Item(0, "   \t\n", Category.EDIBLE, new BigDecimal("5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveNullDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, null);
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveNegativeDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("-5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveTooLargeDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("9999.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldSave() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("5.00"));

        Result<Item> result = service.add(item);

        assertNotNull(result.getPayload());
        assertEquals(2, result.getPayload().getId());
    }

    @Test
    void shouldNotSaveDuplicateName() throws DataException {
        Item item = new Item(0, "Chanterelle", Category.MEDICINAL, new BigDecimal("10.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldSaveInedibleWithZeroPrice() throws DataException {
        Item item = new Item(0, "Rock", Category.INEDIBLE, BigDecimal.ZERO);
        Result<Item> result = service.add(item);
        assertTrue(result.isSuccess());
        assertEquals(2, result.getPayload().getId());
    }

    @Test
    void shouldSavePoisonousWithZeroPrice() throws DataException {
        Item item = new Item(0, "Toxic Mushroom", Category.POISONOUS, BigDecimal.ZERO);
        Result<Item> result = service.add(item);
        assertTrue(result.isSuccess());
        assertEquals(2, result.getPayload().getId());
    }

    @Test
    void shouldNotSavePoisonousItemWithNonZeroPrice() throws DataException {
        Item item = new Item(0, "Nightshade", Category.POISONOUS, new BigDecimal("0.01"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().get(0).contains("must be 0"));
    }

    @Test
    void shouldNotSaveInedibleItemWithNonZeroPrice() throws DataException {
        Item item = new Item(0, "Rock", Category.INEDIBLE, new BigDecimal("1.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().get(0).contains("must be 0"));
    }





}