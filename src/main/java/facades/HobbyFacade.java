package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.HobbyDTO;
import entities.Hobby;
import exception.HobbyNotFoundException;
import exception.MissingInputException;

import java.util.ArrayList;
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

    private List<HobbyDTO> toHobbyDTOList(List<Hobby> hobbies) {
        List<HobbyDTO> dtos = new ArrayList<>();
        hobbies.forEach(hobby -> {
            dtos.add(new HobbyDTO(hobby));
        });
        return dtos;
    }

    public List<HobbyDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Hobby> hobbies = entityManager.createNamedQuery("Hobby.getAll", Hobby.class)
                    .getResultList();
            return toHobbyDTOList(hobbies);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets a list of Hobbies corresponding to the specified name.
     * @param name the name of the hobbies to search for.
     * @return a list of hobbies corresponding to the given name.
     */
    public HobbyDTO getByName(String name) throws HobbyNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Hobby hobby = entityManager.createNamedQuery("Hobby.findByName", Hobby.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return new HobbyDTO(hobby);
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
    public HobbyDTO getById(int id) throws HobbyNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Hobby hobby = entityManager.find(Hobby.class, id);
            if(hobby == null) {
                throw new HobbyNotFoundException();
            } else {
                return new HobbyDTO(hobby);
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Persists a Hobby
     * @param hobbyDTO the hobby to be persisted.
     * @return The hobby object with a auto-generated ID
     */
    public HobbyDTO create(HobbyDTO hobbyDTO) throws MissingInputException {
        EntityManager em = emf.createEntityManager();
        Hobby hobby = new Hobby(hobbyDTO);
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_HOBBY_MESSAGE);
        } finally {
            em.close();
        }
        hobbyDTO.setId(hobby.getId());
        return hobbyDTO;
    }

    // TODO(Benjamin): DELETE Hobby
    // TODO(Benjamin): UPDATE Hobby
}
