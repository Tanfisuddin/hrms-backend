package com.imw.admin.services;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.UserIdsDTO;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.Shift;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.ShiftRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(ShiftService.class);

    public ResponseDTO createShift(UserDetailsImpl user, Shift shift){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Organization organization = organizationRepository.findById(user.getOrganization().getId()).get();
            shift.setOrganization(organization);
            if(shift.getIsGeoFencingEnabled()){
                if(shift.getLatitude() == null || shift.getLongitude() == null ||  shift.getRadius() == null || shift.getLocationName() == null || shift.getLocationName().isEmpty() ){
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Location details are needed to enable Geolocation");
                    return responseDTO;
                }
            }
            Shift savedShift = shiftRepository.save(shift);
            responseDTO.setData(savedShift);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Shift created successfully");
        }catch(Exception e){
            log.error("Error Creating Shift: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error Creating Shift");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateShift(UserDetailsImpl currentUser, Long shiftId, Shift updateShiftData){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Shift> shift = shiftRepository.findByIdAndOrganizationId(shiftId, currentUser.getOrganization().getId());
            if(shift.isPresent() ){
                Shift shiftUpdate = shift.get();

                shiftUpdate.setShiftName(updateShiftData.getShiftName());
                shiftUpdate.setIsGeoFencingEnabled(updateShiftData.getIsGeoFencingEnabled());

                if(updateShiftData.getIsGeoFencingEnabled()){
                    if(updateShiftData.getLatitude() == null || updateShiftData.getLongitude() == null ||  updateShiftData.getRadius() == null || updateShiftData.getLocationName() == null || updateShiftData.getLocationName().isEmpty()){
                        responseDTO.setSuccess(false);
                        responseDTO.setMessage("Location details are needed to enable Geolocation");
                        return responseDTO;
                    }
                }

                shiftUpdate.setLatitude(updateShiftData.getLatitude());
                shiftUpdate.setLongitude(updateShiftData.getLongitude());
                shiftUpdate.setRadius(updateShiftData.getRadius());
                shiftUpdate.setLocationName(updateShiftData.getLocationName());
                shiftUpdate.setShiftStartTime(updateShiftData.getShiftStartTime());
                shiftUpdate.setShiftEndTime(updateShiftData.getShiftEndTime());
                shiftUpdate.setBufferTimeTo(updateShiftData.getBufferTimeTo());
                shiftUpdate.setWorkingHour(updateShiftData.getWorkingHour());

                Shift updatedShift = shiftRepository.saveAndFlush(shiftUpdate);

                responseDTO.setData(updatedShift);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Shift updated successfully");

            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Shift with id "+ shiftId +" not found");
            }
        }catch(Exception e){
            log.error("Error updating Shift with id "+ shiftId +": {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating Shift with id "+ shiftId);
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO getShiftByUserId(Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.findById(userId).get();
            Shift shift =  (Shift) Hibernate.unproxy(user.getShift());

            responseDTO.setData(shift);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User shift");
        }catch(Exception e){
            log.error("Error finding user shift: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding user shift");
        }
        return responseDTO;
    }

    public ResponseDTO getShiftList(UserDetailsImpl user){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Shift> shifts = shiftRepository.findAllByOrganizationId(user.getOrganization().getId());
            responseDTO.setData(shifts);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Shift list found");
        }catch(Exception e){
            log.error("Error finding shifts: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding shifts");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO getShiftUserList(UserDetailsImpl user, Long shiftId, Pageable pageable){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(shiftRepository.findByIdAndOrganizationId(shiftId, user.getOrganization().getId()).isPresent() ){
                Page<User> users = userRepository.findAllByShiftId(shiftId, pageable);
                responseDTO.setData(users);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Users list for the shift " + shiftId + " found");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Shift with id "+ shiftId +" not found");
            }
        }catch(Exception e){
            log.error("Error finding list of users in shift: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding list of users in shift");
        }
        return responseDTO;
    }

    public ResponseDTO addUsersInShift(UserDetailsImpl currentUser, Long shiftId, UserIdsDTO userIdsDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Shift> shift = shiftRepository.findByIdAndOrganizationId(shiftId, currentUser.getOrganization().getId());
            if(shift.isPresent() ){
                List<User> users = userRepository.findAllByOrganizationIdAndIdIn(currentUser.getOrganization().getId(), userIdsDTO.getUserIds());
                if(users.size() == userIdsDTO.getUserIds().size()){
                    for (User user : users) {
                        user.setShift(shift.get());
                    }
                    List<User> savedUsers = userRepository.saveAll(users);
                    responseDTO.setData(savedUsers);
                    responseDTO.setSuccess(true);
                    responseDTO.setMessage("Users added successfully");
                }else{
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Some user's not found in organization");
                }
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Shift with id "+ shiftId +" not found");
            }
        }catch(Exception e){
            log.error("Error adding users in the shift: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error adding users in the shift");
        }
        return responseDTO;
    }

    public ResponseDTO removeUsersFromShift(UserDetailsImpl currentUser, Long shiftId, UserIdsDTO userIdsDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Shift> shift = shiftRepository.findByIdAndOrganizationId(shiftId, currentUser.getOrganization().getId());
            if(shift.isPresent() ){
                List<User> users = userRepository.findAllByOrganizationIdAndIdIn(currentUser.getOrganization().getId(), userIdsDTO.getUserIds());
                if(users.size() == userIdsDTO.getUserIds().size()){
                    for (User user : users) {
                        user.setShift(null);
                    }
                    userRepository.saveAll(users);
                    responseDTO.setData(null);
                    responseDTO.setSuccess(true);
                    responseDTO.setMessage("Users removed from shift successfully");
                }else{
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Some user's not found in organization");
                }
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Shift with id "+ shiftId +" not found");
            }
        }catch(Exception e){
            log.error("Error removing users from the shift: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error removing users from the shift");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteShift(UserDetailsImpl currentUser, Long shiftId){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Shift> shift = shiftRepository.findByIdAndOrganizationId(shiftId, currentUser.getOrganization().getId());
            if(shift.isPresent() ){
                List<User> users = userRepository.findAllByShiftId(shiftId);
                for (User user : users) {
                    user.setShift(null);
                }
                userRepository.saveAll(users);
                shiftRepository.delete(shift.get());
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Shift deleted successfully");

            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Shift with id "+ shiftId +" not found");
            }
        }catch(Exception e){
            log.error("Error deleting Shift with id "+ shiftId +": {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting Shift with id "+ shiftId);
        }
        return responseDTO;
    }
}
