package entities;
/*
 * author paepke
 * version 1.0
 */

import dto.HobbyDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "hobbies")
    List<Person> persons = new ArrayList<>();

    public Hobby() {
    }

    public Hobby(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Hobby(HobbyDTO hobbyDTO) {
        this.id = hobbyDTO.getId();
        this.name = hobbyDTO.getName();
        this.description = hobbyDTO.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void addPerson(Person person) {
        if(!persons.contains(person)){
            persons.add(person);
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            Hobby other = (Hobby)obj;
            if(this.id == null || other.getId() == null) return false;
            return other.getId().equals(this.id);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, persons);
    }
}
