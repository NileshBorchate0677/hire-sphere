package com.hiresphere.hiresphere.Auth.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Entity
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String refreshToken;
	
	@CreationTimestamp
	private LocalDateTime lastCreatedAt;
	
	@ManyToOne
	private Users user;

	
}
