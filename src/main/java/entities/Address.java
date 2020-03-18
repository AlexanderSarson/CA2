package entities;

/*
 * author paepke
 * version 1.0
 */

import dto.AddressDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Address")
@NamedQueries({
    @NamedQuery(name = "Address.deleteAllRows", query = "DELETE FROM Address"),
    @NamedQuery(name = "Address.getAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.getByStreet", query = "SELECT a FROM Address a WHERE a.street LIKE :street"),})
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String street;
    private String additionalInfo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private CityInfo cityInfo;

    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private List<Person> persons = new ArrayList<>();

    public Address() {
    }

    public Address(String street, String additionalInfo) {
        this.street = street;
        this.additionalInfo = additionalInfo;
    }

    public Address(AddressDTO addressDTO) {
        this.id = addressDTO.getId();
        this.street = addressDTO.getStreet();
        this.additionalInfo = addressDTO.getAdditionalInfo();
        if(addressDTO.getCityInfo() != null){
            this.cityInfo = new CityInfo(addressDTO.getCityInfo());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfoInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfoInfo(String addtionalInfo) {
        this.additionalInfo = addtionalInfo;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
        if (cityInfo != null) {
            cityInfo.addAddress(this);
        }
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public List<Person> getPersons() {
        return persons;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            Address other = (Address) obj;
            return other.getId() == this.id;
        }
    }
}
