package facades;
/*
 * author paepke
 * version 1.0
 */

import entities.CityInfo;
import exception.CityInfoNotFoundException;
import exception.MissingInputException;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CityInfoFacadeTest {

    private static EntityManagerFactory entityManagerFactory;
    private static CityInfoFacade cityinfoFacade;
    private static CityInfo c1, c2;

    public CityInfoFacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        cityinfoFacade = CityInfoFacade.getCityInfoFacade(entityManagerFactory);
        c1 = new CityInfo(1111, "Frederiksberg");
        c2 = new CityInfo(2222, "Valby");
    }

    @BeforeEach
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            entityManager.persist(c1);
            entityManager.persist(c2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }
    @Test public void testGetAll() {
        List<CityInfo> cityInfoList = cityinfoFacade.getAll();
        assertTrue(cityInfoList.contains(c1));
        assertTrue(cityInfoList.contains(c2));
    }
    @Test public void testGetById_with_valid_id() throws CityInfoNotFoundException {
        int id = c1.getId();
        CityInfo cityInfo = cityinfoFacade.getById(id);
        assertEquals(c1, cityInfo);
    }
    @Test public void testGetById_with_invalid_id() {
        int id = 1000;
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityinfoFacade.getById(id);
        });
    }
    @Test public void testGetByZipCode_with_valid_zipCode() throws CityInfoNotFoundException {
        int zipCode = c1.getZipCode();
        CityInfo cityInfo = cityinfoFacade.getByZipCode(zipCode);
        assertEquals(c1, cityInfo);
    }
    @Test public void testGetByZipCode_with_invalid_zipcode() {
        int zipCode = 1000;
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityinfoFacade.getByZipCode(zipCode);
        });
    }
    @Test public void testGetByCity_with_valid_city() {
        String city = c1.getCity();
        List<CityInfo> cityInfo = cityinfoFacade.getByCity(city);
        assertTrue(cityInfo.contains(c1));
    }
    @Test public void testGetByCity_with_invalid_city() {
        String city = "This is not a city";
        List<CityInfo> cityInfos = cityinfoFacade.getByCity(city);
        assertEquals(0, cityInfos.size());
    }
    @Test public void testCreateCityInfo_with_valid_input() throws MissingInputException {
        CityInfo c3 = new CityInfo(3333, "Lyngby");
        cityinfoFacade.create(c3);
        int nextId = Math.max(c1.getId(), c2.getId()) + 1;
        assertEquals(nextId, c3.getId());
    }
    @Test public void testCreateCityInfo_with_non_unique_zipCode() {
        CityInfo c3 = new CityInfo(c1.getZipCode(), "We don't have a unique zipcode");
        assertThrows(MissingInputException.class, () -> {
            cityinfoFacade.create(c3);
        });
    }
    @Test public void testCreateCityInfo_with_invalid_city() {
        CityInfo c3 = new CityInfo(5141,null);
        assertThrows(MissingInputException.class, () -> {
            cityinfoFacade.create(c3);
        });
    }
}
