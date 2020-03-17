package dto;

import entities.Hobby;

import java.util.ArrayList;
import java.util.List;

public class HobbyDTOList {
    private List<HobbyDTO> hobbyList = new ArrayList<>();

    public HobbyDTOList() {}

    public HobbyDTOList(List<Hobby> hobbies) {
        hobbies.forEach(hobby -> {
            hobbyList.add(new HobbyDTO(hobby));
        });
    }

    public List<Hobby> convertToHobbyList() {
        List<Hobby> hobbies = new ArrayList<>();
        this.hobbyList.forEach(hobby -> {
            hobbies.add(new Hobby(hobby));
        });
        return hobbies;
    }

    public List<HobbyDTO> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyDTO> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public void add(HobbyDTO hobbyDTO) {
        hobbyList.add(hobbyDTO);
    }
}
