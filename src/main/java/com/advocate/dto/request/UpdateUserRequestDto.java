package com.advocate.dto.request;

import com.advocate.entity.Address;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class UpdateUserRequestDto {
    
    private Long id;
    
	private String firstName;

	private String lastName;
	
	private String email;

	private String password;

	private String confirmPassword;

	private String mobile;

	private String role; 

	private Address address; 


}
