package dto;

import entities.Hobby;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Hobby")
public class HobbyDTO {
    @Hidden
    private Integer id;
    @Schema(required = true, example = "Chess")
    private String name;
    @Schema(example = "All we do is play chess")
    private String description;

    public HobbyDTO() {
    }

    public HobbyDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public HobbyDTO(Hobby hobby)  {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.description = hobby.getDescription();
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

    public static List<HobbyDTO> convertToHobbyDTO(List<Hobby> hobbies){
        List<HobbyDTO> hobbyDTOList = new ArrayList<>();
        for(Hobby hobby: hobbies) {
            hobbyDTOList.add(new HobbyDTO(hobby));
        }
        return hobbyDTOList;
    }
    
    public static List<Hobby> convertToHobby(List<HobbyDTO> hobbiesDTO){
        List<Hobby> hobbyList = new ArrayList<>();
        hobbiesDTO.forEach(hobbyDTO -> hobbyList.add(new Hobby(hobbyDTO)));
        return hobbyList;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
