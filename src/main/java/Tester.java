import dto.AddressDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;
import entities.*;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import facades.AddressFacade;
import facades.HobbyFacade;
import facades.PersonFacade;
import facades.PhoneFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Tester {
    public static void testDuplicateHobby(EntityManagerFactory entityManagerFactory) throws MissingInputException {
        HobbyFacade hobbyFacade = HobbyFacade.getHobbyFacade(entityManagerFactory);
        PhoneFacade phoneFacade = PhoneFacade.getPhoneFacade(entityManagerFactory);
        AddressFacade addressFacade = AddressFacade.getAddressFacade(entityManagerFactory);

        Hobby hobby = new Hobby("Chess", "We play chess");
        hobbyFacade.create(new HobbyDTO(hobby));

        Phone phone = new Phone("11111111", "Work");
        phoneFacade.create(new PhoneDTO(phone));

        CityInfo cityInfo = new CityInfo(2000,"Valby");
        Address address = new Address("Langgade", "Extra");
        address.setCityInfo(cityInfo);

        addressFacade.create(new AddressDTO(address));
    }

    public static void main(String[] args) throws MissingInputException {
        EntityManagerFactory entityManagerFactory = EMF_Creator.createEntityManagerFactory(
                EMF_Creator.DbSelector.DEV,
                EMF_Creator.Strategy.DROP_AND_CREATE);


        // SETUP PRE-RUN TEST CASES
        testDuplicateHobby(entityManagerFactory);


        // The following will create a Person with all relations attached
        Person person = new Person("Peter", "Nielsen", "pNielsen@gmail.com");
        Person person2 = new Person("Lars", "Larsen", "larsen@gmail.com");
        PersonFacade pf = PersonFacade.getPersonFacade(entityManagerFactory);

        Hobby h1 = new Hobby("Chess","We play chess");
        Hobby h2 = new Hobby("Chess Boxing", "Yes, it is what is sounds like");

        Phone p1 = new Phone("11111111", "Work");
        Phone p2 = new Phone("22222222", "Private");

        Phone p11 = new Phone("11111112", "Private");

        CityInfo cityInfo = new CityInfo(2000, "Valby");
        Address address = new Address("Langgade", "Extra");
        address.setCityInfo(cityInfo);

        person.addHobby(h1);
        person.addHobby(h2);
        person.addPhone(p1);
        person.addPhone(p2);
        person.setAddress(address);

        person2.addHobby(h2);
        person2.addPhone(p11);
        person2.setAddress(address);

        PersonFacade personFacade = PersonFacade.getPersonFacade(entityManagerFactory);
        personFacade.create(new PersonDTO(person));
        personFacade.create(new PersonDTO(person2));
    }
}
