package com.imw.admin.services.employee;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Bank;
import com.imw.commonmodule.persistence.Contact;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.ContactRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(ContactService.class);

    public ResponseDTO  getContactByUserId(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Contact contact =  contactRepository.getByUserId(userId);
            responseDTO.setData(contact);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Contact details with id found");
        }catch(Exception e){
            log.error("Error finding contact details with id: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding contact details with id");
        }
        return responseDTO;
    }

    public ResponseDTO saveContact(Contact contact, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Contact contact1 = contactRepository.getByUserId(userId);
            Contact savedContact;
            if (contact1 != null) {
                contact1.setPhoneNumber1(contact.getPhoneNumber1());
                contact1.setPhoneNumber2(contact.getPhoneNumber2());
                contact1.setEmergencyNumber(contact.getEmergencyNumber());
                contact1.setPersonalEmail(contact.getPersonalEmail());
                savedContact= contactRepository.save(contact1);
            }
            else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                contact.setUser(user);
                savedContact= contactRepository.save(contact);
            }
            responseDTO.setData(savedContact);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Contact details saved successfully");
        }catch(Exception e){
            log.error("Error saving contact details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving contact details");
        }
        return responseDTO;

    }
}
