/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class PhoneListDTO {
    private List<PhoneDTO> phoneList = new ArrayList<>();

    public PhoneListDTO() {
    }
    
    public void addPhone(PhoneDTO phone){
        this.phoneList.add(phone);
    }

    public List<Phone> convertToPhoneList() {
        List<Phone> phones = new ArrayList<>();
        this.phoneList.forEach(phoneDTO -> phones.add(new Phone(phoneDTO)));
        return phones;
    }
    public static List<PhoneDTO> convertToHobbyListDTO(List<Phone> phones){
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phones.forEach(phone -> phoneDTOList.add(new PhoneDTO(phone)));
        return phoneDTOList;
    }

    public void setPhoneList(List<PhoneDTO> phoneList) {
        this.phoneList = phoneList;
    }
    
    
}
