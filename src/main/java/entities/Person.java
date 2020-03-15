package entities;
/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.persistence.*;


@Entity
@Table(name = "Person")
@NamedNativeQuery(name = "Person.truncate", query = "TRUNCATE TABLE Person")
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE FROM Person"),
    @NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.getByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
    @NamedQuery(name = "Person.deletePerson", query = "DELETE FROM Person p WHERE p.id = :id")
})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "PersonHobby",
            joinColumns =  {@JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "HOBBY_ID", referencedColumnName = "ID")}
    )
    List<Hobby> hobbies = new ArrayList<>();


    public Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Person(PersonDTO dto) {
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.setHobbies(dto.getHobbies());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addHobby(Hobby hobby) {
        hobbies.add(hobby);
    }
    public List<Hobby> getHobbies() { return hobbies; }

    private void setHobbies(List<Hobby> hobbies) { this.hobbies = hobbies; }
}
