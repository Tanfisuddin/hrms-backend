package com.imw.admin.services.subscription;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.subscription.Order;
import com.imw.commonmodule.persistence.subscription.Plans;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.subscription.PlansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlansService {

    @Autowired
    PlansRepository plansRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    private Logger log = LoggerFactory.getLogger(PlansService.class);

    public ResponseDTO createPlan(Plans plan, Long organizationId, Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(organizationId != null){
                Organization currentOrganization =  organizationRepository.findById(organizationId).orElse(null);
                if(currentOrganization == null){
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Organization with id " + organizationId + " doesn't exist");
                    return responseDTO;
                }
                if(plansRepository.existsByOrganizationId(organizationId)){
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Custom plan exists for organization with id " + organizationId + ".");
                    return responseDTO;
                }
                plan.setOrganization(currentOrganization);
            }
            if(orderId != null){
                Order currentOrder =  new Order();
                currentOrder.setId(orderId);
                plan.setOrder(currentOrder);
            }
            Plans savedPlan = plansRepository.save(plan);
            responseDTO.setData(savedPlan);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Plan created successfully");
        }catch(Exception e){
            log.error("Error creating the plan: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating the plan");
        }
        return responseDTO;
    }

    public ResponseDTO getPlans() {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Plans> plansList = plansRepository.findAllByOrganizationIsNullAndOrderIsNull();
            responseDTO.setData(plansList);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Plans list found");
        }catch(Exception e){
            log.error("Error finding plans list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding plans list");
        }
        return responseDTO;
    }

    public ResponseDTO getCustomPlanForOrganization(Long organizationId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Plans plan = plansRepository.getByOrganizationIdAndOrderId(organizationId, null);
            responseDTO.setData(plan);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Custom plan");
        }catch(Exception e){
            log.error("Error getting custom plan: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error getting custom plan");
        }
        return responseDTO;
    }

    public ResponseDTO updatePlanWithId(Plans updatePlan, Long planId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Plans original = plansRepository.findById(planId).orElse(null);
            Long id = original.getId();
            BeanUtils.copyProperties(updatePlan, original);
            original.setId(id);
            Plans updatedPlan = plansRepository.save(original);
            responseDTO.setData(updatedPlan);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Plan Updated");
        }catch(Exception e){
            log.error("Error updating plan: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating plan");
        }
        return responseDTO;
    }

    public ResponseDTO deletePlanWithId(Long planId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            plansRepository.deleteById(planId);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Plan Deleted");
        }catch(Exception e){
            log.error("Error deleting plan: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting plan");
        }
        return responseDTO;
    }
}
