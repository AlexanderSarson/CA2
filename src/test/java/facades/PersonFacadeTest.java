package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;
import entities.Person;
import entities.Phone;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

public class PersonFacadeTest {
    private static EntityManagerFactory entityManagerFactory;
    private static PersonFacade personFacade;
    private static Person p1, p2;
    private static PersonDTO pd1, pd2;

    public PersonFacadeTest() {}
    @BeforeAll
    public static void setUpClass() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        personFacade = PersonFacade.getPersonFacade(entityManagerFactory);
        p1 = new Person("Peter", "Pan", "peterPan@gmail.com");
        p2 = new Person("Lars", "Larsen", "larsLarsen@gmail.com");
        Phone ph1 = new Phone("70809050", "home phone");
        p1.addPhone(ph1);
    }
    @BeforeEach
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            entityManager.createNamedQuery("Person.deleteAllRows").executeUpdate();
            entityManager.persist(p1);
            entityManager.persist(p2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        pd1 = new PersonDTO(p1);
        pd2 = new PersonDTO(p2);
    }

    @Test
    public void testGetAll() {
        List<PersonDTO> dtos = personFacade.getAll();
        assertTrue(dtos.contains(new PersonDTO(p1)));
        assertTrue(dtos.contains(new PersonDTO(p2)));
    }

    @Test
    public void testGetByEmail_with_invalid_email() {
        String email = "this is not an email";
        assertThrows(PersonNotFoundException.class, () -> {
           personFacade.getByEmail(email);
        });
    }

    @Test
    public void testGetByEmail_with_valid_email() throws PersonNotFoundException {
        String email = "peterPan@gmail.com";
        PersonDTO dto = personFacade.getByEmail(email);
        assertEquals("Peter", dto.getFirstName());
        assertEquals("Pan", dto.getLastName());
        assertEquals(email, dto.getEmail());
    }

    @Test
    public void testGetById_with_valid_id() throws PersonNotFoundException {
        int id = p1.getId();
        PersonDTO expected = new PersonDTO(p1);
        PersonDTO result = personFacade.getById(id);
        assertEquals(expected, result);
    }

    @Test
    public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(PersonNotFoundException.class, () -> {
            personFacade.getById(id);
        });
    }

    @Test
    public void testCreatePerson_with_valid_input() throws MissingInputException {
        Person p3 = new Person("Benny", "Hill", "bHill@outlook.com");
        PersonDTO result = personFacade.create(new PersonDTO(p3));
        // NOTE(Benjamin): This ID must be calculated during runtime, else we have no idea what it will be.
        Integer nextId = Math.max(p1.getId(),p2.getId()) + 1;
        assertEquals(nextId, result.getId());
    }

    @Test
    public void testCreatePerson_with_invalid_firstName() {
        Person p3 = new Person(null, "Hill", "hill@outlook.com");
        assertThrows(MissingInputException.class, () -> {
           personFacade.create(new PersonDTO(p3));
        });
    }

    @Test
    public void testCreatePerson_with_invalid_lastName() {
        Person p3 = new Person("Peter", null, "p@outlook.com");
        assertThrows(MissingInputException.class, () -> {
            personFacade.create(new PersonDTO(p3));
        });
    }

    @Test
    public void testCreatePerson_with_invalid_email() {
        Person p3 = new Person("Peter", "Nielsen", null);
        assertThrows(MissingInputException.class, () ->  {
            personFacade.create(new PersonDTO(p3));
        });
    }

    @Test
    public void testDeletePerson_with_valid_id() throws PersonNotFoundException {
        Integer id = p1.getId();
        PersonDTO person = personFacade.deletePerson(id);
        assertEquals(id, person.getId());;
    }

    @Test
    public void testDeletePerson_with_invalid_id() {
        int id = 1000;
        assertThrows(PersonNotFoundException.class, () ->  {
           personFacade.deletePerson(id);
        });
    }
    
    @Test
    public void testUpdatePerson() throws PersonNotFoundException, MissingInputException{
        String expected = "Mohammed";
        pd1.setFirstName(expected);
        String result = personFacade.updatePerson(pd1).getFirstName();
        assertEquals(expected, result);
    }
    
    @Test
    public void testUpdatePerson_with_missing_input(){
        pd1.setLastName(null);
        assertThrows(MissingInputException.class, () -> {
            personFacade.updatePerson(pd1);
        });
    }

    @Test
    public void testUpdatePerson_with_invalid_id() {
        pd1.setId(1000);
        assertThrows(PersonNotFoundException.class, () -> {
            personFacade.updatePerson(pd1);
        });
    }

    @Test
    public void testGetPerson_with_valid_phoneNumber() throws PersonNotFoundException{
        String phoneNumber = "70809050";
        String expected = "Peter";
        String result = personFacade.getByPhoneNumber(phoneNumber).get(0).getFirstName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPerson_with_invalid_phoneNumber() throws PersonNotFoundException {
        String phoneNumber = "ghjgfhgfh";
        assertThrows(IllegalArgumentException.class, () -> {
            personFacade.getByPhoneNumber(phoneNumber);
        });
    }
}
