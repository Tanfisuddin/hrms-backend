package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.employee.*;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.employee.EmployeeInformationDto;
import com.imw.commonmodule.dto.employee.JoiningDetailsDto;
import com.imw.commonmodule.persistence.*;
import com.imw.admin.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class EmployeeProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private BankService bankService;


    @GetMapping(value = "user/{id}")
    public ResponseEntity<?> getEmployeeInformation (
            @PathVariable Long id
    ){
        ResponseDTO employeeInformation = userService.GetUserInfo(id);
        if (employeeInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,employeeInformation.getData(),employeeInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,employeeInformation.getMessage());
        }
    }

    @PutMapping(value = "user/{id}")
    public ResponseEntity<?> updateEmployeeInformation (
            @RequestBody @Valid EmployeeInformationDto employeeInformationDto,
            @PathVariable Long id
    ){
        ResponseDTO user = userService.UpdateUserInfo(id, employeeInformationDto);
        if (user.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,user.getData(),user.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,user.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/joining")
    public ResponseEntity<?> updateEmployeeJoiningDetails (
            @PathVariable Long id
    ){
        ResponseDTO joiningDetails = userService.GetUserJoining(id);
        if (joiningDetails.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,joiningDetails.getData(),joiningDetails.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,joiningDetails.getMessage());
        }
    }

    @PutMapping(value = "user/{id}/joining")
    public ResponseEntity<?> updateEmployeeJoiningDetails (
            @RequestBody @Valid JoiningDetailsDto joiningDetailsDto,
            @PathVariable Integer id
    ){
        ResponseDTO joiningDetails = userService.UpdateUserJoining(id, joiningDetailsDto);
        if (joiningDetails.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,joiningDetails.getData(),joiningDetails.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,joiningDetails.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/personal-information")
    public ResponseEntity<?> getEmployeePersonalInformation (
            @PathVariable Long id
    ){
        ResponseDTO  personalInformation = personalInformationService.getPersonalInformation(id);
        if ( personalInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,  personalInformation.getData(),  personalInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,  personalInformation.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/documents")
    public ResponseEntity<?> getEmployeeDocuments (
            @PathVariable Long id
    ){
        ResponseDTO  documents = documentService.getDocumentsByUserId(id);
        if ( documents.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,   documents.getData(),   documents.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,   documents.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/educations")
    public ResponseEntity<?> getEmployeeEducations (
            @PathVariable Long id
    ){
        ResponseDTO educations = educationService.getALlByUserId(id);
        if ( educations.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,   educations.getData(),   educations.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,   educations.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/address")
    public ResponseEntity<?> getEmployeeAddresses (
            @PathVariable Long id
    ){
        ResponseDTO addresses = addressService.getAddressesByUserId(id);
        if (addresses.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,addresses.getData(),addresses.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,addresses.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/contact")
    public ResponseEntity<?> getEmployeeContact (
            @PathVariable Long id
    ){
        ResponseDTO contact = contactService.getContactByUserId(id);
        if (contact.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,contact.getData(),contact.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,contact.getMessage());
        }
    }

    @GetMapping(value = "user/{id}/bank")
    public ResponseEntity<?> getEmployeeBank (
            @PathVariable Long id
    ){
        ResponseDTO bank = bankService.getBankByUserId(id);
        if (bank.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,bank.getData(),bank.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,bank.getMessage());
        }
    }

}
