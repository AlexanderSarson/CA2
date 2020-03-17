package dto;

import entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneDTOList {
    private List<PhoneDTO> phoneList = new ArrayList<>();

    public PhoneDTOList() {}

    public PhoneDTOList(List<Phone> phones) {
        phones.forEach(phone -> {
            phoneList.add(new PhoneDTO(phone));
        });
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
