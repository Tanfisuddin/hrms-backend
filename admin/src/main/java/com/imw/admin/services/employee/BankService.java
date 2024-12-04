package com.imw.admin.services.employee;

import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.Address;
import com.imw.commonmodule.persistence.Bank;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.BankRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {


    @Autowired
    BankRepository bankRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(BankService.class);

    public ResponseDTO getBankByUserId(Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Bank bank = bankRepository.getByUserId(userId);
            responseDTO.setData(bank);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Bank details with id found");
        }catch(Exception e){
            log.error("Error finding bank details with id: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding bank details with id");
        }
        return responseDTO;
    }

    public ResponseDTO saveBank(Bank bank, Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Bank bank1  = bankRepository.getByUserId(userId);
            Bank savedBank ;
            if (bank1 != null) {
                bank1.setBankName(bank.getBankName());
                bank1.setAccountNumber(bank.getAccountNumber());
                bank1.setIfscCode(bank.getIfscCode());
                bank1.setNameOnAccount(bank.getNameOnAccount());
                savedBank = bankRepository.save(bank1);
            }
            else{
                User user = userRepository.getReferenceById(Long.valueOf((int) (long) userId));
                bank.setUser(user);
                savedBank = bankRepository.save(bank);
            }
            responseDTO.setData(savedBank);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Bank details saved successfully");
        }catch(Exception e){
            log.error("Error saving bank details: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error saving bank details");
        }
        return responseDTO;
    }
}
