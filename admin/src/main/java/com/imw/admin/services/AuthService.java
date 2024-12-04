package com.imw.admin.services;

import com.imw.admin.payload.LoginRequest;
import com.imw.admin.security.JwtUtils;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.auth.ChangePasswordRequestDto;
import com.imw.commonmodule.dto.auth.ResetPasswordRequestDto;
import com.imw.commonmodule.dto.auth.UserAuthResponseDto;
import com.imw.commonmodule.dto.department.DepartmentResponseDto;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.PasswordResetToken;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.persistence.UserOtp;
import com.imw.commonmodule.repository.PasswordResetTokenRepository;
import com.imw.commonmodule.repository.RoleRepository;
import com.imw.commonmodule.repository.UserOtpRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserOtpRepository userOtpRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    private Logger log = LoggerFactory.getLogger(AuthService.class);

    public ResponseDTO userSignIn(LoginRequest loginRequest) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Map<String, String>  response = new HashMap<>();
            response.put("token", jwt);
            responseDTO.setData(response);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Signed in successfully");
        }catch(Exception e){
            if(e instanceof BadCredentialsException){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Bad credential, email and password don't match");
                return responseDTO;
            }
            log.error("Error signing in user: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error signing in user");
        }
        return responseDTO;
    }
    public ResponseDTO userOrganizations(String email) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findByEmail(email);
            ArrayList organizationList = new ArrayList<>();
            if(user.isPresent()){
                Organization organization = user.get().getOrganization();
                user.get().getRoles().forEach(role -> {
                    Map<String, String> roleObject = new HashMap();
                    roleObject.put("role",role.getName().name());
                    roleObject.put("organizationName",organization.getName());
                    roleObject.put("organizationAbbreviation",organization.getAbbreviation());
                    organizationList.add(roleObject);
                });

            }
            responseDTO.setData(organizationList);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Organization list found");
        }catch(Exception e){
            log.error("Error getting organization list: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error getting organization list");
        }
        return responseDTO;
    }
    public ResponseDTO getAuthenticatedUser(UserDetailsImpl currentUser) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findById(currentUser.getId());

            Map<String, User>  response = new HashMap<>();
            response.put("user", user.get());
            responseDTO.setData(response);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User details retrieved");
        }catch(Exception e){
            log.error("Error fetching user: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error fetching user");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO getPasswordResetToken(String email, String otp) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findByEmail(email);
            Calendar calendar = Calendar.getInstance();

            if(user.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with email " + email + " does not exist");
                return responseDTO;
            }

            Optional<UserOtp> userOtp =  userOtpRepository.findByUserIdAndOtp(user.get().getId(), otp);
            if( userOtp.isEmpty() || calendar.getTime().after(userOtp.get().getValidTill()) ) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("The otp is invalid or expired");
                return responseDTO;
            }
            userOtpRepository.deleteAllByUserId(user.get().getId());

            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user.get());

            passwordResetToken.setCreatedAt(calendar.getTime());
            calendar.add(Calendar.MINUTE, 5);
            passwordResetToken.setValidTill(calendar.getTime());

            String resetToken = generateToken();
            passwordResetToken.setResetToken(resetToken);
            passwordResetTokenRepository.save(passwordResetToken);

            Map<String, String>  response = new HashMap<>();
            response.put("token", resetToken);
            responseDTO.setData(response);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Password reset token generated");
        }catch(Exception e){
            log.error("Error generating reset token: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error generating reset token");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO resetPasswordWithToken(ResetPasswordRequestDto resetPasswordRequestDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findByEmail(resetPasswordRequestDto.getEmail());
            Calendar calendar = Calendar.getInstance();

            if(user.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with email " + resetPasswordRequestDto.getEmail() + " does not exist");
                return responseDTO;
            }

            Optional<PasswordResetToken> PasswordResetToken =  passwordResetTokenRepository.findByUserIdAndResetToken(user.get().getId(), resetPasswordRequestDto.getResetToken());
            if( PasswordResetToken.isEmpty() || calendar.getTime().after(PasswordResetToken.get().getValidTill()) ) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("The reset token is invalid or expired");
                return responseDTO;
            }
            passwordResetTokenRepository.deleteAllByUserId(user.get().getId());

            User updateUser = user.get();
            updateUser.setPassword(encoder.encode(resetPasswordRequestDto.getPassword()));
            userRepository.saveAndFlush(updateUser);

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Password changed successfully");
        }catch(Exception e){
            log.error("Error changing password: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error changing password");
        }
        return responseDTO;
    }

    @Transactional
    public ResponseDTO changePasswordWithOldPassword(ChangePasswordRequestDto changePasswordRequestDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findByEmail(changePasswordRequestDto.getEmail());

            if(user.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with email " + changePasswordRequestDto.getEmail() + " does not exist");
                return responseDTO;
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(changePasswordRequestDto.getEmail(), changePasswordRequestDto.getOldPassword()));

            User updateUser = user.get();
            updateUser.setPassword(encoder.encode(changePasswordRequestDto.getNewPassword()));
            userRepository.saveAndFlush(updateUser);

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Password changed successfully");
        }catch(Exception e){
            if(e instanceof BadCredentialsException){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Bad credentials");
            }else{
                log.error("Error changing password: {}", e.getMessage());
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Error changing password");
            }
        }
        return responseDTO;
    }

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
