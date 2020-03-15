package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;
import entities.Person;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PersonFacadeTest {
    // This value must be one more than the  number of persons created in the setup method.
    private static final int NEXT_CREATED_ID = 3;
    private static EntityManagerFactory emf;
    private static PersonFacade personfacade;
    private Person p1, p2;

    public PersonFacadeTest() {}
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        personfacade = PersonFacade.getPersonFacade(emf);
    }
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.truncate").executeUpdate();
            em.persist(p1 = new Person("Peter", "Pan", "peterPan@gmail.com"));
            em.persist(p2 = new Person("Lars", "Larsen", "larsLarsen@gmail.com"));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test public void testGetAll() {
        List<PersonDTO> dtos = personfacade.getAll();
        assertTrue(dtos.contains(new PersonDTO(p1)));
        assertTrue(dtos.contains(new PersonDTO(p2)));
    }
    @Test public void testGetByEmail_with_invalid_email() {
        String email = "this is not an email";
        assertThrows(PersonNotFoundException.class, () -> {
           personfacade.getByEmail(email);
        });
    }
    @Test public void testGetByEmail_with_valid_email() throws PersonNotFoundException {
        String email = "peterPan@gmail.com";
        PersonDTO dto = personfacade.getByEmail(email);
        assertEquals("Peter", dto.getFirstName());
        assertEquals("Pan", dto.getLastName());
        assertEquals(email, dto.getEmail());
    }
    @Test public void testGetById_with_valid_id() throws PersonNotFoundException {
        int id = p1.getId();
        PersonDTO expected = new PersonDTO(p1);
        PersonDTO result = personfacade.getById(id);
        assertEquals(expected, result);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(PersonNotFoundException.class, () -> {
            personfacade.getById(id);
        });
    }
    @Test public void testCreatePerson_with_valid_input() throws MissingInputException {
        Person p3 = new Person("Benny", "Hill", "bHill@outlook.com");
        PersonDTO result = personfacade.create(new PersonDTO(p3));
        assertEquals(NEXT_CREATED_ID, result.getId());
    }
    @Test public void testCreatePerson_with_invalid_firstName() {
        Person p3 = new Person(null, "Hill", "hill@outlook.com");
        assertThrows(MissingInputException.class, () -> {
           personfacade.create(new PersonDTO(p3));
        });
    }
    @Test public void testCreatePerson_with_invalid_lastName() {
        Person p3 = new Person("Peter", null, "p@outlook.com");
        assertThrows(MissingInputException.class, () -> {
            personfacade.create(new PersonDTO(p3));
        });
    }
    @Test public void testCreatePerson_with_invalid_email() {
        Person p3 = new Person("Peter", "Nielsen", null);
        assertThrows(MissingInputException.class, () ->  {
            personfacade.create(new PersonDTO(p3));
        });
    }
}
