package utils;

import entities.Hobby;
import entities.Person;
import entities.Phone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceCheckerTest {

    private static EntityManagerFactory entityManagerFactory;
    private static Hobby h1;
    private static Phone p1;

    @BeforeAll
    public static void classSetup() {
        entityManagerFactory = EMF_Creator
                .createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        h1 = new Hobby("Chess", "Chess");
        p1 = new Phone("11111111", "Work");
    }

    @BeforeEach
    public void setup() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
        entityManager.createNamedQuery("Phone.deleteAllRows").executeUpdate();
        entityManager.persist(h1);
        entityManager.persist(p1);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void filterDuplicateHobbies_with_no_duplicates() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail","firstName","lastName");
        person.addHobby(new Hobby("Boxing", "Boxing"));

        List<Hobby> result = PersistenceChecker.filterDuplicateHobbies(entityManager, person);
        entityManager.close();

        assertEquals(person.getHobbies().size(), result.size());
    }
    @Test
    public void filterDuplicateHobbies_with_one_duplicate() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail","firstName","lastName");
        person.addHobby(new Hobby("Chess", "Chess"));
        int expected = person.getHobbies().size() - 1;

        List<Hobby> result = PersistenceChecker.filterDuplicateHobbies(entityManager, person);
        entityManager.close();

        assertEquals(expected, result.size());
    }
    @Test
    public void filterDuplicateHobbies_with_no_hobbies() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("email","firstName","lastName");
        List<Hobby> result = PersistenceChecker.filterDuplicateHobbies(entityManager,person);
        entityManager.close();
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterDuplicateHobbies_with_illegal_state() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail", "firstName", "lastName");
        person.setHobbies(null);
        assertThrows(IllegalStateException.class, () -> {
            PersistenceChecker.filterDuplicateHobbies(entityManager, person);
        });
    }


    @Test
    void filterDuplicatePhones_with_no_duplicates() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail","firstName","lastName");
        person.addPhone(new Phone("22222222","Private"));
        List<Phone> result = PersistenceChecker.filterDuplicatePhones(entityManager, person);

        entityManager.close();

        assertEquals(person.getPhones().size(), result.size());
    }

    @Test
    void filterDuplicatePhones_with_one_duplicate() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail","firstName","lastName");
        person.addPhone(new Phone("11111111","Private"));
        int expected = person.getPhones().size() - 1;
        List<Phone> result = PersistenceChecker.filterDuplicatePhones(entityManager, person);
        entityManager.close();

        assertEquals(expected, result.size());
    }

    @Test
    public void filterDuplicateHobbies_with_no_phones() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("email","firstName","lastName");
        List<Phone> result = PersistenceChecker.filterDuplicatePhones(entityManager,person);
        entityManager.close();
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterDuplicateHobbies_with_illegal_state_phone() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person("mail", "firstName", "lastName");
        person.setPhones(null);
        assertThrows(IllegalStateException.class, () -> {
            PersistenceChecker.filterDuplicatePhones(entityManager, person);
        });
    }
}