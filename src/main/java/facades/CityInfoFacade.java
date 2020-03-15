package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.CityInfo;
import exception.CityInfoNotFoundException;
import exception.MissingInputException;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;


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

    public List<CityInfo> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            List<CityInfo> cityInfos = entityManager.createNamedQuery("CityInfo.getAll", CityInfo.class)
                    .getResultList();
            return cityInfos;
        } finally {
            entityManager.close();
        }
    }

    public CityInfo getByZipCode(int zipCode) throws CityInfoNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            CityInfo cityInfo = entityManager.createNamedQuery("CityInfo.getByZipCode", CityInfo.class)
                    .setParameter("zipCode", zipCode)
                    .getSingleResult();
            return cityInfo;
        } catch (NoResultException e) {
            throw new CityInfoNotFoundException();
        } finally {
            entityManager.close();
        }
    }

    public List<CityInfo> getByCity(String city) {
        EntityManager entityManager = getEntityManager();
        try {
            List<CityInfo> cityInfo = entityManager.createNamedQuery("CityInfo.getByCity", CityInfo.class)
                    .setParameter("city", city)
                    .getResultList();
            return cityInfo;
        } finally {
            entityManager.close();
        }
    }

    public CityInfo getById(int id) throws CityInfoNotFoundException {
        EntityManager entityManager = getEntityManager();
        try {
            CityInfo cityInfo = entityManager.find(CityInfo.class, id);
            if(cityInfo == null) {
                throw new CityInfoNotFoundException();
            } else {
                return cityInfo;
            }
        } finally {
            entityManager.close();
        }
    }

    public CityInfo create(CityInfo cityinfo) throws MissingInputException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cityinfo);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new MissingInputException(MissingInputException.DEFAULT_CITYINFO_MESSAGE);
        } finally {
            em.close();
        }
        return cityinfo;
    }

    // TODO(Benjamin): DELETE CityInfo
    // TODO(Benjamin): UPDATE CityInfo
}
