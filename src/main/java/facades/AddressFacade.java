package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.Address;
import exception.AddressNotFoundException;
import exception.MissingInputException;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory entityManagerFactory;

    private AddressFacade() {
    }

    /**
     * @param entityManagerFactory
     * @return an instance of this facade class.
     */
    public static AddressFacade getAddressFacade(EntityManagerFactory entityManagerFactory) {
        if (instance == null) {
            AddressFacade.entityManagerFactory = entityManagerFactory;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public List<Address> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Address> addresses = entityManager.createNamedQuery("Address.getAll",Address.class)
                    .getResultList();
            return addresses;
        } finally {
            entityManager.close();
        }
    }

    public List<Address> getByStreet(String street) {
        EntityManager entityManager = getEntityManager();
        try {
            List<Address> addresses = entityManager.createNamedQuery("Address.getByStreet",Address.class)
                    .setParameter("street", street)
                    .getResultList();
            return addresses;
        } finally {
            entityManager.close();
        }
    }

    public Address getById(int id) throws AddressNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Address address = entityManager.find(Address.class, id);
            if(address == null) {
                throw new AddressNotFoundException();
            } else {
                return address;
            }
        } finally {
            entityManager.close();
        }
    }

    public Address create(Address address) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_ADDRESS_MESSAGE);
        } finally {
            em.close();
        }
        return address;
    }

    // TODO(Benjamin): UPDATE Address
    // TODO(Benjamin): DELETE Address
}
