package com.imw.admin.controller;

import com.imw.admin.services.AuthService;
import com.imw.admin.services.utils.OtpService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.auth.ChangePasswordRequestDto;
import com.imw.commonmodule.dto.auth.ResetPasswordRequestDto;
import com.imw.commonmodule.dto.auth.ResetTokenRequestDto;
import com.imw.commonmodule.dto.auth.UserAuthResponseDto;
import com.imw.admin.payload.LoginRequest;
import com.imw.admin.payload.MessageResponse;
import com.imw.admin.payload.SignupRequest;
import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.Role;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.RoleRepository;
import com.imw.commonmodule.repository.UserRepository;
import com.imw.admin.security.JwtUtils;
import com.imw.admin.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static com.imw.admin.common.ApiConstants.API_V1;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping( API_V1 + "/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    OtpService otpService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseDTO signInResponse = authService.userSignIn(loginRequest);
        if (signInResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,signInResponse.getData(),signInResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,signInResponse.getMessage());
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordWithOldPassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        ResponseDTO changePasswordResponse = authService.changePasswordWithOldPassword(changePasswordRequestDto);
        if (changePasswordResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,changePasswordResponse.getData(),changePasswordResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,changePasswordResponse.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotUserPassword(@RequestParam(defaultValue = "") String emailId ) {
        ResponseDTO forgotPasswordResponse = otpService.generateOtp(emailId);
        if (forgotPasswordResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,forgotPasswordResponse.getData(),forgotPasswordResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,forgotPasswordResponse.getMessage());
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> getPasswordResetToken(@Valid @RequestBody ResetTokenRequestDto resetTokenRequestDto) {
        ResponseDTO resetTokenResponse = authService.getPasswordResetToken(resetTokenRequestDto.getEmail(), resetTokenRequestDto.getOtp());
        if (resetTokenResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,resetTokenResponse.getData(),resetTokenResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,resetTokenResponse.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasswordWithToken(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        ResponseDTO resetTokenResponse = authService.resetPasswordWithToken(resetPasswordRequestDto);
        if (resetTokenResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,resetTokenResponse.getData(),resetTokenResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,resetTokenResponse.getMessage());
        }
    }

    @GetMapping("organizations")
    public ResponseEntity<?> getUserOrganizations(@RequestParam(defaultValue = "") String email) {
        ResponseDTO response = authService.userOrganizations(email);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @GetMapping("user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ORG_ADMIN','ROLE_ORG_OWNER','ROLE_ADMIN')")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO signInResponse = authService.getAuthenticatedUser(currentUser);
        if (signInResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,signInResponse.getData(),signInResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,signInResponse.getMessage());
        }
    }
}
