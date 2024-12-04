package com.imw.admin.services;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.utils.EmailService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.employee.CreateEmployeeDto;
import com.imw.commonmodule.dto.employee.EmployeeInformationDto;
import com.imw.commonmodule.dto.employee.JoiningDetailsDto;
import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.*;
import com.imw.commonmodule.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    AccessoryRepository accessoryRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    PersonalInformationRepository personalInformationRepository;

    @Autowired
    UserFileRepository userFileRepository;

    @Autowired
    UserOtpRepository userOtpRepository;

    @Autowired
    EmailService emailService;

    private Logger log = LoggerFactory.getLogger(UserService.class);

    public ResponseDTO  GetUserInfo(Long employeeId){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.findById(employeeId).get();
            EmployeeInformationDto employeeInformation = new EmployeeInformationDto();

            employeeInformation.setTitle(user.getTitle());
            employeeInformation.setFullName(user.getFullName());
            employeeInformation.setEmployeeId(user.getEmployeeId());
            employeeInformation.setProfileImage(user.getProfileImage());

            employeeInformation.setEmail(user.getEmail());
            Contact contact = contactRepository.getByUserId(employeeId);
            if(contact != null){
                employeeInformation.setPhone(contact.getPhoneNumber1());
            }
            employeeInformation.setGender(user.getGender());

            User reportingAuthority = user.getReportingAuthority();
            if(reportingAuthority!= null){
                employeeInformation.setReportingAuthorityId(reportingAuthority.getId());
                employeeInformation.setReportingAuthorityName(reportingAuthority.getFullName());
            }
            responseDTO.setData(employeeInformation);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User information found");
        }catch(Exception e){
            log.error("Error finding user information: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding user information");
        }
        return responseDTO;
    }

    public ResponseDTO UpdateUserInfo(Long employeeId, EmployeeInformationDto employeeInformationDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.getReferenceById(employeeId);

            user.setTitle(employeeInformationDto.getTitle());
            user.setFullName(employeeInformationDto.getFullName());
            user.setEmployeeId(employeeInformationDto.getEmployeeId());

            user.setEmail(employeeInformationDto.getEmail());

            Contact contact = contactRepository.getByUserId(employeeId);
            if(contact == null){
                contact = new Contact();
            }
            contact.setPhoneNumber1(employeeInformationDto.getPhone());
            contact.setUser(user);

            user.setGender(employeeInformationDto.getGender());

            if(employeeInformationDto.getReportingAuthorityId() != null){
                User reportingUser = userRepository.getReferenceById(employeeInformationDto.getReportingAuthorityId());
                user.setReportingAuthority(reportingUser);
            }else{
                user.setReportingAuthority(null);
            }
            User savedUser =  userRepository.save(user);
            responseDTO.setData(null);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Updated user information");
        }catch(Exception e){
            log.error("Error updating user information: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating user information");
        }
        return responseDTO;
    }

    public ResponseDTO UpdateUserJoining(Integer employeeId, JoiningDetailsDto joiningDetailsDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.getReferenceById(Long.valueOf(employeeId));

            user.setJoiningDate(joiningDetailsDto.getJoiningDate());
            user.setNoticePeriod(joiningDetailsDto.getNoticePeriod());
            user.setJobType(joiningDetailsDto.getJobType());

            User savedUser = userRepository.save(user);

            JoiningDetailsDto joiningDetailsDto1 = new JoiningDetailsDto();
            joiningDetailsDto1.setJoiningDate(savedUser.getJoiningDate());
            joiningDetailsDto1.setNoticePeriod(savedUser.getNoticePeriod());
            joiningDetailsDto1.setJobType(savedUser.getJobType());

            responseDTO.setData(joiningDetailsDto1);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Updated user joining details");
        }catch(Exception e){
            log.error("Error updating user joining details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating user joining details");
        }
        return responseDTO;
    }

    public ResponseDTO GetUserJoining(Long employeeId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.getReferenceById(employeeId);
            JoiningDetailsDto joiningDetailsDto = new JoiningDetailsDto();

            joiningDetailsDto.setJoiningDate(user.getJoiningDate());
            joiningDetailsDto.setNoticePeriod(user.getNoticePeriod());
            joiningDetailsDto.setJobType(user.getJobType());

            responseDTO.setData(joiningDetailsDto);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Joining details found");
        }catch(Exception e){
            log.error("Error finding the joining details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding the joining details");
        }
        return responseDTO;
    }

    public ResponseDTO updateUserRole(UserDetailsImpl loggedUser, Long updateUserId, String role) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User user = userRepository.findById(updateUserId).orElse(null);
            for(Role userRole : user.getRoles()){
                if(userRole.getName().equals(ERole.ROLE_ORG_OWNER)){
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Cannot update organization owner role");
                    return responseDTO;
                }
            }
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role User is not found."));
            roles.add(userRole);
            if (role != null && role.equals("admin")) {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ORG_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role Org Admin is not found."));
                roles.add(adminRole);
            }
            user.setRoles(roles);
            userRepository.save(user);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User roles updated successfully.");
        }catch(Exception e){
            log.error("Error updating the user roles: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating the user roles.");
        }
        return responseDTO;
    }

    public Boolean userExistByIdAndOrganizationId(Long employeeId, Long organizationId) {
        return userRepository.existsByIdAndOrganizationId(employeeId,organizationId);
    }

    @Transactional
    public ResponseDTO CreateUser(UserDetailsImpl currentUser, CreateEmployeeDto employeeDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            User user = new User();
            if(userRepository.findByEmail(employeeDto.getEmail()).isPresent()){
                responseDTO.setMessage("Email already exists");
                responseDTO.setSuccess(false);
                return responseDTO;
            } else if (userRepository.existsByEmployeeIdAndOrganizationId(employeeDto.getEmployeeId(), currentUser.getOrganization().getId())) {
                responseDTO.setMessage("Employee with given id already exists");
                responseDTO.setSuccess(false);
                return responseDTO;
            }
            user.setFullName(employeeDto.getFullName());
            user.setEmployeeId(employeeDto.getEmployeeId());
            user.setJoiningDate(employeeDto.getJoiningDate());
            user.setDesignation(employeeDto.getJobRole());
            user.setEmail(employeeDto.getEmail());
            Department department = null;
            if (employeeDto.getDepartmentId() != null) {
                department = departmentRepository.findByIdAndOrganizationId(employeeDto.getDepartmentId(), currentUser.getOrganization().getId());
                user.setDepartment(department);
            }

            user.setJobType(employeeDto.getJobType());
            user.setJobRole(employeeDto.getJobRole());
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role User is not found."));
            roles.add(userRole);
            if (employeeDto.getRole() != null && employeeDto.getRole().equals("admin")) {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ORG_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role Org Admin is not found."));
                roles.add(adminRole);
            }
            user.setRoles(roles);
            if (employeeDto.getReportingAuthorityId() != null) {
                User reportingUser = userRepository.getReferenceById(Long.valueOf((int) (long) employeeDto.getReportingAuthorityId()));
                user.setReportingAuthority(reportingUser);
            }else if(department != null){
                User departmentHead = department.getDepartmentHead();
                if(departmentHead != null){
                    user.setReportingAuthority(departmentHead);
                }
            }
            user.setOrganization(currentUser.getOrganization());
            String password = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(password));
            List<Shift> shifts = shiftRepository.findAllByOrganizationId(currentUser.getOrganization().getId());
            if(!shifts.isEmpty()){
                user.setShift(shifts.get(0));
            }
            User savedUser = userRepository.save(user);

            Contact contact = new Contact();
            contact.setPhoneNumber1(employeeDto.getPhone());
            contact.setUser(savedUser);
            contactRepository.save(contact);

            // Send email with user credentials
            emailService.sendUserCredentialsEmail(currentUser.getOrganization().getName(),savedUser.getFullName(),savedUser.getEmail(), password, savedUser.getEmail());

            responseDTO.setData(savedUser);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User Created Successfully!");
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating user");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO deleteUser(UserDetailsImpl currentUser, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(userRepository.existsByIdAndOrganizationId(userId, currentUser.getOrganization().getId())){
                User user = userRepository.getReferenceById(userId);
                Set<Role> userRoles = user.getRoles();
                Optional<Role> ownerRole = roleRepository.findByName(ERole.ROLE_ORG_OWNER);
                if(ownerRole.isPresent() && !userRoles.contains(ownerRole.get())){
                    personalInformationRepository.deleteByUserId(user.getId());
                    contactRepository.deleteByUserId(user.getId());
                    bankRepository.deleteByUserId(user.getId());
                    addressRepository.deleteByUserId(user.getId());
                    attendanceRepository.deleteAllByUserId(user.getId());
                    accessoryRepository.deleteAllByUserId(user.getId());
                    departmentRepository.updateAllDepartmentHeadByDepartmentHeadId(null, user.getId());
                    documentRepository.deleteAllByUserId(user.getId());
                    educationRepository.deleteAllByUserId(user.getId());
                    userFileRepository.deleteAllByUserId(user.getId());
                    userOtpRepository.deleteAllByUserId(user.getId());
                    userRepository.updateAllUserReportingAuthorityByUserReportingAuthorityId(null, user.getId());
                    userRepository.delete(user);

                    responseDTO.setSuccess(true);
                    responseDTO.setMessage("User deleted Successfully!");
                }else{
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Can not delete employee with Id "+userId);
                }
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Employee with Id not found.");
            }
        }catch(Exception e){
            log.error("Error deleting Employee: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error deleting Employee");
        }
        return responseDTO;
    }

    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = upperCaseLetters.toLowerCase();
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+";
        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }

    public ResponseDTO<?> GetAllUsers(Long organizationId, Pageable pageable, String search, String designation, Long departmentId, ERole role) {

        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Page<User> employees;
            if(departmentId!=null) {
                if(search!=null && !search.isBlank()){
                    if(designation!=null && !designation.isBlank()){
                        employees = userRepository.findAllByOrganizationIdAndDepartmentIdAndDesignationContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRolesName(organizationId,departmentId, designation, search, role, pageable);
                    }else{
                        employees = userRepository.findAllByOrganizationIdAndDepartmentIdAndFullNameContainingIgnoreCaseAndRolesName(organizationId,departmentId, search, role, pageable);
                    }
                }else{
                    if(designation!=null && !designation.isBlank()){
                        employees = userRepository.findAllByOrganizationIdAndDepartmentIdAndDesignationContainingIgnoreCaseAndRolesName(organizationId,departmentId, designation, role, pageable);
                    }else{
                        employees = userRepository.findAllByOrganizationIdAndDepartmentIdAndRolesName(organizationId,departmentId, role, pageable);
                    }
                }
            }else{
                if(search!=null && !search.isBlank()){
                    if(designation!=null && !designation.isBlank()){
                        employees = userRepository.findAllByOrganizationIdAndDesignationContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRolesName(organizationId, designation, search, role, pageable);
                    }else{
                        employees = userRepository.findAllByOrganizationIdAndFullNameContainingIgnoreCaseAndRolesName(organizationId, search, role, pageable);
                    }
                }else{
                    if(designation!=null && !designation.isBlank()){
                        employees = userRepository.findAllByOrganizationIdAndDesignationContainingIgnoreCaseAndRolesName(organizationId, designation, role, pageable);
                    }else{
                        employees = userRepository.findAllByOrganizationIdAndRolesName(organizationId, role, pageable);
                    }
                }
            }
            responseDTO.setData(employees);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Users found");
        }catch(Exception e){
            log.error("Error searching for users: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error searching for users");
        }
        return responseDTO;
    }

    public ResponseDTO updateUserDepartment(UserDetailsImpl loggedUser, Long updateUserId, Long departmentId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{

            User user = userRepository.findById(updateUserId).orElse(null);
            Department department = departmentRepository.findByIdAndOrganizationId(departmentId, loggedUser.getOrganization().getId());
            if(department == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Department with Id "+departmentId+ " doesn't exist.");
                return responseDTO;
            }
            user.setDepartment(department);
            userRepository.save(user);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User department updated successfully.");
        }catch(Exception e){
            log.error("Error updating the user department: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating the user department.");
        }
        return responseDTO;
    }

    public ResponseDTO updateUserReportingAuthority(UserDetailsImpl loggedUser, Long updateUserId, Long reportingAuthorityId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{

            User updatingUser = userRepository.findById(updateUserId).orElse(null);
            User reportingUser = userRepository.findById(reportingAuthorityId).orElse(null);
            if(reportingUser == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Reporting User with Id "+reportingAuthorityId+ " doesn't exist.");
                return responseDTO;
            }
            updatingUser.setReportingAuthority(reportingUser);
            userRepository.save(updatingUser);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User reporting authority updated successfully.");
        }catch(Exception e){
            log.error("Error updating the user reporting authority: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating the user authority.");
        }
        return responseDTO;
    }
    public ResponseDTO updateUserProfileImage(Long userId, String imageUrl) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            User updatingUser = userRepository.findById(userId).orElse(null);
            if(updatingUser == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with Id "+userId+ " doesn't exist.");
                return responseDTO;
            }
            updatingUser.setProfileImage(imageUrl);
            userRepository.save(updatingUser);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User profile image updated successfully.");
        }catch(Exception e){
            log.error("Error updating the user profile image: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error updating the user profile image.");
        }
        return responseDTO;
    }
}