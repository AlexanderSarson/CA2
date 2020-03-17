/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.*;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

/**
 *
 * @author root
 */
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Peter", "Brandt", "email");
        p2 = new Person("Jens", "Jensen", "email2");
        Address ad1 = new Address("street", "more info");
        Address ad2 = new Address("street", "more info");
        CityInfo ci1 = new CityInfo(2880, "bagsvaerd");
        CityInfo ci2 = new CityInfo(2890, "bagsvaerd");
        Hobby h1 = new Hobby("programming", "i just code stuff");
        Hobby h2 = new Hobby("programming2", "i just code stuff");
        Phone ph1 = new Phone("70809050", "home number");
        Phone ph2 = new Phone("708090502", "home number");
        ad1.setCityInfo(ci1);
        ad2.setCityInfo(ci2);
        p1.setAddress(ad1);
        p1.addHobby(h1);
        p1.addPhone(ph1);
        p2.setAddress(ad2);
        p2.addHobby(h2);
        p2.addPhone(ph2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Disabled
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .when()
                .get("/person")
                .then()
                .statusCode(200);
    }

    /**
     * Test of getPersonByPhoneNumber method, of class PersonResource.
     */
    @Test
    public void testGetPerson_with_valid_phoneNumber() {
        String phoneNumber = "70809050";
        String expected = "Peter";
        given()
                .contentType(ContentType.JSON)
                .get("person/phone/" + phoneNumber)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName[0]", equalTo(expected));
    }

    @Test
    public void testGetPerson_with_invalid_phoneNumber() {
        String invalidPhoneNumber = "dfgdfgd";
        given()
                .contentType(ContentType.JSON)
                .get("person/phone/" + invalidPhoneNumber)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("No person found with that phone number"));
    }

    /**
     * Test of getPersonsByHobby method, of class PersonResource.
     */
    @Test
    public void testGetPerson_with_valid_hobby() {
        String hobby = "programming";
        String expected = "Peter";
        given()
                .contentType(ContentType.JSON)
                .get("person/hobby/" + hobby)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName[0]", equalTo(expected));
    }

    @Test
    public void testGetPerson_with_invalid_hobby() {
        String invalidHobby = "this doesn't exist";
        given()
                .contentType(ContentType.JSON)
                .get("person/hobby/" + invalidHobby)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("No persons found with that hobby"));
    }

    @Test
    public void testCreatePerson_with_valid_information() {
        PersonDTO personDTO = new PersonDTO(null,"Testemail", "Oscar", "Laurberg");
        AddressDTO addressDTO = new AddressDTO(null,"street", "more info");
        addressDTO.setCityInfo(new CityInfoDTO(null, 2900, "city"));
        personDTO.setAddress(addressDTO);
        HobbyDTO h1 = new HobbyDTO(null, "football", "i just play");
        PhoneDTO ph1 = new PhoneDTO(null, "50607080", "home number");
        personDTO.addHobby(h1);
        personDTO.addPhone(ph1);
        given()
                .contentType(ContentType.JSON)
                .body(personDTO)
                .when()
                .post("/person")
                .then()
                .body("firstName", equalTo("Oscar"))
                .body("lastName", equalTo("Laurberg"));
    }

    @Test
    public void testCreatePerson_with_invalid_information() {
        PersonDTO personDTO = new PersonDTO(null,"Testemail", null, "Laurberg");
        AddressDTO addressDTO = new AddressDTO(null,"street", "more info");
        addressDTO.setCityInfo(new CityInfoDTO(null, 2900, "city"));
        personDTO.setAddress(addressDTO);
        HobbyDTO h1 = new HobbyDTO(null, "football", "i just play");
        PhoneDTO ph1 = new PhoneDTO(null, "50607080", "home number");
        personDTO.addHobby(h1);
        personDTO.addPhone(ph1);
        given()
                .contentType(ContentType.JSON)
                .body(personDTO)
                .when()
                .post("/person")
                .then()
                .body("message", equalTo("Person not created"));
    }

}
