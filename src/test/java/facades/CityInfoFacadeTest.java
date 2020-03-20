package facades;
/*
 * author paepke
 * version 1.0
 */

import dto.CityInfoDTO;
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
    private static CityInfoFacade cityInfoFacade;
    private static CityInfo c1, c2;
    private static CityInfoDTO cd1, cd2;

    public CityInfoFacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        entityManagerFactory = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        cityInfoFacade = CityInfoFacade.getCityInfoFacade(entityManagerFactory);
        c1 = new CityInfo(1111, "Frederiksberg");
        c2 = new CityInfo(2222, "Valby");
    }

    @BeforeEach
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("Address.deleteAllRows").executeUpdate();
            entityManager.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            entityManager.persist(c1);
            entityManager.persist(c2);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        cd1 = new CityInfoDTO(c1);
        cd2 = new CityInfoDTO(c2);
    }
    @Test public void testGetAll() {
        List<CityInfoDTO> cityInfoList = cityInfoFacade.getAll();
        assertTrue(cityInfoList.contains(cd1));
        assertTrue(cityInfoList.contains(cd2));
    }
    @Test public void testGetById_with_valid_id() throws CityInfoNotFoundException {
        Integer id = c1.getId();
        CityInfoDTO cityInfo = cityInfoFacade.getById(id);
        assertEquals(cd1, cityInfo);
    }
    @Test public void testGetById_with_invalid_id() {
        Integer id = 1000;
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityInfoFacade.getById(id);
        });
    }
    @Test public void testGetByZipCode_with_valid_zipCode() throws CityInfoNotFoundException {
        Integer zipCode = c1.getZipCode();
        CityInfoDTO cityInfo = cityInfoFacade.getByZipCode(zipCode);
        assertEquals(cd1, cityInfo);
    }
    @Test public void testGetByZipCode_with_invalid_zipcode() {
        Integer zipCode = 1000;
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityInfoFacade.getByZipCode(zipCode);
        });
    }
    @Test public void testGetByCity_with_valid_city() {
        String city = c1.getCity();
        List<CityInfoDTO> cityInfo = cityInfoFacade.getByCity(city);
        assertTrue(cityInfo.contains(cd1));
    }
    @Test public void testGetByCity_with_invalid_city() {
        String city = "This is not a city";
        List<CityInfoDTO> cityInfos = cityInfoFacade.getByCity(city);
        assertEquals(0, cityInfos.size());
    }
    @Test public void testCreateCityInfo_with_valid_input() throws MissingInputException {
        CityInfo c3 = new CityInfo(3333, "Lyngby");
        CityInfoDTO result = cityInfoFacade.create(new CityInfoDTO(c3));
        Integer nextId = Math.max(c1.getId(), c2.getId()) + 1;
        assertEquals(nextId, result.getId());
    }
    @Test public void testCreateCityInfo_with_non_unique_zipCode() {
        CityInfo c3 = new CityInfo(c1.getZipCode(), "We don't have a unique zipcode");
        assertThrows(MissingInputException.class, () -> {
            cityInfoFacade.create(new CityInfoDTO(c3));
        });
    }
    @Test public void testCreateCityInfo_with_invalid_city() {
        CityInfo c3 = new CityInfo(5141,null);
        assertThrows(MissingInputException.class, () -> {
            cityInfoFacade.create(new CityInfoDTO(c3));
        });
    }
    
        @Test public void testUpdateCityInfo() throws CityInfoNotFoundException, MissingInputException{
        String expected = "Søborg";
        cd1.setCity(expected);
        String result = cityInfoFacade.updateCityInfo(cd1).getCity();
        assertEquals(expected, result);
             
    }
    
    @Test public void testUpdateCityInfo_with_missing_input(){
        cd1.setCity(null);
        assertThrows(MissingInputException.class, () -> {
            cityInfoFacade.updateCityInfo(cd1);
        });
    }
    
    @Test public void testUpdateCityInfo_with_invalid_id() {
        cd1.setId(1000);
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityInfoFacade.updateCityInfo(cd1);
        });
        
    }
    
    @Test public void testDeleteCityInfo() throws CityInfoNotFoundException, MissingInputException{
        int expected = cd1.getId();
        CityInfoDTO cd3 = cityInfoFacade.deleteCityInfo(expected);
        assertEquals(cd1, cd3);
    }
    
    @Test public void testDeleteCityInfo_with_invalid_id(){
        int id = 10000;
        assertThrows(CityInfoNotFoundException.class, () -> {
            cityInfoFacade.deleteCityInfo(id);
        });
    }
}
