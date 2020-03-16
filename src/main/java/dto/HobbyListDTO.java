/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class HobbyListDTO {
    private List<HobbyDTO> hobbyList = new ArrayList<>();

    public HobbyListDTO() {
    }
    
    public void addHobby(HobbyDTO hobby){
        this.hobbyList.add(hobby);
    }

    public List<HobbyDTO> getHobbyList() {
        return hobbyList;
    }
    
    public List<Hobby> convertHobbyList(){
        List<Hobby> hobbies = new ArrayList<>();
        hobbyList.forEach(hobbyDTO -> hobbies.add(new Hobby(hobbyDTO)));
        return hobbies;
    }
    
    public static List<HobbyDTO> convertToHobbyListDTO(List<Hobby> hobbies){
        List<HobbyDTO> hobbyDTOList = new ArrayList<>();
        hobbies.forEach(hobby -> hobbyDTOList.add(new HobbyDTO(hobby)));
        return hobbyDTOList;
    }

    public void setHobbyList(List<HobbyDTO> hobbyList) {
        this.hobbyList = hobbyList;
    }
    
    
}
