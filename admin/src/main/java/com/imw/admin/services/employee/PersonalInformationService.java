package com.imw.admin.services.employee;


import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Education;
import com.imw.commonmodule.persistence.PersonalInformation;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.PersonalInformationRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalInformationService {

    @Autowired
    private PersonalInformationRepository personalInformationRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(PersonalInformationService.class);

    public ResponseDTO getPersonalInformation(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            PersonalInformation personalInformation = personalInformationRepository.findByUserId(userId);
            responseDTO.setData(personalInformation);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Personal information found");
        }catch(Exception e){
            log.error("Error finding personal information: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding personal information");
        }
        return responseDTO;
    }

    public ResponseDTO setPersonalInformation(PersonalInformation personalInformation, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            PersonalInformation userPersonalInformation = personalInformationRepository.findByUserId(userId);
            PersonalInformation savedPersonalInformation ;
            if(userPersonalInformation != null){
                userPersonalInformation.setBirthDate(personalInformation.getBirthDate());
                userPersonalInformation.setNationality(personalInformation.getNationality());
                userPersonalInformation.setBloodGroup(personalInformation.getBloodGroup());
                userPersonalInformation.setMaritalStatus(personalInformation.getMaritalStatus());
                userPersonalInformation.setSpouseName(personalInformation.getSpouseName());
                userPersonalInformation.setFatherName(personalInformation.getFatherName());

                savedPersonalInformation = personalInformationRepository.save(userPersonalInformation);
            }else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                personalInformation.setUser(user);
                savedPersonalInformation = personalInformationRepository.save(personalInformation);
            }
            responseDTO.setData(savedPersonalInformation);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Personal information saved successfully");
        }catch(Exception e){
            log.error("Error saving personal information: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving personal information");
        }
        return responseDTO;
    }
}
