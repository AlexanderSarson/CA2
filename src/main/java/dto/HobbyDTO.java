package dto;

import entities.Hobby;
import entities.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "HobbyDTO")
public class HobbyDTO {
    @Schema(required = true, example = "1")
    private Integer id;
    @Schema(required = true, example = "Chess")
    private String name;
    @Schema(example = "All we do is play chess")
    private String description;

    private List<Person> persons = new ArrayList<>();

    public HobbyDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public HobbyDTO(Hobby hobby)  {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.description = hobby.getDescription();
        this.persons = hobby.getPersons();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        else {
            HobbyDTO other = (HobbyDTO)obj;
            if(this.id == null || other.getId() == null) return false;
            return other.getId().equals(this.id);
        }
    }
}
