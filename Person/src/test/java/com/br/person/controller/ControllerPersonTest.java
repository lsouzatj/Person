package com.br.person.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.br.person.ApiPath;
import com.br.person.entity.EntityPerson;
import com.br.person.service.ServicePerson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ControllerPersonTest {

	private static final String  NAME             = "Michael Jackson";
	private static final int      AGE             = 31;
	private static final int DOCUMENT             = 1234567890;
	private static final int NONEXISTENT_DOCUMENT = 123456789;

	
	@MockBean
	ServicePerson servicePerson;
	
	@Autowired
	MockMvc mvc;
	
	@Before
	public void setUp() {
		BDDMockito.given(servicePerson.savePerson(Mockito.any())).willReturn(new EntityPerson(1L, 
																							  NAME,
																							  AGE,
																							  DOCUMENT));
		
	}
	
	public String getJsonPayLoad() throws JsonProcessingException {
		EntityPerson entityPerson= new EntityPerson();
		ObjectMapper mappJson = new ObjectMapper();
		
		entityPerson.setName(NAME);
		entityPerson.setAge(AGE);
		entityPerson.setDocument(DOCUMENT);
		
		return mappJson.writeValueAsString(entityPerson);
	}
	
	public String getJsonPayLoadError() throws JsonProcessingException {
		EntityPerson entityPerson= new EntityPerson();
		ObjectMapper mappJson = new ObjectMapper();
		
		entityPerson.setName("MJ");
		entityPerson.setAge(AGE);
		entityPerson.setDocument(DOCUMENT);
		
		return mappJson.writeValueAsString(entityPerson);
	}
	
	public String getJsonPayLoadUpdate() throws JsonProcessingException {
		EntityPerson entityPerson= new EntityPerson();
		ObjectMapper mappJson = new ObjectMapper();
		
		entityPerson.setName(NAME);
		entityPerson.setAge(AGE + 1);
		entityPerson.setDocument(DOCUMENT);
		
		return mappJson.writeValueAsString(entityPerson);
	}
	
	@Test
	public void testSavePersonOk() throws JsonProcessingException, Exception {
		mvc.perform(MockMvcRequestBuilders
				.post(ApiPath.ROOT.concat(ApiPath.SAVE_PERSON))
				.content(this.getJsonPayLoad())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.data.id").exists())
		.andExpect(jsonPath("$.data.name").value(NAME))
		.andExpect(jsonPath("$.data.age").value(AGE))
		.andExpect(jsonPath("$.data.document").value(DOCUMENT));
	}
	
	@Test
	public void testSavePersonError() throws JsonProcessingException, Exception {
		mvc.perform(MockMvcRequestBuilders
				.post(ApiPath.ROOT.concat(ApiPath.SAVE_PERSON))
				.content(this.getJsonPayLoadError())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

	}
	
	@Test
	public void testFindByDocument() throws Exception {
		BDDMockito.given(servicePerson.findByDocument(Mockito.anyInt())).willReturn(Optional.of(new EntityPerson(1L,
																												 NAME,
																												 AGE,
																												 DOCUMENT)));
		
		mvc.perform(MockMvcRequestBuilders
				.get(ApiPath.ROOT.concat(ApiPath.FIND_PERSON.concat("?document=").concat(String.valueOf(DOCUMENT))))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testFindByDocumentNotFound() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get(ApiPath.ROOT.concat(ApiPath.FIND_PERSON.concat("?document=").concat(String.valueOf(NONEXISTENT_DOCUMENT))))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		BDDMockito.given(servicePerson.deletePerson(Mockito.anyInt())).willReturn(true);

		mvc.perform(MockMvcRequestBuilders
				.delete(ApiPath.ROOT.concat(ApiPath.DELETE_PERSON.concat("?document=").concat(String.valueOf(DOCUMENT))))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testDeletePersonNonexistent() throws Exception {
		BDDMockito.given(servicePerson.deletePerson(Mockito.anyInt())).willReturn(false);

		mvc.perform(MockMvcRequestBuilders
				.delete(ApiPath.ROOT.concat(ApiPath.DELETE_PERSON.concat("?document=").concat(String.valueOf(NONEXISTENT_DOCUMENT))))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void testUpdatePersonOk() throws Exception {
		BDDMockito.given(servicePerson.updatePerson(Mockito.any(), Mockito.anyInt())).willReturn(true);
		
		mvc.perform(MockMvcRequestBuilders
				.put(ApiPath.ROOT.concat(ApiPath.UPDATE_PERSON.concat("?document=").concat(String.valueOf(DOCUMENT))))
				.content(this.getJsonPayLoadUpdate())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errors").doesNotExist())
		.andExpect(jsonPath("$.data").value("Updated recoverd."))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdatePersonNotFound() throws Exception {
		BDDMockito.given(servicePerson.updatePerson(Mockito.any(), Mockito.anyInt())).willReturn(false);
		
		mvc.perform(MockMvcRequestBuilders
				.put(ApiPath.ROOT.concat(ApiPath.UPDATE_PERSON.concat("?document=").concat(String.valueOf(DOCUMENT))))
				.content(this.getJsonPayLoadUpdate())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.data").doesNotExist())
		.andExpect(jsonPath("$.errors").value("No record for updated."))
		.andExpect(status().isNoContent());
	}
}
