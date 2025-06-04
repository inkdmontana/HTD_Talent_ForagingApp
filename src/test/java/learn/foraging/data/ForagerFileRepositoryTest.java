package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {

    static final String SEED_PATH = "./data/foragers-seed.csv";
    static final String TEST_PATH = "./data/foragers-test.csv";
    static final int FORAGER_COUNT = 4;

    ForagerFileRepository repo = new ForagerFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Forager> all = repo.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void shouldFindById() {
        Forager result = repo.findById("33333333-3333-3333-3333-333333333333");
        assertNotNull(result);
        assertEquals("Montana", result.getLastName());
    }

    @Test
    void shouldReturnNullWhenIdNotFound() {
        Forager result = repo.findById("99999999-9999-9999-9999-999999999999");
        assertNull(result);
    }
}