# Module04-Assessment-sustainable-foragingFor
Student Facing Exercises and Assessments for the Java Async Program

    [x] Add a Forager
    * Does the ForagerService class contain the necessary validations?
    [x] Report: Item kg/day
    * Is the Stream API code located in the domain or data layer?
    [x] Report: Category $/day
    * Is the Stream API code located in the domain or data layer?
    [x] Missing Feature (View Foragers)
    * Did the trainee use the existing ForagerService and ForagerFileRepository findByState() methods?
    [x] Validation (Duplicate Forages)
    * This validation could be addressed two different ways: reject duplicate forages using a validation or allow duplicate forages by updating the existing forage instead of creating a new forage
    * Were the test doubles updated to support unit testing this validation?
    [x] Resolved Existing Bugs
    * The ItemService.add() method doesn't validate that the item category has been provided
    * The ItemService.add() method doesn't correctly validate the $/Kg field value
        * If the category is edible, medicinal, validate that the $/kr is between $0.01 and $7500.00
        * If the category is inedible, poisonous, validate that the $/kg is $0
    * The ItemFileRepository doesn't protect against the delimiter being added in the Name field
    [x] Domain and Data Layer Testing (new code only)
    * Are both the happy and unhappy paths fully tested?
    [x] Spring DI
    [x] File Formats Unaltered
    [x] Proper BigDecimal/LocalDate Usage
    [x] Java Idioms (excellent layering, class design, method responsibilities, and naming)
    [x] Pattern Awareness (complete understanding of patterns: repository, service, MVC)