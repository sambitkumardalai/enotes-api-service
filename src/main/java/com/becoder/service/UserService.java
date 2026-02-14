package com.becoder.service;

import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.PasswordResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	public void changePassword(PasswordChngRequest passwordRequest);

	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

	public void verifyPasswordResetLink(Integer uid, String code) throws Exception;

	public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;
}
