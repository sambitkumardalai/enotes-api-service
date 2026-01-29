package com.becoder.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.becoder.entity.User;

public interface JwtService {

	public String extractUsername(String token);

	public String generateToken(User user);

	public Boolean validateToken(String token, UserDetails userDetails);

}
