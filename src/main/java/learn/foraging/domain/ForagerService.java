package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForagerService {

    private final ForagerRepository repository;

    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        String lowerPrefix = prefix.toLowerCase();
        return repository.findAll().stream()
                .filter(f -> f.getLastName() != null && f.getLastName().toLowerCase().startsWith(lowerPrefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> add(Forager forager) throws DataException {
        Result<Forager> result = validate(forager);
        if (!result.isSuccess()) {
            return result;
        }

        boolean duplicate = repository.findAll().stream()
                .anyMatch(f ->
                        f.getFirstName().equalsIgnoreCase(forager.getFirstName())
                                && f.getLastName().equalsIgnoreCase(forager.getLastName())
                                && f.getState().equalsIgnoreCase(forager.getState()));

        if (duplicate) {
            result.addErrorMessage("Cannot add duplicate Forager.");
            return result;
        }

        forager.setId(java.util.UUID.randomUUID().toString());
        Forager added = repository.add(forager);
        result.setPayload(added);
        return result;
    }


    private Result<Forager> validate(Forager forager) {
        Result<Forager> result = new Result<>();

        if (forager == null) {
            result.addErrorMessage("Forager must not be null.");
            return result;
        }
        if (forager.getFirstName() == null || forager.getFirstName().isBlank()) {
            result.addErrorMessage("First name is required.");
        }
        if (forager.getLastName() == null || forager.getLastName().isBlank()) {
            result.addErrorMessage("Last name is required.");
        }
        if (forager.getState() == null || forager.getState().isBlank()) {
            result.addErrorMessage("State is required.");
        } else if (forager.getState().length() != 2) {
            result.addErrorMessage("State must be abbreviation. Two letters only.");
        }
        return result;
    }


}
