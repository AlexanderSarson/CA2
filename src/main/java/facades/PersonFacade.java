package facades;

/*
 * author paepke
 * version 1.0
 */

import dto.HobbyDTOList;
import dto.PersonDTO;
import dto.PhoneDTOList;
import entities.Person;
import entities.Phone;
import exception.MissingInputException;
import exception.PersonNotFoundException;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import javax.persistence.*;

public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory entityManagerFactory;

    public PersonFacade() {
    }

    /**
     * @param entityManagerFactory
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory entityManagerFactory) {
        if (instance == null) {
            PersonFacade.entityManagerFactory = entityManagerFactory;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Gets a Person by their associated email.
     *
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
     *
     * @return The list of PersonDTOs
     */
    public List<PersonDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Person> persons = entityManager.createNamedQuery("Person.getAll", Person.class)
                    .getResultList();
            return toPersonDTOList(persons);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Get a PersonDTO from a given ID
     *
     * @param id the id of the person to find.
     * @return a PersonDTO of the found person
     * @exception PersonNotFoundException if a person was not found, given ID
     */
    public PersonDTO getById(int id) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Person person = entityManager.find(Person.class, id);
            if (person == null) {
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
     *
     * @param personDTO The person to be persisted.
     * @return The persisted person, but with its ID assigned.
     */
    public PersonDTO create(PersonDTO personDTO) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Person person = new Person(personDTO);
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PERSON_MESSAGE);
        } finally {
            em.close();
        }
        personDTO.setId(person.getId());
        return personDTO;
    }

    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Person person = entityManager.find(Person.class, id);
            if(person ==  null) {
                throw new PersonNotFoundException();
            } else {
                entityManager.getTransaction().begin();
                entityManager.remove(person);
                entityManager.getTransaction().commit();
            }
            return new PersonDTO(person);
        } finally {
            entityManager.close();
        }
    }

    public PersonDTO updatePerson(PersonDTO personDTO) throws PersonNotFoundException, MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Person person = em.find(Person.class, personDTO.getId());
            if (person == null) {
                throw new PersonNotFoundException();
            } else {
                em.getTransaction().begin();
                person.setEmail(personDTO.getEmail());
                person.setFirstName(personDTO.getFirstName());
                person.setLastName(personDTO.getLastName());
                em.merge(person);
                em.getTransaction().commit();
                return personDTO;
            }
        } catch (RollbackException e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PERSON_MESSAGE);
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> getByPhoneNumber(String number) throws PersonNotFoundException, IllegalArgumentException {
        if(!Phone.isValidDanishNumber(number)) {
            throw new PersonNotFoundException();
        }
        EntityManager em = getEntityManager();
        try {
            List<Person> persons = em.createNamedQuery("Person.getByPhoneNumber", Person.class)
                    .setParameter("number", number)
                    .getResultList();
            return toPersonDTOList(persons);
        } catch (NoResultException e) {
            throw new PersonNotFoundException();
        } finally {
            em.close();
        }
    }
    
    public List<PersonDTO> getByHobby(String hobby) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            List<Person> persons = em.createNamedQuery("Person.getByHobby", Person.class)
                    .setParameter("hobby", hobby)
                    .getResultList();
            return toPersonDTOList(persons);
        } catch (NoResultException e) {
            throw new PersonNotFoundException();
        } finally {
            em.close();
        }
    }
    
    public long getPersonsCountByHobby (String hobby) throws PersonNotFoundException{
        EntityManager em = getEntityManager();
        try{
            long personCount = (long) em.createNamedQuery("Person.getByHobbyCount")
                    .setParameter("hobby", hobby)
                    .getSingleResult();
            return personCount;
        } catch (NoResultException e){
            throw new PersonNotFoundException();
        }finally{
            em.close();
        }
    }

    public List<PersonDTO> getByZipCode(int zipCode) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try{
            List<Person> persons = em.createNamedQuery("Person.getByZipCode", Person.class)
                    .setParameter("zipCode", zipCode)
                    .getResultList();
            return toPersonDTOList(persons);
        } catch (NoResultException e){
            throw new PersonNotFoundException();
        }finally{
            em.close();
        }
    }
    
    /**
     * Generates a list of PersonDTOs from a list of Persons
     *
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
