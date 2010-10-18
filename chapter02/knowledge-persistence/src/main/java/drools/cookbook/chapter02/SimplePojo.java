package drools.cookbook.chapter02;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SimplePojo {

    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
