package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {
    ForagerService service = new ForagerService(new ForagerRepositoryDouble());

    @Test
    void shouldAddValidForager() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Johnny");
        forager.setLastName("Begood");
        forager.setState("AZ");

        Result<Forager> result = service.add(forager);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("Johnny", result.getPayload().getFirstName());
        assertEquals("Begood", result.getPayload().getLastName());
        assertEquals("AZ", result.getPayload().getState());

    }

    @Test
    void shouldNotAddDuplicateForager() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Johnny");
        forager.setLastName("Begood");
        forager.setState("AZ");

        Result<Forager> result = service.add(forager);
        assertTrue(result.isSuccess());

        result = service.add(forager);
        assertFalse(result.isSuccess());
        assertEquals("Cannot add duplicate Forager.", result.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddInvalidStateForager() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setLastName("Deer");
        forager.setState("nyc");

        Result<Forager> result = service.add(forager);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());

    }

    @Test
    void shouldNotAddInvalidFirstName() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("");
        forager.setLastName("Deer");
        forager.setState("NY");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
    }

    @Test
    void shouldNotAddInvalidLastName() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setLastName("");
        forager.setState("NY");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
    }


}