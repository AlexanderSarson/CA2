package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;
import entities.Address;
import entities.Person;
import exception.AddressNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AddressFacadeTest {

    private static EntityManagerFactory entityManagerFactory;
    private static AddressFacade addressFacade;
    private static Address a1, a2;

    public AddressFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        addressFacade = AddressFacade.getAddressFacade(entityManagerFactory);
        a1 = new Address("Address1", "AI1");
        a2 = new Address("Address2", "AI2");
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(a1);
            em.persist(a2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test public void testGetAll() {
        List<Address> addresses = addressFacade.getAll();
        assertTrue(addresses.contains(a1));
        assertTrue(addresses.contains(a2));
    }
    @Test public void testGetById_with_valid_id() throws AddressNotFoundException {
        int id = a1.getId();
        Address result = addressFacade.getById(id);
        assertEquals(a1, result);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(AddressNotFoundException.class, () -> {
            addressFacade.getById(id);
        });
    }
    @Test public void testGetByStreet_with_valid_street() {
        String street = a1.getStreet();
        List<Address> addresses = addressFacade.getByStreet(street);
        assertEquals(1, addresses.size());
        assertTrue(addresses.contains(a1));
    }
    @Test public void testGetByStreet_with_invalid_street() {
        String street = "";
        List<Address> addresses = addressFacade.getByStreet(street);
        assertEquals(0, addresses.size());
    }
    @Test public void testCreateAddress_with_valid_input() throws MissingInputException {
        Address a3 = new Address("New Street", "This is a new Address");
        int nextId = Math.max(a1.getId(), a2.getId()) + 1;
        addressFacade.create(a3);
        assertEquals(nextId, a3.getId());
    }
    @Test public void testCreateAddress_with_missing_street() {
        Address a3 = new Address(null, "this is just fluff");
        assertThrows(MissingInputException.class, () -> {
            addressFacade.create(a3);
        });
    }
}
