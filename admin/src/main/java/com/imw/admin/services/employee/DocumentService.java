package com.imw.admin.services.employee;


import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Contact;
import com.imw.commonmodule.persistence.Document;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.DocumentRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(DocumentService.class);

    public ResponseDTO getDocumentsByUserId(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Document> documents = documentRepository.findAllByUserId(userId);
            responseDTO.setData(documents);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Document details found");
        }catch(Exception e){
            log.error("Error finding document details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding document details");
        }
        return responseDTO;
    }

    public ResponseDTO saveDocument(Document document, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Document document1 = documentRepository.findByDocumentTypeAndUserId(document.getDocumentType(), userId);
            Document savedDocument;
            if (document1 != null) {
                document1.setDocumentType(document.getDocumentType());
                document1.setNameOnDocument(document.getNameOnDocument());
                document1.setNumberOnDocument(document.getNumberOnDocument());
                document1.setMediaUrl(document.getMediaUrl());
                savedDocument = documentRepository.save(document1);
            }
            else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                document.setUser(user);
                savedDocument = documentRepository.save(document);
            }
            responseDTO.setData(savedDocument);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Document details saved successfully");
        }catch(Exception e){
            log.error("Error saving document details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving document details");
        }
        return responseDTO;
    }

}
