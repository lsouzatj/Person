package com.br.person.controller;

import java.util.Arrays;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.person.ApiPath;
import com.br.person.dto.PersonDTO;
import com.br.person.entity.EntityPerson;
import com.br.person.response.Response;
import com.br.person.service.ServicePerson;

@RestController
@RequestMapping(ApiPath.ROOT)
public class ControllerPerson {

	@Autowired
	private ServicePerson servicePerson;
	
	@GetMapping(ApiPath.FIND_PERSON)
	public ResponseEntity<Response<Optional<EntityPerson>>> findPerson(@RequestParam(name = "document", required = true ) int document) {
		Response<Optional<EntityPerson>> finalFindResponse = new Response<>();
		
		Optional<EntityPerson> responseFind = servicePerson.findByDocument(document);
		
		if (responseFind.isPresent()) {
			finalFindResponse.setData(responseFind);
		}else {
			finalFindResponse.getErrors();
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(finalFindResponse);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(finalFindResponse);
	}
	
	@DeleteMapping(ApiPath.DELETE_PERSON)
	public ResponseEntity<Response<String>> deletePerson(@RequestParam(name = "document", required = true) int document){
		Response<String> finalDeletingResponse = new Response<>();
		
		if (!servicePerson.deletePerson(document)) {
			finalDeletingResponse.setErrors(Arrays.asList("No record for deletion."));
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(finalDeletingResponse);
		}
		
		finalDeletingResponse.setData("Deleted recoverd.");
		return ResponseEntity.status(HttpStatus.OK).body(finalDeletingResponse);
	}
	
	@PostMapping(ApiPath.SAVE_PERSON)
	public ResponseEntity<Response<PersonDTO>> savePerson(@Valid @RequestBody PersonDTO personDTO,
														  BindingResult result) {
		Response<PersonDTO> finalSaveResponse = new Response<>();
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(erro -> finalSaveResponse.getErrors().add(erro.getDefaultMessage()));
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(finalSaveResponse);
		}
		
		EntityPerson responseSaved = servicePerson.savePerson(this.convertDtoToEntity(personDTO));
		
		finalSaveResponse.setData(this.convertEntityToDto(responseSaved));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(finalSaveResponse);
	}
	
	@PutMapping(ApiPath.UPDATE_PERSON)
	public ResponseEntity<Response<String>> updatePerson(@Valid @RequestBody PersonDTO personDTO,
														 @RequestParam(name = "document", required = true) int document){
		Response<String> finalUpdatedResponse = new Response<>();
		
		if (!servicePerson.updatePerson(this.convertDtoToEntity(personDTO), document)) {
			finalUpdatedResponse.setErrors(Arrays.asList("No record for updated."));
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(finalUpdatedResponse);
		}
		
		finalUpdatedResponse.setData("Updated recoverd.");
		return ResponseEntity.status(HttpStatus.OK).body(finalUpdatedResponse);
	}
	
	private EntityPerson convertDtoToEntity(PersonDTO personDTO) {
		EntityPerson entityPerson = new EntityPerson();
		
		entityPerson.setId(personDTO.getId());
		entityPerson.setName(personDTO.getName());
		entityPerson.setAge(personDTO.getAge());
		entityPerson.setDocument(personDTO.getDocument());
		
		return entityPerson;
	}
	
	private PersonDTO convertEntityToDto(EntityPerson entityPerson) {
		PersonDTO personDTO = new PersonDTO();
		
		personDTO.setId(entityPerson.getId());
		personDTO.setName(entityPerson.getName());
		personDTO.setAge(entityPerson.getAge());
		personDTO.setDocument(entityPerson.getDocument());
		
		return personDTO;
	}
}
