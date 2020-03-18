package utils;

import entities.Hobby;
import entities.Person;
import entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class PersistenceChecker {
    /**
     * Filters out all Hobbies which are already persisted in the database,
     * @param entityManager The in-use EntityManager, with an active transaction.
     * @param person The person holding the hobbies.
     * @return a filtered list of hobbies, with only hobbies needed to be persisted.
     */
    public static List<Hobby> filterDuplicateHobbies(EntityManager entityManager, Person person) {
        List<Hobby> filteredList = new ArrayList<>();
        List<Hobby> hobbies = person.getHobbies();
        if(hobbies != null && entityManager.getTransaction().isActive()) {
            hobbies.forEach(hobby -> {
                try {
                    Hobby foundHobby = entityManager.createNamedQuery("Hobby.findByName", Hobby.class)
                            .setParameter("name",hobby.getName())
                            .getSingleResult();
                } catch (NoResultException e) {
                    filteredList.add(hobby);
                }
            });
        } else {
            throw new IllegalStateException("Could not check hobbies");
        }
        return filteredList;
    }

    /**
     * Filters out all Phones which are already persisted in the database.
     * @param entityManager The in-use EntityManager, with an active transaction
     * @param person The person holding the phones
     * @return a filtered list of phones, with only phones needed to be added.
     */
    public static List<Phone> filterDuplicatePhones(EntityManager entityManager, Person person) {
        List<Phone> filteredList = new ArrayList<>();
        List<Phone> phones = person.getPhones();
        if(phones != null && entityManager.getTransaction().isActive()) {
            phones.forEach(phone -> {
                try {
                    Phone foundPhone = entityManager.createNamedQuery("Phone.getByNumber", Phone.class)
                            .setParameter("number",phone.getNumber())
                            .getSingleResult();
                } catch (NoResultException e) {
                    filteredList.add(phone);
                }
            });
        } else {
            throw new IllegalStateException("Could not check phones");
        }
        return filteredList;
    }
}
