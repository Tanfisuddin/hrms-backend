package com.imw.admin.services;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.department.DepartmentCreateDto;
import com.imw.commonmodule.dto.department.DepartmentResponseDto;
import com.imw.commonmodule.persistence.Department;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.DepartmentRepository;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.UserRepository;
import com.imw.admin.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Logger log = LoggerFactory.getLogger(DepartmentService.class);

    public ResponseDTO addDepartMent(UserDetailsImpl currentUser, DepartmentCreateDto departmentCreateDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Department department = new Department();

            department.setName(departmentCreateDto.getName());
            department.setName(departmentCreateDto.getName());
            department.setOrganization(currentUser.getOrganization());
            if(departmentCreateDto.getDepartmentImage() != null && !departmentCreateDto.getDepartmentImage().isEmpty()){
                department.setImageUrl(departmentCreateDto.getDepartmentImage());
            }
            if(departmentCreateDto.getDepartmentHead() != null){
                department.setDepartmentHead( userRepository.findById(departmentCreateDto.getDepartmentHead()).get() );
            }
            Department savedDepartment = departmentRepository.save(department);
            responseDTO.setData(savedDepartment);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Department created successfully");
        }catch(Exception e){
            log.error("Error creating department: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating department");
        }
        return responseDTO;
    }

    public ResponseDTO getAllDepartments(UserDetailsImpl currentUser, Pageable pageable) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Page<DepartmentResponseDto> departments = departmentRepository.findAllByOrganizationId(pageable, currentUser.getOrganization().getId());
            responseDTO.setData(departments);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Departments found");
        }catch(Exception e){
            log.error("Error finding departments: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding departments");
        }
        return responseDTO;
    }
    public ResponseDTO updateDepartmentHead(UserDetailsImpl currentUser, Long departmentId, Long departmentHeadId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<Department> department = departmentRepository.findById(departmentId);
            if(department.isEmpty() || department.get().getOrganization().getId() != currentUser.getOrganization().getId()){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Department with id " + departmentId + " not found");
                return responseDTO;
            }
            if( departmentHeadId!=null && !userRepository.existsByIdAndOrganizationId(departmentHeadId, currentUser.getOrganization().getId())){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Department Head with id " + departmentHeadId + " not found");
                return responseDTO;
            }
            Department updateDepartment = department.get();
            if(departmentHeadId!=null){
                updateDepartment.setDepartmentHead(userRepository.findById(departmentHeadId).get());
            }else{
                updateDepartment.setDepartmentHead(null);
            }
            Department updatedDepartment = departmentRepository.save(updateDepartment);
            responseDTO.setData(updatedDepartment);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Departments head updated successfully");
        }catch(Exception e){
            log.error("Error updating department head: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating department head: {}");
        }
        return responseDTO;
    }

    public ResponseDTO getDepartmentById(UserDetailsImpl currentUser, Long departmentId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Department department = departmentRepository.findByIdAndOrganizationId(departmentId, currentUser.getOrganization().getId());
            responseDTO.setData(department);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Department with id found");
        }catch(Exception e){
            log.error("Error finding department with id: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding department with id");
        }
        return responseDTO;
    }

    public ResponseDTO getEmployeesInDepartmentWithId(UserDetailsImpl currentUser, Long departmentId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Department department = departmentRepository.findByIdAndOrganizationId(departmentId, currentUser.getOrganization().getId());
            if(department!=null){
                Set<User> users = department.getEmployees() ;
                responseDTO.setData(users);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Department with id found");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Department with id not found");
            }
        }catch(Exception e){
            log.error("Error finding department with id: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding department with id");
        }
        return responseDTO;
    }

    public Boolean departmentExistByNameAndOrganization(String departmentName, Long organizationId) {
        return  departmentRepository.existsByNameAndOrganizationId(departmentName, organizationId);
    }
}
