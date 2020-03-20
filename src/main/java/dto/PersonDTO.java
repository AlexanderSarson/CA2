/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
@Schema(name = "Person")
public class PersonDTO {

    @Hidden
    private Integer id;
    @Schema(required = true, example = "person@example.com")
    private String email;
    @Schema(required = true, example = "Frederik")
    private String firstName;
    @Schema(required = true, example = "Jensen")
    private String lastName;

    private List<HobbyDTO> hobbies = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private AddressDTO address;

    public PersonDTO() {
    }

    public PersonDTO(Integer id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.hobbies = HobbyDTO.convertToHobbyDTO(person.getHobbies());
        this.phones = PhoneDTO.convertToPhoneDTO(person.getPhones());
        if (person.getAddress() != null) {
            this.address = new AddressDTO(person.getAddress());
        }
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            PersonDTO other = (PersonDTO) obj;
            if (id == null || other.getId() == null) {
                return false;
            }
            return this.id.equals(other.getId());
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void addHobby(HobbyDTO h1) {
        hobbies.add(h1);
    }

    public void addPhone(PhoneDTO ph1) {
        phones.add(ph1);
    }
}
