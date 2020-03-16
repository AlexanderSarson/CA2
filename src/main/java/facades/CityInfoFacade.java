package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.CityInfoDTO;
import entities.CityInfo;
import exception.CityInfoNotFoundException;
import exception.MissingInputException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;


public class CityInfoFacade {

    private static CityInfoFacade instance;
    private static EntityManagerFactory entityManagerFactory;

    private CityInfoFacade() {
    }

    /**
     * @param entityManagerFactory
     * @return an instance of this facade class.
     */
    public static CityInfoFacade getCityInfoFacade(EntityManagerFactory entityManagerFactory) {
        if (instance == null) {
            CityInfoFacade.entityManagerFactory = entityManagerFactory;
            instance = new CityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private List<CityInfoDTO> toCityInfoDTOList(List<CityInfo> cityInfos) {
        List<CityInfoDTO> dtos = new ArrayList<>();
        cityInfos.forEach( cityInfo -> {
            dtos.add(new CityInfoDTO(cityInfo));
        });
        return dtos;
    }

    public List<CityInfoDTO> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<CityInfo> cityInfos = entityManager.createNamedQuery("CityInfo.getAll", CityInfo.class)
                    .getResultList();
            return toCityInfoDTOList(cityInfos);
        } finally {
            entityManager.close();
        }
    }

    public CityInfoDTO getByZipCode(int zipCode) throws CityInfoNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            CityInfo cityInfo = entityManager.createNamedQuery("CityInfo.getByZipCode", CityInfo.class)
                    .setParameter("zipCode", zipCode)
                    .getSingleResult();
            return new CityInfoDTO(cityInfo);
        } catch (NoResultException e) {
            throw new CityInfoNotFoundException();
        } finally {
            entityManager.close();
        }
    }

    public List<CityInfoDTO> getByCity(String city) {
        EntityManager entityManager = getEntityManager();
        try {
            List<CityInfo> cityInfo = entityManager.createNamedQuery("CityInfo.getByCity", CityInfo.class)
                    .setParameter("city", city)
                    .getResultList();
            return toCityInfoDTOList(cityInfo);
        } finally {
            entityManager.close();
        }
    }

    public CityInfoDTO getById(int id) throws CityInfoNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            CityInfo cityInfo = entityManager.find(CityInfo.class, id);
            if(cityInfo == null) {
                throw new CityInfoNotFoundException();
            } else {
                return new CityInfoDTO(cityInfo);
            }
        } finally {
            entityManager.close();
        }
    }

    public CityInfoDTO create(CityInfoDTO cityInfoDTO) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        CityInfo cityInfo = new CityInfo(cityInfoDTO);
        try {
            em.getTransaction().begin();
            em.persist(cityInfo);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_CITYINFO_MESSAGE);
        } finally {
            em.close();
        }
        cityInfoDTO.setId(cityInfo.getId());
        return cityInfoDTO;
    }

        public CityInfoDTO updateCityInfo(CityInfoDTO ctd) throws CityInfoNotFoundException, MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            CityInfo ct = em.find(CityInfo.class, ctd.getId());
            if (ct == null) {
                throw new CityInfoNotFoundException();
            } else {
                em.getTransaction().begin();
                ct.setCity(ctd.getCity());
                ct.setZipCode(ctd.getZipCode());
                em.merge(ct);
                em.getTransaction().commit();
                return ctd;
            }
        } catch (RollbackException e) {
            throw new MissingInputException(MissingInputException.DEFAULT_CITYINFO_MESSAGE);
        } finally {
            em.close();
        }
    }

    public CityInfoDTO deleteCityInfo(int id) throws CityInfoNotFoundException, MissingInputException {
        EntityManager em = getEntityManager();
        try {
            CityInfo cityInfo = em.find(CityInfo.class, id);
            if (cityInfo == null) {
                throw new CityInfoNotFoundException();
            } else {
                em.getTransaction().begin();
                em.remove(cityInfo);
                em.getTransaction().commit();
                return new CityInfoDTO(cityInfo);
            }
        } catch (RollbackException e) {
            //TODO: Create specific exception 
            throw new MissingInputException(MissingInputException.DEFAULT_CITYINFO_MESSAGE);
        } finally {
            em.close();
        }
    }

}
