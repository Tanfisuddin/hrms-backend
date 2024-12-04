package com.imw.admin.services.subscription;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.subscription.AddOns;
import com.imw.commonmodule.persistence.subscription.Order;
import com.imw.commonmodule.persistence.subscription.Plans;
import com.imw.commonmodule.repository.subscription.AddOnsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.AffineTransform;
import java.util.List;

@Service
public class AddOnsService {

    @Autowired
    AddOnsRepository addOnsRepository;

    private Logger log = LoggerFactory.getLogger(PlansService.class);

    public ResponseDTO createAddOn(AddOns addOn) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            AddOns savedAddon = addOnsRepository.save(addOn);
            responseDTO.setData(savedAddon);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Add on created successfully");
        } catch (Exception e) {
            log.error("Error creating the Add on: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating the Add on");
        }
        return responseDTO;
    }

    public ResponseDTO getListOfAddOns() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<AddOns> addOns = addOnsRepository.findAll();
            responseDTO.setData(addOns);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Add ons list retrieved");
        } catch (Exception e) {
            log.error("Error getting Add ons list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error getting Add ons list");
        }
        return responseDTO;
    }

    public ResponseDTO updateAddOnById(Long addOnId, AddOns addOns) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            AddOns addOnsStale = addOnsRepository.findById(addOnId).orElse(null);
            if(addOnsStale == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Add on with id " + addOnId + " doesn't exist.");
                return responseDTO;
            }
            BeanUtils.copyProperties(addOns, addOnsStale);
            addOnsStale.setId(addOnId);
            AddOns updatedAddOn = addOnsRepository.save(addOnsStale);

            responseDTO.setData(updatedAddOn);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Add on with id "+ addOnId + " updated successfully");
        } catch (Exception e) {
            log.error("Error updating Add on: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating Add on with id " + addOnId);
        }
        return responseDTO;
    }
    public ResponseDTO deleteAddOnById(Long addOnId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            addOnsRepository.deleteById(addOnId);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Add on with id "+ addOnId + " deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting Add on: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting Add on with id " + addOnId);
        }
        return responseDTO;
    }
}
