package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.AddressDTO;
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
    private static AddressDTO ad1, ad2;

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
        ad1 = new AddressDTO(a1);
        ad2 = new AddressDTO(a2);
    }

    @Test public void testGetAll() {
        List<AddressDTO> addresses = addressFacade.getAll();
        assertTrue(addresses.contains(new AddressDTO(a1)));
        assertTrue(addresses.contains(new AddressDTO(a2)));
    }
    @Test public void testGetById_with_valid_id() throws AddressNotFoundException {
        Integer id = a1.getId();
        AddressDTO result = addressFacade.getById(id);
        assertEquals(new AddressDTO(a1), result);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(AddressNotFoundException.class, () -> {
            addressFacade.getById(id);
        });
    }
    @Test public void testGetByStreet_with_valid_street() {
        String street = a1.getStreet();
        List<AddressDTO> addresses = addressFacade.getByStreet(street);
        assertEquals(1, addresses.size());
        assertTrue(addresses.contains(new AddressDTO(a1)));
    }
    @Test public void testGetByStreet_with_invalid_street() {
        String street = "";
        List<AddressDTO> addresses = addressFacade.getByStreet(street);
        assertEquals(0, addresses.size());
    }
    @Test public void testCreateAddress_with_valid_input() throws MissingInputException {
        Address a3 = new Address("New Street", "This is a new Address");
        Integer nextId = Math.max(a1.getId(), a2.getId()) + 1;
        AddressDTO result = addressFacade.create(new AddressDTO(a3));
        assertEquals(nextId, result.getId());
    }
    @Test public void testCreateAddress_with_missing_street() {
        Address a3 = new Address(null, "this is just fluff");
        assertThrows(MissingInputException.class, () -> {
            addressFacade.create(new AddressDTO(a3));
        });
    }
    
    @Test public void testUpdateAddress() throws AddressNotFoundException, MissingInputException{
        String expected = "Vej Allé 1";
        ad1.setStreet(expected);
        String result = addressFacade.updateAddress(ad1).getStreet();
        assertEquals(expected, result);
             
    }
    
    @Test public void testUpdateAddress_with_missing_input(){
        ad1.setStreet(null);
        assertThrows(MissingInputException.class, () -> {
            addressFacade.updateAddress(ad1);
        });
    }
    
    @Test public void testUpdateAddress_with_invalid_id() {
        ad1.setId(1000);
        assertThrows(AddressNotFoundException.class, () -> {
            addressFacade.updateAddress(ad1);
        });
        
    }
    
    @Test public void testDeleteAddress() throws AddressNotFoundException, MissingInputException{
        int expected = ad1.getId();
        AddressDTO ad3 = addressFacade.deleteAddress(expected);
        assertEquals(ad1, ad3);
    }
    
    @Test public void testDeleteAddress_with_invalid_id(){
        int id = 10000;
        assertThrows(AddressNotFoundException.class, () -> {
            addressFacade.deleteAddress(id);
        });
    }
    
}
