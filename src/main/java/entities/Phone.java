package entities;
/*
 * author paepke
 * version 1.0
 */

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "Phone")
@NamedQueries({
    @NamedQuery(name = "Phone.deleteAllRows", query = "DELETE FROM Phone"),
    @NamedQuery(name = "Phone.getAll", query = "SELECT p FROM Phone p"),
    @NamedQuery(name = "Phone.getByNumber", query = "SELECT p FROM Phone p WHERE p.number LIKE :number")
})
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String number;
    private String description;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")
    private Person owner;

    public Phone() {
    }

    public Phone(String number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            Phone other = (Phone)obj;
            return other.getId() == this.id;
        }
    }
}
