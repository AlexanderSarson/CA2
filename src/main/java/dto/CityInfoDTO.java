package dto;

import entities.Address;
import entities.CityInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "CityInfoDTO")
public class CityInfoDTO {
    @Schema(required = true, example = "1")
    private Integer id;
    @Schema(required = true, example = "2800")
    private Integer zipCode;
    @Schema(required = true, example = "Lyngby")
    private String city;

    private List<Address> addresses = new ArrayList<>();

    public CityInfoDTO(Integer id, Integer zipCode, String city) {
        this.id = id;
        this.zipCode = zipCode;
        this.city = city;
    }

    public CityInfoDTO(CityInfo cityInfo) {
        this.id = cityInfo.getId();
        this.zipCode = cityInfo.getZipCode();
        this.city = cityInfo.getCity();
        this.addresses = cityInfo.getAddresses();
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
            CityInfoDTO other = (CityInfoDTO)obj;
            if(this.id == null || other.getId() == null) return false;
            return other.getId().equals(this.id);
        }
    }
}
