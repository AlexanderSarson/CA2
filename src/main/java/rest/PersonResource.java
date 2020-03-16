package rest;

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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
        
    @Operation(summary = "Get all Persons from database",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested list of Persons"),
                @ApiResponse(responseCode = "404", description = "Persons not found")})
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PersonDTO> getAllPersonsFromDatabase() {
        List<PersonDTO> list = new ArrayList<>();
        list.add(new PersonDTO(1, "email@example.com", "Jens", "Sorensen"));
        list.add(new PersonDTO(2, "email2@example.com", "Frederik", "Sorensen"));
        return list;
    }
    
    
    @Operation(summary = "Get info about a person from phone number",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested list of a Person"),
                @ApiResponse(responseCode = "404", description = "Person not found")})
    @GET
    @Path("{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonInfoByPhoneNumber(@PathParam("phone") String phone){
        List<PersonDTO> list = new ArrayList<>();
        list.add(new PersonDTO(1, "email@example.com", "Jens", "Sorensen"));
        return list;
    }
            
 
}
