/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Address;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class AddressListDTO {
    private List<AddressDTO> AddressList = new ArrayList<>();

    public AddressListDTO() {
    }
    
    public void addAddress(AddressDTO address){
        this.AddressList.add(address);
    }

    public List<AddressDTO> getAddressList() {
        return AddressList;
    }
    
    public List<Address> convertAddressList(){
        List<Address> Addresses = new ArrayList<>();
        AddressList.forEach(AddressDTO -> Addresses.add(new Address(AddressDTO)));
        return Addresses;
    }
}
