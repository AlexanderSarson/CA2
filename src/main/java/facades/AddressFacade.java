package facades;

/*
 * author paepke
 * version 1.0
 */
import dto.AddressDTO;
import entities.Address;
import exception.AddressNotFoundException;
import exception.MissingInputException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

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

    private List<AddressDTO> toAddressDTOList(List<Address> addresses) {
        List<AddressDTO> dtos = new ArrayList<>();
        addresses.forEach(address -> {
            dtos.add(new AddressDTO(address));
        });
        return dtos;
    }

    public List<AddressDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<Address> addresses = entityManager.createNamedQuery("Address.getAll", Address.class)
                    .getResultList();
            return toAddressDTOList(addresses);
        } finally {
            entityManager.close();
        }
    }

    public List<AddressDTO> getByStreet(String street) {
        EntityManager entityManager = getEntityManager();
        try {
            List<Address> addresses = entityManager.createNamedQuery("Address.getByStreet", Address.class)
                    .setParameter("street", street)
                    .getResultList();
            return toAddressDTOList(addresses);
        } finally {
            entityManager.close();
        }
    }

    public AddressDTO getById(int id) throws AddressNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            Address address = entityManager.find(Address.class, id);
            if (address == null) {
                throw new AddressNotFoundException();
            } else {
                return new AddressDTO(address);
            }
        } finally {
            entityManager.close();
        }
    }

    public AddressDTO create(AddressDTO addressDTO) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Address address = new Address(addressDTO);
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_ADDRESS_MESSAGE);
        } finally {
            em.close();
        }
        addressDTO.setId(address.getId());
        return addressDTO;
    }

    public AddressDTO updateAddress(AddressDTO addressDTO) throws AddressNotFoundException, MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Address address = em.find(Address.class, addressDTO.getId());
            if (address == null) {
                throw new AddressNotFoundException();
            } else {
                em.getTransaction().begin();
                address.setStreet(addressDTO.getStreet());
                address.setAdditionalInfoInfo(addressDTO.getAdditionalInfo());
                address.setCityInfo(addressDTO.getCityInfo());
                em.merge(address);
                em.getTransaction().commit();
                return addressDTO;
            }
        } catch (RollbackException e) {
            throw new MissingInputException(MissingInputException.DEFAULT_ADDRESS_MESSAGE);
        } finally {
            em.close();
        }
    }

    public AddressDTO deleteAddress(int id) throws AddressNotFoundException, MissingInputException {
        EntityManager em = getEntityManager();
        try {
            Address address = em.find(Address.class, id);
            if (address == null) {
                throw new AddressNotFoundException();
            } else {
                em.getTransaction().begin();
                em.remove(address);
                em.getTransaction().commit();
                return new AddressDTO(address);
            }
        } catch (RollbackException e) {
            //TODO: Create specific exception 
            throw new MissingInputException(MissingInputException.DEFAULT_ADDRESS_MESSAGE);
        } finally {
            em.close();
        }
    }

}
