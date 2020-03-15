package entities;
/*
 * author paepke
 * version 1.0
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.persistence.*;


@Entity
@Table(name = "CityInfo")
@NamedQueries({
    @NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE FROM CityInfo"),
    @NamedQuery(name = "CityInfo.getAll", query = "SELECT c FROM CityInfo c"),
    @NamedQuery(name = "CityInfo.getByZipCode", query = "SELECT c FROM CityInfo c WHERE c.zipCode = :zipCode"),
    @NamedQuery(name = "CityInfo.getByCity", query = "SELECT c FROM CityInfo c WHERE c.city LIKE :city"),
})
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private int zipCode;
    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "cityInfo", cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

    public CityInfo() {
    }

    public CityInfo(int zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void addAddress(Address address) {
        if(!addresses.contains(address)) {
            addresses.add(address);
        }
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            CityInfo other = (CityInfo)obj;
            return other.getId() == this.id;
        }
    }
}
