/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
@Schema(name = "PersonDTO")
public class PersonDTO {

    @Schema(required = true, example = "1")
    private Integer id;
    @Schema(required = true, example = "person@example.com")
    private String email;
    @Schema(required = true, example = "Frederik")
    private String firstName;
    @Schema(required = true, example = "Jensen")
    private String lastName;

    private HobbyDTOList hobbies = new HobbyDTOList();
    private PhoneDTOList phones = new PhoneDTOList();
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
        this.hobbies = new HobbyDTOList(person.getHobbies());
        this.phones = new PhoneDTOList(person.getPhones());
        if (person.getAddress() != null) {
            this.address = new AddressDTO(person.getAddress());
        }
    }

    public PersonDTO(Person person, HobbyDTOList hobbies, PhoneDTOList phones){
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.hobbies = hobbies;
        this.phones = phones;
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

    public HobbyDTOList getHobbies() {
        return hobbies;
    }
    public void setHobbies(HobbyDTOList hobbies) {
        this.hobbies = hobbies;
    }
    public void addHobby(HobbyDTO hobbyDTO) {
        this.hobbies.add(hobbyDTO);
    }

    public PhoneDTOList getPhones() {
        return phones;
    }
    public void setPhones(PhoneDTOList phones) {
        this.phones = phones;
    }
    public void addPhone(PhoneDTO phoneDTO) {
        this.phones.add(phoneDTO);
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

}
