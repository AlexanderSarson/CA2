package facades;
/*
 * author paepke
 * version 1.0
 */

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
    }

    @Test public void testGetAll() {
        List<Hobby> hobbies = hobbyFacade.getAll();
        assertTrue(hobbies.contains(h1));
        assertTrue(hobbies.contains(h2));
    }
    @Test public void testGetById_with_valid_id() throws HobbyNotFoundException {
        int id = h1.getId();
        Hobby hobby = hobbyFacade.getById(id);
        assertEquals(h1,hobby);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(HobbyNotFoundException.class, () -> {
            hobbyFacade.getById(id);
        });
    }
    @Test public void testGetByName_with_valid_name() throws HobbyNotFoundException {
        String name = h1.getName();
        Hobby hobby = hobbyFacade.getByName(name);
        assertEquals(h1, hobby);
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
        hobbyFacade.create(hobby);
        assertEquals(expectedID, hobby.getId());
    }
    @Test public void testCreateHobby_with_missing_name() {
        Hobby hobby = new Hobby(null, "we are missing a name");
        assertThrows(MissingInputException.class, () -> {
           hobbyFacade.create(hobby);
        });
    }
    @Test public void testCreateHobby_with_non_unique_name() {
        Hobby hobby = new Hobby(h1.getName(), "We do not have a unique name");
        assertThrows(MissingInputException.class, () ->  {
           hobbyFacade.create(hobby);
        });
    }

}
