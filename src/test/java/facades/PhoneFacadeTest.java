package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PhoneDTO;
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
    private static PhoneDTO pd1, pd2;

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
        pd1 = new PhoneDTO(p1);
        pd2 = new PhoneDTO(p2);
    }

    @Test public void testGetAll() {
        List<PhoneDTO> phones = phoneFacade.getAll();
        assertTrue(phones.contains(pd1));
        assertTrue(phones.contains(pd2));
    }
    @Test public void testGetById_with_valid_id() throws PhoneNotFoundException {
        Integer id = p1.getId();
        PhoneDTO phone = phoneFacade.getById(id);
        assertEquals(pd1, phone);
    }
    @Test public void testGetById_with_invalid_id() {
        Integer id = 1000;
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.getById(id);
        });
    }
    @Test public void testGetByNumber_with_valid_number() throws PhoneNotFoundException {
        String number = p1.getNumber();
        PhoneDTO phone = phoneFacade.getByNumber(number);
        assertEquals(pd1, phone);
    }
    @Test public void testGetByNumber_with_invalid_number() {
        String number = "This is not a number";
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.getByNumber(number);
        });
    }
    @Test public void testCreatePhone_with_valid_input() throws MissingInputException {
        Phone p3 = new Phone("33333333", "New phone who dis?");
        PhoneDTO result = phoneFacade.create(new PhoneDTO(p3));
        Integer nextId = Math.max(p1.getId(), p2.getId()) + 1;
        assertEquals(nextId, result.getId());
    }
    @Test public void testCreatePhone_with_invalid_number() throws MissingInputException {
        Phone p3 = new Phone(null, "New phone who dis?");
        assertThrows(MissingInputException.class, () -> {
            phoneFacade.create(new PhoneDTO(p3));
        });
    }
    @Test public void testCreatePhone_with_non_unique_number() throws MissingInputException {
        Phone p3 = new Phone(p1.getNumber(), "New phone who dis?");
        assertThrows(MissingInputException.class, () -> {
            phoneFacade.create(new PhoneDTO(p3));
        });
    }
    
        @Test public void testUpdatePhone() throws PhoneNotFoundException, MissingInputException{
        String expected = "29803002";
        pd1.setNumber(expected);
        String result = phoneFacade.updatePhone(pd1).getNumber();
        assertEquals(expected, result);
             
    }
    
    @Test public void testUpdatePhone_with_missing_input(){
        pd1.setNumber(null);
        assertThrows(MissingInputException.class, () -> {
            phoneFacade.updatePhone(pd1);
        });
    }
    
    @Test public void testUpdatePhone_with_invalid_id() {
        pd1.setId(1000);
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.updatePhone(pd1);
        });
        
    }
    
    @Test public void testDeletePhone() throws PhoneNotFoundException, MissingInputException{
        int expected = pd1.getId();
        PhoneDTO pd3 = phoneFacade.deletePhone(expected);
        assertEquals(pd1, pd3);
    }
    
    @Test public void testDeletePhone_with_invalid_id(){
        int id = 10000;
        assertThrows(PhoneNotFoundException.class, () -> {
            phoneFacade.deletePhone(id);
        });
    }
}
