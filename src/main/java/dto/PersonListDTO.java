/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class PersonListDTO {
    private List<PersonDTO> personList = new ArrayList<>();

    public PersonListDTO() {
    }
    
    public void addPerson(PersonDTO person){
        this.personList.add(person);
    }

    public List<PersonDTO> getPersonList() {
        return personList;
    }
    
    public List<Person> convertToPersonList(){
        List<Person> persons = new ArrayList<>();
        this.personList.forEach(personDTO -> persons.add(new Person(personDTO)));
        return persons;
    }
    
    public static List<PersonDTO> convertToPersonListDTO(List<Person> persons){
        List<PersonDTO> personDTOList = new ArrayList<>();
        persons.forEach(person -> personDTOList.add(new PersonDTO(person)));
        return personDTOList;
    }

    public void setPersonList(List<PersonDTO> personList) {
        this.personList = personList;
    }
    
}
