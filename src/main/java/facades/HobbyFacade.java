package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.Hobby;
import exception.HobbyNotFoundException;
import exception.MissingInputException;

import java.util.List;
import javax.persistence.*;


public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    private HobbyFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Hobby> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Hobby> hobbies = entityManager.createNamedQuery("Hobby.getAll", Hobby.class)
                    .getResultList();
            return hobbies;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets a list of Hobbies corresponding to the specified name.
     * @param name the name of the hobbies to search for.
     * @return a list of hobbies corresponding to the given name.
     */
    public Hobby getByName(String name) throws HobbyNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Hobby hobby = entityManager.createNamedQuery("Hobby.findByName", Hobby.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return hobby;
        } catch (NoResultException e) {
            throw new HobbyNotFoundException();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets a Hobby given an ID
     * @param id the id of the Hobby to find.
     * @return The found hobby.
     * @exception HobbyNotFoundException if no hobby was found corresponding the given ID.
     */
    public Hobby getById(int id) throws HobbyNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Hobby hobby = entityManager.find(Hobby.class, id);
            if(hobby == null) {
                throw new HobbyNotFoundException();
            } else {
                return hobby;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Persists a Hobby
     * @param hobby the hobby to be persisted.
     * @return The hobby object with a auto-generated ID
     */
    public Hobby create(Hobby hobby) throws MissingInputException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_HOBBY_MESSAGE);
        } finally {
            em.close();
        }
        return hobby;
    }

    // TODO(Benjamin): Delete Hobby
}
