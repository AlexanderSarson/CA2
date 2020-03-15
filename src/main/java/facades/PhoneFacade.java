package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.Phone;
import exception.MissingInputException;
import exception.PhoneNotFoundException;

import java.util.List;
import javax.persistence.*;


public class PhoneFacade {

    private static PhoneFacade instance;
    private static EntityManagerFactory entityManagerFactory;

    private PhoneFacade() {
    }

    /**
     * @param entityManagerFactory
     * @return an instance of this facade class.
     */
    public static PhoneFacade getPhoneFacade(EntityManagerFactory entityManagerFactory) {
        if (instance == null) {
            PhoneFacade.entityManagerFactory = entityManagerFactory;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public List<Phone> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Phone> phones = entityManager.createNamedQuery("Phone.getAll", Phone.class)
                    .getResultList();
            return phones;
        } finally {
            entityManager.close();
        }
    }

    public Phone getByNumber(String number) throws PhoneNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Phone phone = entityManager.createNamedQuery("Phone.getByNumber", Phone.class)
                    .setParameter("number", number)
                    .getSingleResult();
            return phone;
        } catch (NoResultException e) {
            throw new PhoneNotFoundException();
        } finally {
            entityManager.close();
        }
    }

    public Phone getById(int id) throws PhoneNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Phone phone = entityManager.find(Phone.class, id);
            if(phone == null) {
                throw new PhoneNotFoundException();
            } else {
                return phone;
            }
        } finally {
            entityManager.close();
        }
    }

    public Phone create(Phone phone) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(phone);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PHONE_MESSAGE);
        } finally {
            em.close();
        }
        return phone;
    }

    // TODO(Benjamin): DELETE Phone
    // TODO(Benjamin): UPDATE Phone
}
