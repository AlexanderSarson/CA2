package dto;

import entities.Address;
import entities.CityInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author paepke
 * @version 1.0
 */
@Schema(name = "AddressDTO")
public class AddressDTO {
    @Schema(required = true, example = "1")
    private Integer id;
    @Schema(required = true, example ="Jernbane Allé 7")
    private String street;
    @Schema(example = "The street along side the train station")
    private String additionalInfo;

    private CityInfoDTO cityInfo;

    public AddressDTO(int id, String street, String additionalInfo) {
        this.id = id;
        this.street = street;
        this.additionalInfo = additionalInfo;
    }

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfoInfo();
        if(address.getCityInfo() != null){
            this.cityInfo = new CityInfoDTO(address.getCityInfo());
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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public CityInfoDTO getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.cityInfo = cityInfo;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) {
            return false;
        } else {
            AddressDTO other = (AddressDTO)obj;
            if(id == null || other.getId() == null) return false;
            return this.id.equals(other.getId());
        }
    }
}
