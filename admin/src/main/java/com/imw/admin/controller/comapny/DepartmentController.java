package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.payload.MessageResponse;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.UserService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.department.DepartmentCreateDto;
import com.imw.commonmodule.dto.NotFoundResponse;
import com.imw.commonmodule.persistence.Department;
import com.imw.admin.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @PostMapping(value="department")
    public ResponseEntity<?> addDepartment(Authentication authentication, @RequestBody @Valid DepartmentCreateDto departmentCreateDto) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        if(departmentService.departmentExistByNameAndOrganization(departmentCreateDto.getName(), currentUser.getOrganization().getId())){
            return ResponseEntity.badRequest().body( new MessageResponse("Department with name " +departmentCreateDto.getName()+ " already exist"));
        }
        if(
                departmentCreateDto.getDepartmentHead()!=null &&
                ! userService.userExistByIdAndOrganizationId(departmentCreateDto.getDepartmentHead(), currentUser.getOrganization().getId())
        ){
            return ResponseEntity.badRequest().body( new MessageResponse("Department Head with Id " +departmentCreateDto.getDepartmentHead()+ " does not exist"));
        }
        ResponseDTO department = departmentService.addDepartMent(currentUser, departmentCreateDto);
        if (department.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,department.getData(),department.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,department.getMessage());
        }
    }

    @PutMapping(value="department/{departmentId}/head")
    public ResponseEntity<?> addDepartmentHead(Authentication authentication, @PathVariable Long departmentId , @RequestParam(required = false) Long departmentHeadId) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO department = departmentService.updateDepartmentHead(currentUser, departmentId, departmentHeadId);
        if (department.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,department.getData(),department.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,department.getMessage());
        }
    }

    @GetMapping(value="departments")
    public ResponseEntity<?> getDepartmentList(Authentication authentication, Pageable pageable) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO departments = departmentService.getAllDepartments(currentUser, pageable);
        if (departments.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,departments.getData(),departments.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,departments.getMessage());
        }
    }

    @GetMapping(value="department/{id}")
    public ResponseEntity<?> getDepartmentWithId(Authentication authentication, @PathVariable Long id) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO department = departmentService.getDepartmentById(currentUser, id);
        if (department.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,department.getData(),department.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,department.getMessage());
        }
    }
    @GetMapping(value="department/{id}/employees")
    public ResponseEntity<?> getEmployeesInDepartment(Authentication authentication, @PathVariable Long id) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO employees = departmentService.getEmployeesInDepartmentWithId(currentUser, id);
        if (employees.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,employees.getData(),employees.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,employees.getMessage());
        }
    }
}
