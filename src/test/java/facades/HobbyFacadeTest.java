package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.HobbyDTO;
import entities.Hobby;
import exception.HobbyNotFoundException;
import exception.MissingInputException;
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


public class HobbyFacadeTest {

    private static EntityManagerFactory entityManagerFactory;
    private static HobbyFacade hobbyFacade;
    private static Hobby h1, h2;
    private static HobbyDTO hd1, hd2;
    public HobbyFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        hobbyFacade = HobbyFacade.getHobbyFacade(entityManagerFactory);
        h1 = new Hobby("Chess", "What we do is just play chess");
        h2 = new Hobby("Chess-boxing", "Yes it is what it sounds like");
    }

    @BeforeEach
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            entityManager.persist(h1);
            entityManager.persist(h2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        hd1 = new HobbyDTO(h1);
        hd2 = new HobbyDTO(h2);
    }

    @Test public void testGetAll() {
        List<HobbyDTO> hobbies = hobbyFacade.getAll();
        assertTrue(hobbies.contains(hd1));
        assertTrue(hobbies.contains(hd2));
    }
    @Test public void testGetById_with_valid_id() throws HobbyNotFoundException {
        Integer id = h1.getId();
        HobbyDTO hobby = hobbyFacade.getById(id);
        assertEquals(hd1,hobby);
    }
    @Test public void testGetById_with_invalid_id() {
        Integer id = 1000;
        assertThrows(HobbyNotFoundException.class, () -> {
            hobbyFacade.getById(id);
        });
    }
    @Test public void testGetByName_with_valid_name() throws HobbyNotFoundException {
        String name = h1.getName();
        HobbyDTO hobby = hobbyFacade.getByName(name);
        assertEquals(hd1, hobby);
    }
    @Test public void testGetByName_with_invalid_name() {
        String name = "This is not a hobby";
        assertThrows(HobbyNotFoundException.class, () -> {
            hobbyFacade.getByName(name);
        });
    }
    @Test public void testCreateHobby_with_valid_input() throws MissingInputException {
        Hobby hobby = new Hobby("Tennis", "We play alot of tennis");
        Integer expectedID = Math.max(h1.getId(),h2.getId()) + 1;
        HobbyDTO result = hobbyFacade.create(new HobbyDTO(hobby));
        assertEquals(expectedID, result.getId());
    }
    @Test public void testCreateHobby_with_missing_name() {
        Hobby hobby = new Hobby(null, "we are missing a name");
        assertThrows(MissingInputException.class, () -> {
           hobbyFacade.create(new HobbyDTO(hobby));
        });
    }
    @Test public void testCreateHobby_with_non_unique_name() {
        Hobby hobby = new Hobby(h1.getName(), "We do not have a unique name");
        assertThrows(MissingInputException.class, () ->  {
           hobbyFacade.create(new HobbyDTO(hobby));
        });
    }

}
