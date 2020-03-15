package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;
import entities.Person;
import exception.MissingInputException;
import exception.PersonNotFoundException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


public class PersonFacade {
    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Gets a Person by their associated email.
     * @param email the email to search for.
     * @return A PersonDTO of the Person found.
     * @throws PersonNotFoundException if no person was found, this will be thrown.
     */
    public PersonDTO getByEmail(String email) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Person person = entityManager.createNamedQuery("Person.getByEmail", Person.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return new PersonDTO(person);
        } catch (NoResultException e) {
            throw new PersonNotFoundException();
        } finally {
            entityManager.close();
        }
    }


    /**
     * Gets a list of all Persons, as PersonDTOs
     * @return The list of PersonDTOs
     */
    public List<PersonDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Person> persons = entityManager.createNamedQuery("Person.getAll",Person.class)
                    .getResultList();
            return toPersonDTOList(persons);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Get a PersonDTO from a given ID
     * @param id the id of the person to find.
     * @return a PersonDTO of the found person
     * @exception PersonNotFoundException if a person was not found, given ID
     */
    public PersonDTO getById(int id) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Person person = entityManager.find(Person.class, id);
            if(person == null) {
                throw new PersonNotFoundException();
            } else {
                return new PersonDTO(person);
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Persists the person.
     * @param person The person to be persisted.
     * @return The persisted person, but with its ID assigned.
     */
    public PersonDTO create(PersonDTO person) throws MissingInputException {
        EntityManager em = emf.createEntityManager();
        Person result = new Person(person);
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PERSON_MESSAGE);
        } finally {
            em.close();
        }
        return new PersonDTO(result);
    }

    public int deletePerson(int id) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            int rows = entityManager.createNamedQuery("Person.deletePerson")
                    .setParameter("id", id)
                    .executeUpdate();
            entityManager.getTransaction().commit();
            if(rows < 1) {
                // Nothing was deleted - handle as an error!
                throw new PersonNotFoundException();
            }
            return rows;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Generates a list of PersonDTOs from a list of Persons
     * @param persons The list to be converted to DTOs
     * @return the list of generated PersonDTOs
     */
    private List<PersonDTO> toPersonDTOList(List<Person> persons) {
        List<PersonDTO> dtos = new ArrayList<>();
        persons.forEach(person -> {
            dtos.add(new PersonDTO(person));
        });
        return dtos;
    }
}
