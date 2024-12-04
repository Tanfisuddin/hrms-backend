package com.imw.admin.services.employee;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Document;
import com.imw.commonmodule.persistence.Education;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.EducationRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(EducationService.class);

    public ResponseDTO getALlByUserId(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Education> educations = educationRepository.findAllByUserId(userId);
            responseDTO.setData(educations);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Education details found");
        }catch(Exception e){
            log.error("Error finding education details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding education details");
        }
        return responseDTO;
    }

    public ResponseDTO saveEducation(Education education, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Education education1 = educationRepository.findByEducationTypeAndUserId(education.getEducationType(), userId);
            Education savedEducation ;
            if (education1 != null) {
                education1.setEducationType(education.getEducationType());
                education1.setStartDate(education.getStartDate());
                education1.setFinishDate(education.getFinishDate());
                education1.setQualificationArea(education.getQualificationArea());
                education1.setInstituteName(education.getInstituteName());
                education1.setGrade(education.getGrade());
                savedEducation = educationRepository.save(education1);
            }
            else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                education.setUser(user);
                savedEducation = educationRepository.save(education);
            }
            responseDTO.setData(savedEducation);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Education details saved successfully");
        }catch(Exception e){
            log.error("Error saving education details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving education details");
        }
        return responseDTO;
    }
}
