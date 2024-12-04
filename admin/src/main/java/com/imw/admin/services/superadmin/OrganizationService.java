package com.imw.admin.services.superadmin;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.superadmin.OrganizationAndOwnerResponseDTO;
import com.imw.commonmodule.enums.accessories.DeviceType;
import com.imw.commonmodule.persistence.Accessory;
import com.imw.commonmodule.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    private Logger log = LoggerFactory.getLogger(OrganizationService.class);

    public ResponseDTO getOrganizationsAndOwner( String search, Pageable pageable ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Page<OrganizationAndOwnerResponseDTO> organizations = null;
            if(search != null && !search.isEmpty()){
                organizations =  organizationRepository.findAllWithOwnerByOrganizationName(pageable, search);
            }else{
                organizations =  organizationRepository.findAllWithOwner(pageable);
            }
            responseDTO.setData(organizations);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Organization list found");
        }catch(Exception e){
            log.error("Error searching for Organization list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error searching for Organization list");
        }
        return responseDTO;
    }
}
