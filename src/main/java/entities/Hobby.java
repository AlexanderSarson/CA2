package entities;
/*
 * author paepke
 * version 1.0
 */

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "Hobby")
@NamedQueries({
    @NamedQuery(name = "Hobby.deleteAllRows", query = "DELETE FROM Hobby"),
    @NamedQuery(name = "Hobby.findByName", query = "SELECT h FROM Hobby h WHERE h.name LIKE :name"),
    @NamedQuery(name = "Hobby.getAll", query = "SELECT h FROM Hobby h"),
})
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;

    public Hobby() {
    }

    public Hobby(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            Hobby other = (Hobby)obj;
            return other.getId() == this.id;
        }
    }
}
