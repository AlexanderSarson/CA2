/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
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

    private HobbyListDTO hobbies = new HobbyListDTO();
    private PhoneListDTO phones = new PhoneListDTO();
    private Address address;

    public PersonDTO(int id, String email, String firstName, String lastName) {
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
        this.hobbies.setHobbyList(HobbyListDTO.convertToHobbyListDTO(person.getHobbies()));
        this.phones.setPhoneList(PhoneListDTO.convertToHobbyListDTO(person.getPhones()));
        this.address = person.getAddress();
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
    
    public void addHobby(HobbyDTO hobby){
        this.hobbies.addHobby(hobby);
    }
    
    public void addPhone(PhoneDTO phone){
        this.phones.addPhone(phone);
    }
    
    public HobbyListDTO getHobbies(){
        return this.hobbies;
    }
    public PhoneListDTO getPhones(){
        return this.phones;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        } else {
            PersonDTO other = (PersonDTO)obj;
            if(id == null || other.getId() == null) return false;
            return this.id.equals(other.getId());
        }
    }

    public Address getAddress() {
        return address;
    }
}
