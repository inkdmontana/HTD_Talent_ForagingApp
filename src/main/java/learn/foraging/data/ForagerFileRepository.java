package learn.foraging.data;

import learn.foraging.models.Forager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ForagerFileRepository implements ForagerRepository {

    private final String filePath;

    public ForagerFileRepository(@Value("./data/foragers.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Forager> findAll() {
        ArrayList<Forager> result = new ArrayList<>();
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
    public Forager findById(String id) {
        return findAll().stream()
                .filter(i -> i.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Forager> findByState(String stateAbbr) {
        return findAll().stream()
                .filter(i -> i.getState().equalsIgnoreCase(stateAbbr))
                .collect(Collectors.toList());
    }
    
    private Forager deserialize(String[] fields) {

        Forager result = new Forager();
        result.setId(fields[0]);

        String lastName = fields[1].replace("@@@", ",");
        String firstName = fields[2].replace("@@@", ",");
        result.setLastName(lastName);
        result.setFirstName(firstName);
        result.setState(fields[3]);
        return result;
    }

    @Override
    public Forager add(Forager forager) throws DataException {
        List<Forager> all = findAll();
        all.add(forager);
        writeAll(all);
        return forager;
    }

    private void writeAll(List<Forager> foragers) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("id,last_name,first_name,state");
            for (Forager forager : foragers) {

                String lastName = forager.getLastName().replace(",", "@@@");
                String firstName = forager.getFirstName().replace(",", "@@@");

                writer.println(String.format("%s,%s,%s,%s",
                        forager.getId(),
                        lastName,
                        firstName,
                        forager.getState()));
            }
        }catch (IOException ex) {
            throw new DataException("Could not write to file", ex);
        }
    }
}
