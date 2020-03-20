package facades;

/*
 * author paepke
 * version 1.0
 */

import dto.PersonDTO;
import entities.*;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import utils.PersistenceChecker;

import java.util.ArrayList;
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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Person person = new Person(personDTO);
        try {
            entityManager.getTransaction().begin();
            preCreateCheck(entityManager,person);
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PERSON_MESSAGE);
        } finally {
            entityManager.close();
        }
        return new PersonDTO(person);
    }

    private void preCreateCheck(EntityManager entityManager, Person person) {
        List<Hobby> completeHobbyList = new ArrayList<>();
        person.getHobbies().forEach(hobby -> {
            try {
                Hobby found = entityManager.createNamedQuery("Hobby.findByName", Hobby.class)
                        .setParameter("name",hobby.getName())
                        .getSingleResult();
                completeHobbyList.add(found);
            } catch(NoResultException e) {
                entityManager.persist(hobby);
                completeHobbyList.add(hobby);
            }
        });
        person.setHobbies(completeHobbyList);

        List<Phone> completePhoneList = new ArrayList<>();
        person.getPhones().forEach(phone -> {
           try {
               Phone found = entityManager.createNamedQuery("Phone.getByNumber", Phone.class)
                       .setParameter("number", phone.getNumber())
                       .getSingleResult();
               completePhoneList.add(found);
           } catch (NoResultException e) {
               entityManager.persist(phone);
               completePhoneList.add(phone);
           }
        });
        person.setPhones(completePhoneList);

        // Check the CityInfo first
        CityInfo cityInfo = person.getAddress().getCityInfo();
        CityInfo foundCityInfo = null;
        try {
            foundCityInfo = entityManager.createNamedQuery("CityInfo.getByZipCode", CityInfo.class)
                    .setParameter("zipCode",cityInfo.getZipCode())
                    .getSingleResult();
        } catch (NoResultException e) {
            entityManager.persist(cityInfo);
            foundCityInfo = cityInfo;
        }
        entityManager.getTransaction().commit();
        Address address = person.getAddress();
        address.setCityInfo(null);
        entityManager.getTransaction().begin();
        Address foundAddress = null;
        try {
            foundAddress = entityManager.createNamedQuery("Address.getByStreetAndCityInfoId", Address.class)
                    .setParameter("street",address.getStreet())
                    .setParameter("id",foundCityInfo.getId())
                    .getSingleResult();
        } catch (NoResultException e) {
            entityManager.persist(address);
            foundAddress = address;
        }
        person.setAddress(foundAddress);
        person.getAddress().setCityInfo(foundCityInfo);
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
