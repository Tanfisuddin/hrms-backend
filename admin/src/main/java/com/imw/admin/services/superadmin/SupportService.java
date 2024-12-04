package com.imw.admin.services.superadmin;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.superadmin.OrganizationAndOwnerResponseDTO;
import com.imw.commonmodule.persistence.SupportContact;
import com.imw.commonmodule.repository.SupportContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SupportService {

    @Autowired
    SupportContactRepository supportContactRepository;

    private Logger log = LoggerFactory.getLogger(OrganizationService.class);

    public ResponseDTO getSupportList(Pageable pageable ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Page<SupportContact> messageList = supportContactRepository.findAllOrderByIdDesc(pageable);
            responseDTO.setData(messageList);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Message list");
        }catch(Exception e){
            log.error("Error searching for Support Message list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error searching for Support Message list");
        }
        return responseDTO;
    }

    public ResponseDTO createSupportMessage(SupportContact supportContact ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Date  currentDate = new Date();
            supportContact.setCreatedAt(currentDate);
            SupportContact savedMessage = supportContactRepository.save(supportContact);
            responseDTO.setData(savedMessage);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Support ticket created");
        }catch(Exception e){
            log.error("Error creating support ticket: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating support ticket");
        }
        return responseDTO;
    }

    public ResponseDTO deleteSupportTicketById(Long id ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(supportContactRepository.existsById(id)){
                supportContactRepository.deleteById(id);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Support Message with id "+id+ " deleted successfully.");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Support Message with id "+id+ " not found.");
            }

        }catch(Exception e){
            log.error("Error deleting for Support Message: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting for Support Message");
        }
        return responseDTO;
    }

    public ResponseDTO deleteSupportTicketsByIds(List<Long> ids ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            int deleteCount =  supportContactRepository.deleteByIds(ids);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Deleted "+deleteCount+ " Support Messages successfully.");

        }catch(Exception e){
            log.error("Error deleting for Support Messages: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting for Support Messages");
        }
        return responseDTO;
    }
}
