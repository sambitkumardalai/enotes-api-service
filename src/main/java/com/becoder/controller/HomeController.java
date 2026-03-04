package com.becoder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PasswordResetRequest;
import com.becoder.endpoint.HomeEndpoint;
import com.becoder.service.HomeService;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController implements HomeEndpoint {
    Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        log.info("HomeController : verifyUserAccount() : Execution start");

        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if (verifyAccount)
            return CommonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
        log.info("HomeController : verifyUserAccount() : Execution end");
        return CommonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Email Send Success !! Check Email Reset Password", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code)
            throws Exception {
        userService.verifyPasswordResetLink(uid, code);
        return CommonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password reset succes", HttpStatus.OK);
    }

}
