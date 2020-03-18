package entities;

/*
 * author paepke
 * version 1.0
 */

import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Person")
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE FROM Person"),
    @NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.getByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
    @NamedQuery(name = "Person.deletePerson", query = "DELETE FROM Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.getByPhoneNumber", query = "SELECT p FROM Person p JOIN p.phones ph WHERE ph.number = :number"),
    @NamedQuery(name = "Person.getByHobbyCount", query = "SELECT COUNT(p) FROM Person p JOIN p.hobbies h WHERE h.name = :hobby"),
    @NamedQuery(name = "Person.getByHobby", query = "SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :hobby"),
    @NamedQuery(name = "Person.getByZipCode", query = "SELECT p FROM Person p JOIN P.address a JOIN a.cityInfo c WHERE c.zipCode = :city")
})

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinTable(
            name = "PersonHobby",
            joinColumns = {
                @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "HOBBY_ID", referencedColumnName = "ID")}
    )
    private List<Hobby> hobbies = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Phone> phones = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
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
        this.hobbies = HobbyDTO.convertToHobby(dto.getHobbies());
        this.phones = PhoneDTO.convertToPhone(dto.getPhones());
        if(dto.getAddress() != null) {
            this.address = new Address(dto.getAddress());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        hobby.addPerson(this);
        if (!hobbies.contains(hobby)) {
            hobbies.add(hobby);
        }
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void addPhone(Phone phone) {
        phone.setOwner(this);
        if (!phones.contains(phone)) {
            phones.add(phone);
        }
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            Person other = (Person) obj;
            if (this.id == null || other.getId() == null) {
                return false;
            }
            return other.getId().equals(this.id);
        }
    }
}
