package com.br.person.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PersonDTO {

	private Long id;
	
	@NotBlank
	@Length(min = 3, max = 50, message = "Inform the name bettwen 3 and 50 characters.")
	private String name;
	
	@NotNull
	@Min(value = 1)
	@Max(value = 999)
	private Integer age;
	
	@NotNull
	private int document;
	
}
