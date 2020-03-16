package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        
    @Operation(summary = "Get information about a person (address, hobbies etc) given a phone number",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested person information"),
                @ApiResponse(responseCode = "404", description = "Person not found")})
    @Path("/phone/{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonByPhoneNumber(@PathParam("phone") String phone) {
        List<PersonDTO> list = new ArrayList<>();
        list.add(new PersonDTO(1, "email@example.com", "Jens", "Sorensen"));
        return list;
    }
    
    @Operation(summary = "Get all persons with a given hobby",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested persons information"),
                @ApiResponse(responseCode = "404", description = "Persons not found")})
    @Path("/hobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonsByHobby(@PathParam("hobby") String hobby) {
        List<PersonDTO> list = new ArrayList<>();
        list.add(new PersonDTO(1, "email@example.com", "Jens", "Sorensen"));
        list.add(new PersonDTO(2, "email@example.com", "Frede", "Ferie"));
        return list;
    }
    
    @Operation(summary = "Get all persons living in a given city (i.e. 2800 Lyngby)",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested persons information"),
                @ApiResponse(responseCode = "404", description = "Persons not found")})
    @Path("/city/{city}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonsByCity(@PathParam("city") String city) {
        List<PersonDTO> list = new ArrayList<>();
        list.add(new PersonDTO(1, "email@example.com", "Jens", "Sorensen"));
        list.add(new PersonDTO(2, "email@example.com", "Frede", "Ferie"));
        list.add(new PersonDTO(3, "email@example.com", "Frede", "Ferie"));
        return list;
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
    public Integer getPersonsCountByHobby(@PathParam("hobby") String hobby) {
        return 5;
    }
    
    @Operation(summary = "Create a Person (with hobbies, phone, address etc.)",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person created"),
                @ApiResponse(responseCode = "404", description = "Person not created")})
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO getPersonsCountByHobby(PersonDTO personDTO) {
        return personDTO;
    }
    
 
}
