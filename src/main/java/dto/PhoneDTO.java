package dto;

import entities.Person;
import entities.Phone;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PhoneDTO")
public class PhoneDTO {
    @Schema(required = true, example = "1")
    private Integer id;
    @Schema(required = true, example = "12 34 56 78")
    private String number;
    @Schema(example = "Work")
    private String description;

    private Person owner;

    public PhoneDTO(Integer id, String number, String description) {
        this.id = id;
        this.number = number;
        this.description = description;
    }

    public PhoneDTO(Phone phone) {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();
        this.owner = phone.getOwner();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            PhoneDTO other = (PhoneDTO)obj;
            if(id == null || other.getId() == null) return false;
            return other.getId().equals(this.id);
        }
    }
}
