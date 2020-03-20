package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.PersonDTO;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import facades.PersonFacade;
import utils.EMF_Creator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import parsing.ZipCity;
import parsing.ZipCode;
import rest.deserializationsettings.AnnotationExclusionStrategy;

//Todo Remove or change relevant parts before ACTUAL use
@OpenAPIDefinition(
        info = @Info(
                title = "Person API",
                version = "0.1",
                description = "Simple API to get info about persons",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
            @Tag(name = "person", description = "API related to person Info")

        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/CA2"
            ),
            @Server(
                    description = "Server API",
                    url = "https://www.sarson.codes/CA2"
            )

        }
)
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();

    @Operation(summary = "Get information about a person (address, hobbies etc) given a phone number",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested person information"),
                @ApiResponse(responseCode = "404", description = "No person found with that phone number")})
    @Path("/phone/{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonByPhoneNumber(@PathParam("phone") String phone) {
        List<PersonDTO> persons;
        try {
            persons = FACADE.getByPhoneNumber(phone);
            if (persons.isEmpty()) {
                throw new WebApplicationException("No person found with that phone number", 404);
            }
        } catch (PersonNotFoundException p) {
            throw new WebApplicationException("No person found with that phone number", 404);
        }
        return persons;
    }

    @Operation(summary = "Get all persons with a given hobby",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested persons information"),
                @ApiResponse(responseCode = "404", description = "No persons found with that hobby")})
    @Path("/hobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonsByHobby(@PathParam("hobby") String hobby) {
        List<PersonDTO> persons;
        try {
            persons = FACADE.getByHobby(hobby);
            if (persons.isEmpty()) {
                throw new WebApplicationException("No persons found with that hobby", 404);
            }
        } catch (PersonNotFoundException p) {
            throw new WebApplicationException("No persons found with that hobby", 404);
        }
        return persons;
    }

    @Operation(summary = "Get all persons living in a given city (i.e. 2800 Lyngby)",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested persons information"),
                @ApiResponse(responseCode = "404", description = "Persons not found")})
    @Path("/city/{zipCode}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonsByCity(@PathParam("zipCode") int zipCode) throws PersonNotFoundException {
        List<PersonDTO> persons;
        try {
            persons = FACADE.getByZipCode(zipCode);
            if (persons.isEmpty()) {
                throw new WebApplicationException("No persons found with that zipcode!", 404);

            }
        } catch (PersonNotFoundException e) {
            throw new WebApplicationException("No persons found with that zipcode!", 404);
        }
        return persons;
    }

    @Operation(summary = "Get all zipcodes",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested zipcodes"),
                @ApiResponse(responseCode = "404", description = "Zipcodes could not be fetched")})
    @Path("/city/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ZipCity> getAllZipCodes() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("https://dawa.aws.dk/postnumre");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        StringBuilder content;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (Exception e) {
            throw new WebApplicationException("Zipcodes could not be fetched", 404);
        } finally {
            con.disconnect();
        }
        List<ZipCode> zipCodes = GSON.fromJson(content.toString(), new TypeToken<List<ZipCode>>() {
        }.getType());
        return ZipCode.convertToZipCodeListWithCityName(zipCodes);
    }

    @Operation(summary = "Get the count of people with a given hobby",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested persons information"),
                @ApiResponse(responseCode = "404", description = "Persons not found")})
    @Path("/hobby/count/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public long getPersonsCountByHobby(@PathParam("hobby") String hobby
    ) {
        Long count;
        try {
            count = FACADE.getPersonsCountByHobby(hobby);
            if (count < 1) {
                throw new WebApplicationException("No persons found with that hobby", 404);
            }
        } catch (PersonNotFoundException p) {
            throw new WebApplicationException("No persons found with that hobby", 404);
        }

        return count;
    }

    @Operation(summary = "Create a Person (with hobbies, phone, address etc.)",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person is created"),
                @ApiResponse(responseCode = "404", description = "Person not created")})
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO createPersonByDTO(PersonDTO personDTO
    ) {
        if (personDTO.getFirstName() == null || personDTO.getLastName() == null || personDTO.getEmail() == null) {
            throw new WebApplicationException("Person not created", 404);
        }
        PersonDTO dto;
        try {
            dto = FACADE.create(personDTO);
        } catch (MissingInputException e) {
            throw new WebApplicationException("Person not created", 404);
        }
        return dto;
    }

}
