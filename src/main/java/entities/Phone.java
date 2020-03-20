package entities;
/*
 * author paepke
 * version 1.0
 */

import dto.PhoneDTO;

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
    private Integer id;
    @Column(nullable = false, unique = true)
    private String number;
    private String description;

    @ManyToOne
    @JoinColumn(name="OWNER_ID", referencedColumnName = "ID")
    private Person owner;

    public Phone() {
    }

    public Phone(String number, String description) {
        this.number = number;
        this.description = description;
    }

    public Phone(PhoneDTO dto) {
        this.id = dto.getId();
        this.number = dto.getNumber();
        this.description = dto.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
            if(other.getId() == null || this.id == null) return false;
            return other.getId() == this.id;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static boolean isValidDanishNumber(String input) {
        return isValidPhoneNumber(input,8);
    }

    private static boolean isValidPhoneNumber(String input, int max) {
        if(input.length() > max) return false;
        return input.matches("[0-9]+");
    }
}
