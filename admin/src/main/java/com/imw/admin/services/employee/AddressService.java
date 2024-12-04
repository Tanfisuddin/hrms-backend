package com.imw.admin.services.employee;


import com.imw.admin.services.DepartmentService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Address;
import com.imw.commonmodule.persistence.Department;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.AddressRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(AddressService.class);


    public ResponseDTO getAddressesByUserId(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Address> address = addressRepository.findByUserId(userId);
            if(address.isPresent()) {
                responseDTO.setData(address.get());
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Address with id found");
            }else{
                responseDTO.setData(new HashMap<>());
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Address with id not found");
            }

        }catch(Exception e){
            log.error("Error finding address with id: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding address with id");
        }
        return responseDTO;
    }

    public ResponseDTO saveAddress(Address address, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Address> address1 = addressRepository.findByUserId(userId);
            Address savedAddress ;
            if (address1.isPresent()) {
                Address updateAddress = address1.get();

                if( address.getCommunicationAddress1()!=null ){updateAddress.setCommunicationAddress1(address.getCommunicationAddress1());}
                if( address.getCommunicationAddress2()!=null ){updateAddress.setCommunicationAddress2(address.getCommunicationAddress2());}
                if( address.getCommunicationAddress3()!=null ){updateAddress.setCommunicationAddress3(address.getCommunicationAddress3());}
                if( address.getCommunicationCity()!=null ){updateAddress.setCommunicationCity(address.getCommunicationCity());}
                if( address.getCommunicationState()!=null ){updateAddress.setCommunicationState(address.getCommunicationState());}
                if( address.getCommunicationArea()!=null ){updateAddress.setCommunicationArea(address.getCommunicationArea());}
                if( address.getCommunicationPincode()!=null ){updateAddress.setCommunicationPincode(address.getCommunicationPincode());}
                if( address.getCommunicationCountry()!=null ){updateAddress.setCommunicationCountry(address.getCommunicationCountry());}
                if( address.getCommunicationLandmark()!=null ){updateAddress.setCommunicationLandmark(address.getCommunicationLandmark());}

                if( address.getPermanentAddress1()!=null ){updateAddress.setPermanentAddress1(address.getPermanentAddress1());}
                if( address.getPermanentAddress2()!=null ){updateAddress.setPermanentAddress2(address.getPermanentAddress2());}
                if( address.getPermanentAddress3()!=null ){updateAddress.setPermanentAddress3(address.getPermanentAddress3());}
                if( address.getPermanentCity()!=null ){updateAddress.setPermanentCity(address.getPermanentCity());}
                if( address.getPermanentState()!=null ){updateAddress.setPermanentState(address.getPermanentState());}
                if( address.getPermanentArea()!=null ){updateAddress.setPermanentArea(address.getPermanentArea());}
                if( address.getPermanentPincode()!=null ){updateAddress.setPermanentPincode(address.getPermanentPincode());}
                if( address.getPermanentCountry()!=null ){updateAddress.setPermanentCountry(address.getPermanentCountry());}
                if( address.getPermanentLandmark()!=null ){updateAddress.setPermanentLandmark(address.getPermanentLandmark());}

                if( address.getPermanentSameAsCommunication()!=null ){updateAddress.setPermanentSameAsCommunication(address.getPermanentSameAsCommunication());}

                savedAddress =  addressRepository.save(updateAddress);
            }
            else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                address.setUser(user);
                savedAddress =  addressRepository.save(address);
            }
            responseDTO.setData(savedAddress);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Address saved successfully");
        }catch(Exception e){
            log.error("Error saving the address: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving the address");
        }
        return responseDTO;
    }
}
