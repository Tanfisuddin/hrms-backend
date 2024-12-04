package com.imw.admin.services;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.AccessoryDto;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.enums.accessories.DeviceType;
import com.imw.commonmodule.persistence.Accessory;
import com.imw.commonmodule.persistence.Department;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.AccessoryRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
public class AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(AccessoryService.class);

    public ResponseDTO findAccessories(Pageable pageable,UserDetailsImpl currentUser, String search, DeviceType deviceType) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Page<Accessory> accessories ;
            if(search != null && !search.isEmpty() && deviceType != null){
                accessories =  accessoryRepository.findAllByUserOrganizationIdAndUserFullNameContainingAndDeviceType(currentUser.getOrganization().getId(), search, deviceType, pageable);
            }else if(search != null && !search.isEmpty()){
                accessories =  accessoryRepository.findAllByUserOrganizationIdAndUserFullNameContaining(currentUser.getOrganization().getId(), search, pageable);
            }else if(deviceType != null){
                accessories =  accessoryRepository.findAllByUserOrganizationIdAndDeviceType(currentUser.getOrganization().getId(), deviceType, pageable);
            }else{
                accessories =  accessoryRepository.findAllByUserOrganizationId(currentUser.getOrganization().getId(), pageable);
            }
            responseDTO.setData(accessories.map(this::convertToAccessoryDTO));
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Accessory list found");
        }catch(Exception e){
            log.error("Error searching for accessory list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error searching for accessory list");
        }
        return responseDTO;
    }

    public ResponseDTO createAccessory(UserDetailsImpl currentUser, AccessoryDto accessoryDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> employee = userRepository.findByEmployeeIdAndOrganizationId(accessoryDto.getEmployeeId(), currentUser.getOrganization().getId());
            if(employee.isPresent()){
                Accessory accessory = new Accessory();
                accessory.setAccessoryName(accessoryDto.getAccessoryName());
                accessory.setSerialNo(accessoryDto.getSerialNo());
                accessory.setDeviceType(accessoryDto.getDeviceType());
                accessory.setUser(employee.get());

                Accessory createdAccessory = accessoryRepository.save(accessory);
                responseDTO.setData(createdAccessory);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Accessory created successfully");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with Employee Id " + accessoryDto.getEmployeeId() + " doesn't exists");
            }

        }catch(Exception e){
            log.error("Error creating accessory: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating accessory");
        }
        return responseDTO;
    }

    public ResponseDTO deleteAccessory(UserDetailsImpl currentUser, Long accessoryId){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(accessoryRepository.existsByIdAndUserOrganizationId(accessoryId, currentUser.getOrganization().getId())){
                accessoryRepository.deleteById(accessoryId);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Accessory with id " + accessoryId + " deleted successfully");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Accessory with id " + accessoryId + " doesn't exists");
            }
        }catch(Exception e){
            log.error("Error deleting accessory with id : {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting accessory with id");
        }
        return responseDTO;
    }

    public ResponseDTO getAccessoryListByUser(UserDetailsImpl currentUser){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            List<Accessory> accessories = accessoryRepository.findAllByUserId(currentUser.getId());
            List<AccessoryDto> accessoriesDto= new ArrayList<>();
            for(Accessory accessory : accessories){
                accessoriesDto.add(convertToAccessoryDTO(accessory));
            }
            responseDTO.setData(accessoriesDto);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Accessory list found");
        }catch(Exception e){
            log.error("Error finding accessory list : {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding accessory list");
        }
        return responseDTO;
    }

    private AccessoryDto convertToAccessoryDTO(Accessory accessory) {
        AccessoryDto accessoryDto = new AccessoryDto();
        accessoryDto.setAccessoryName(accessory.getAccessoryName());
        accessoryDto.setSerialNo(accessory.getSerialNo());
        accessoryDto.setDeviceType(accessory.getDeviceType());
        accessoryDto.setEmployeeId(accessory.getUser().getEmployeeId());
        accessoryDto.setEmployeeName(accessory.getUser().getFullName());
        accessoryDto.setId(accessory.getId());
        return accessoryDto;
    }

}
