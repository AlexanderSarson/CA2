package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.PhoneDTO;
import entities.Phone;
import exception.MissingInputException;
import exception.PhoneNotFoundException;

import java.util.ArrayList;
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

    public List<PhoneDTO> toPhoneDTOList(List<Phone> phones) {
        List<PhoneDTO> dtos = new ArrayList<>();
        phones.forEach(phone -> {dtos.add(new PhoneDTO(phone));});
        return dtos;
    }

    public List<PhoneDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Phone> phones = entityManager.createNamedQuery("Phone.getAll", Phone.class)
                    .getResultList();
            return toPhoneDTOList(phones);
        } finally {
            entityManager.close();
        }
    }

    public PhoneDTO getByNumber(String number) throws PhoneNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Phone phone = entityManager.createNamedQuery("Phone.getByNumber", Phone.class)
                    .setParameter("number", number)
                    .getSingleResult();
            return new PhoneDTO(phone);
        } catch (NoResultException e) {
            throw new PhoneNotFoundException();
        } finally {
            entityManager.close();
        }
    }

    public PhoneDTO getById(int id) throws PhoneNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Phone phone = entityManager.find(Phone.class, id);
            if(phone == null) {
                throw new PhoneNotFoundException();
            } else {
                return new PhoneDTO(phone);
            }
        } finally {
            entityManager.close();
        }
    }

    public PhoneDTO create(PhoneDTO phoneDTO) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Phone phone = new Phone(phoneDTO);
        try {
            em.getTransaction().begin();
            em.persist(phone);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_PHONE_MESSAGE);
        } finally {
            em.close();
        }
        phoneDTO.setId(phone.getId());
        return phoneDTO;
    }

    // TODO(Benjamin): DELETE Phone
    // TODO(Benjamin): UPDATE Phone
}
