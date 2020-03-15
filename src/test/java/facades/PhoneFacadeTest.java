package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.Phone;
import exception.MissingInputException;
import exception.PhoneNotFoundException;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PhoneFacadeTest {

    private static EntityManagerFactory emf;
    private static PhoneFacade phoneFacade;
    private static Phone p1, p2;

    public PhoneFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        phoneFacade = PhoneFacade.getPhoneFacade(emf);
        p1 = new Phone("11111111", "Work");
        p2 = new Phone("22222222", "Private");
    }
    @BeforeEach
    public void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            entityManager.persist(p1);
            entityManager.persist(p2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test public void testGetAll() {
        List<Phone> phones = phoneFacade.getAll();
        assertTrue(phones.contains(p1));
        assertTrue(phones.contains(p2));
    }
    @Test public void testGetById_with_valid_id() throws PhoneNotFoundException {
        int id = p1.getId();
        Phone phone = phoneFacade.getById(id);
        assertEquals(p1, phone);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.getById(id);
        });
    }
    @Test public void testGetByNumber_with_valid_number() throws PhoneNotFoundException {
        String number = p1.getNumber();
        Phone phone = phoneFacade.getByNumber(number);
        assertEquals(p1, phone);
    }
    @Test public void testGetByNumber_with_invalid_number() {
        String number = "This is not a number";
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.getByNumber(number);
        });
    }
    @Test public void testCreatePhone_with_valid_input() throws MissingInputException {
        Phone p3 = new Phone("33333333", "New phone who dis?");
        phoneFacade.create(p3);
        int nextId = Math.max(p1.getId(), p2.getId()) + 1;
        assertEquals(nextId, p3.getId());
    }
    @Test public void testCreatePhone_with_invalid_number() throws MissingInputException {
        Phone p3 = new Phone(null, "New phone who dis?");
        assertThrows(MissingInputException.class, () -> {
            phoneFacade.create(p3);
        });
    }
    @Test public void testCreatePhone_with_non_unique_number() throws MissingInputException {
        Phone p3 = new Phone(p1.getNumber(), "New phone who dis?");
        assertThrows(MissingInputException.class, () -> {
            phoneFacade.create(p3);
        });
    }
}
