package dto;

import entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneDTOList {
    private List<PhoneDTO> phoneList = new ArrayList<>();

    public PhoneDTOList() {}

    public PhoneDTOList(List<Phone> phones) {
        List<PhoneDTO> dtos = new ArrayList<>();
        for(Phone phone: phones) {
            dtos.add(new PhoneDTO(phone.getId(),phone.getNumber(),phone.getDescription()));
        }
        this.phoneList = dtos;
    }

    public List<Phone> convertToPhoneList() {
        List<Phone> phones = new ArrayList<>();
        phoneList.forEach(phone -> {
            phones.add(new Phone(phone));
        });
        return phones;
    }

    public List<PhoneDTO> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<PhoneDTO> phoneList) {
        this.phoneList = phoneList;
    }

    public void add(PhoneDTO phoneDTO) {
        this.phoneList.add(phoneDTO);
    }
}
