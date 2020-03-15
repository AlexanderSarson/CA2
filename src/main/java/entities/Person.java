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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "PersonHobby",
            joinColumns =  {@JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "HOBBY_ID", referencedColumnName = "ID")}
    )
    private List<Hobby> hobbies = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Phone> phones = new ArrayList<>();

    @ManyToOne
    private Address address;

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
        this.hobbies = dto.getHobbies();
        this.phones = dto.getPhones();
        this.address = dto.getAddress();
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        address.addPerson(this);
    }

    public void addHobby(Hobby hobby) {
        hobbies.add(hobby);
    }

    public List<Hobby> getHobbies() { return hobbies; }

    public void addPhone(Phone phone) {
        // NOTE(Benjamin): Adding the bi-directional relation here! - and not in method phone.setOwner()
        phone.setOwner(this);
        phones.add(phone);
    }

    public List<Phone> getPhones() { return phones; }
}
