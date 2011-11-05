package drools.cookbook.chapter08.domain;

/**
 * 
 * @author Lucas Amador
 * 
 */
public enum TrainingLevel {

    SENIOR("sr"), SEMISENIOR("ssr"), JUNIOR("jr"), TRAINEE("tr");

    private String abreviation;

    private TrainingLevel(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getAbreviation() {
        return this.abreviation;
    }

}
