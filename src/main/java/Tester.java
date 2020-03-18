import dto.HobbyDTO;
import dto.PersonDTO;
import entities.*;
import exception.MissingInputException;
import facades.HobbyFacade;
import facades.PersonFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Tester {
    public static void main(String[] args) throws MissingInputException {
        EntityManagerFactory entityManagerFactory = EMF_Creator.createEntityManagerFactory(
                EMF_Creator.DbSelector.DEV,
                EMF_Creator.Strategy.DROP_AND_CREATE);

        /* This will not work, currently, as we are trying to persist the same hobby again later
        // Make sure this will work!
        HobbyFacade hobbyFacade = HobbyFacade.getHobbyFacade(entityManagerFactory);
        Hobby hobby = new Hobby("Chess", "We play chess");
        hobbyFacade.create(new HobbyDTO(hobby));
        */

        Person person = new Person("Peter", "Nielsen", "pNielsen@gmail.com");

        Hobby h1 = new Hobby("Chess","We play chess");
        Hobby h2 = new Hobby("Chess Boxing", "Yes, it is what is sounds like");

        Phone p1 = new Phone("11111111", "Work");
        Phone p2 = new Phone("22222222", "Private");

        CityInfo cityInfo = new CityInfo(1111, "Valby");
        Address address = new Address("Langgade", "Extra");
        address.setCityInfo(cityInfo);

        person.addHobby(h1);
        person.addHobby(h2);

        person.addPhone(p1);
        person.addPhone(p2);

        person.setAddress(address);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(cityInfo);
        entityManager.persist(person);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
