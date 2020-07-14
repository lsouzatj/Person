package com.br.person.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EntityPerson implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4882862391079686083L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(length = 50)
	private String name;
	
	@NotNull
	@Column(length = 3)
	private int age;
	
	@NotNull
	@Column(length = 10)
	private int document;
	
}
