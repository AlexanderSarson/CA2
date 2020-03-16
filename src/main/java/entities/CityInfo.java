package entities;
/*
 * author paepke
 * version 1.0
 */

import dto.CityInfoDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private Integer id;
    @Column(unique = true, nullable = false)
    private Integer zipCode;
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

    public CityInfo(CityInfoDTO cityInfoDTO) {
        this.id = cityInfoDTO.getId();
        this.zipCode = cityInfoDTO.getZipCode();
        this.city = cityInfoDTO.getCity();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
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
            if(this.id == null || other.getId() == null) return false;
            return other.getId().equals(this.id);
        }
    }
}
