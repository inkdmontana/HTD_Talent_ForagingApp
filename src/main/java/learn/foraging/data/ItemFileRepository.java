package learn.foraging.data;

import learn.foraging.models.Category;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemFileRepository implements ItemRepository {

    private static final String HEADER = "id,name,category,dollars/kilogram";
    private final String filePath;

    public ItemFileRepository(@Value("./data/items.txt") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Item> findAll() {
        ArrayList<Item> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Item findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }
    @Override
    public Item add(Item item) throws DataException {

        if (item == null) {
            return null;
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new DataException("Item name is required.");
        }

        List<Item> all = findAll();
        boolean isDuplicate = all.stream().anyMatch(i -> i.getName().equalsIgnoreCase(item.getName()));

        if (isDuplicate) {
            throw new DataException(String.format("Item '%s' is a duplicate.", item.getName())) ;
        }

        int nextId = all.stream()
                .mapToInt(Item::getId)
                .max()
                .orElse(0) + 1;

        item.setId(nextId);

        all.add(item);
        writeAll(all);

        return item;
    }

    public boolean update(Item item) throws DataException {

        if (item == null) {
            return false;
        }

        List<Item> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (item.getId() == all.get(i).getId()) {
                all.set(i, item);
                writeAll(all);
                return true;
            }
        }

        return false;
    }

    private String serialize(Item item) {
        String nameWithoutCommas = item.getName().replaceAll(",", "@@@");
        return String.format("%s,%s,%s,%s",
                item.getId(),
                nameWithoutCommas,
                item.getCategory(),
                item.getDollarPerKilogram());
    }

    private Item deserialize(String[] fields) {
        Item result = new Item();
        result.setId(Integer.parseInt(fields[0]));

        String nameWithCommas = fields[1].replace("@@@", ",");
        result.setName(nameWithCommas);
        result.setCategory(Category.valueOf(fields[2]));
        result.setDollarPerKilogram(new BigDecimal(fields[3]));
        return result;
    }

    protected void writeAll(List<Item> items) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            if (items == null) {
                return;
            }

            for (Item item : items) {
                writer.println(serialize(item));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

}
