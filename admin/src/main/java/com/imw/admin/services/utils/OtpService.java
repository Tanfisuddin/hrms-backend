package com.imw.admin.services.utils;

import com.imw.admin.services.UserService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.persistence.UserOtp;
import com.imw.commonmodule.repository.UserOtpRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    UserOtpRepository otpRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    private Logger log = LoggerFactory.getLogger(OtpService.class);

    private static final int OTP_LENGTH = 6;
    private static final String OTP_CHARACTERS = "0123456789";

    public ResponseDTO generateOtp(String email) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("User with email " + email + " does not exist");
                return responseDTO;
            }
            Random random = new Random();
            StringBuilder otp = new StringBuilder(OTP_LENGTH);
            for (int i = 0; i < OTP_LENGTH; i++) {
                int index = random.nextInt(OTP_CHARACTERS.length());
                otp.append(OTP_CHARACTERS.charAt(index));
            }
            Calendar calendar = Calendar.getInstance();

            UserOtp userOtp = new UserOtp();
            userOtp.setUser(user.get());
            userOtp.setOtp(otp.toString());
            userOtp.setCreatedAt(calendar.getTime());
            calendar.add(Calendar.MINUTE, 5);
            userOtp.setValidTill(calendar.getTime());
            otpRepository.save(userOtp);

            emailService.sendUserForgetPasswordEmail( user.get().getOrganization().getName(), user.get().getFullName(), email, otp.toString() );
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Otp generated");
        }catch(Exception e){
            log.error("Error generating otp: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error generating otp");
        }
        return responseDTO;
    }
}
