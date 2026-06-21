package com.hiresphere.hiresphere.Auth.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceProvider {

	@Value("${jwt.secretKey}") // get from properties file 
	private String jwtSecretKey;
	
	
	
	// now create the secretKey
	private SecretKey  getSecretKey() 
	{
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	
	
	
	// Generate the Access Token 
	// this token is always short time taken and again again refresh through the refresh 
	
	public String genrateAccsessToken(Users user)
	{
	    return Jwts.builder()
	            .subject(user.getUserId().toString())
	            .claim("Username", user.getUsername())
	            .claim("Role", user.getRole())
	            .issuedAt(new Date())
	            .expiration(new Date(System.currentTimeMillis() + 1000L*60*15))
	            .signWith(getSecretKey())
	            .compact();
	}
	

	
	// generate the refresh token 
	// this token is regenerate the Access token and it is long time token
	public String genrateRefreshToken(Users user)
	{
	    return Jwts.builder()
	            .subject(user.getUserId().toString())
	            .issuedAt(new Date())
	            .expiration(new Date(System.currentTimeMillis() + 1000L*60*60*24*30*3))
	            .signWith(getSecretKey())
	            .compact();
	}
	
	// to get UserId from the token
	
		public Long getUserIdfromToken(String Token)
		{
			Claims claims = Jwts.parser()
					.verifyWith(getSecretKey())
					.build()
					.parseSignedClaims(Token)
					.getPayload();
			
			return Long.valueOf(claims.getSubject());
		}
		
	
	
}
