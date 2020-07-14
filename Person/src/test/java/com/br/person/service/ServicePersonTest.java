package com.br.person.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.person.entity.EntityPerson;
import com.br.person.repository.RepositoryPerson;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ServicePersonTest {

	private static final String  NAME           = "Michael Jackson";
	private static final int      AGE           = 31;
	private static final int DOCUMENT           = 1234567890;
	private static final int NONEXISTENT_DOCUMENT =12345678;
	
	@MockBean
	RepositoryPerson repositoryPerson;
	
	@Autowired
	ServicePerson servicePerson;
	
	EntityPerson entityPerson;
	
	@Before
	public void setUp() {
		entityPerson = new EntityPerson();
		
		entityPerson.setName(NAME);
		entityPerson.setAge(AGE);
		entityPerson.setDocument(DOCUMENT);
		
		BDDMockito.given(repositoryPerson.save(Mockito.any())).willReturn(entityPerson);
		BDDMockito.given(repositoryPerson.findByDocument(Mockito.anyInt())).willReturn(Optional.of(entityPerson));
	}
	
	@Test
	public void testServiceSavePerson() {
		EntityPerson responseSaved =  servicePerson.savePerson(entityPerson);
		
		assertNotNull(responseSaved);
		assertEquals(NAME, responseSaved.getName());
		assertEquals(AGE, responseSaved.getAge());
		assertEquals(DOCUMENT, responseSaved.getDocument());
	}
	
	@Test
	public void testServiceFindDocument() {
		Optional<EntityPerson> responseFind = servicePerson.findByDocument(DOCUMENT);
		
		assertTrue(responseFind.isPresent());
		assertEquals(NAME, responseFind.get().getName());
		assertEquals(AGE, responseFind.get().getAge());
		assertEquals(DOCUMENT, responseFind.get().getDocument());
	}
	
	@Test
	public void testDeletePersonReturnTrue() {
		boolean responseDelete = servicePerson.deletePerson(DOCUMENT);
		
		assertTrue(responseDelete);
	}
	
	@Test
	public void testDeletePersonReturnFalse() {
		BDDMockito.given(repositoryPerson.findByDocument(Mockito.anyInt())).willReturn(Optional.empty());
		
		boolean responseDelete = servicePerson.deletePerson(NONEXISTENT_DOCUMENT);
		
		assertFalse(responseDelete);
	}
	
	@Test
	public void testUpdatePerson() {
		EntityPerson newPerson = new EntityPerson();
		
		newPerson.setName(NAME);
		newPerson.setAge(AGE + 1);
		newPerson.setDocument(DOCUMENT);
		
		boolean responseFinal = servicePerson.updatePerson(newPerson, DOCUMENT);
		
		assertTrue(responseFinal);
	}
	
	@Test
	public void testUpdatePersonNotFound() {
		BDDMockito.given(repositoryPerson.findByDocument(Mockito.anyInt())).willReturn(Optional.empty());

		EntityPerson newPerson = new EntityPerson();
		
		newPerson.setName(NAME);
		newPerson.setAge(AGE + 1);
		newPerson.setDocument(DOCUMENT);
		
		boolean responseFinal = servicePerson.updatePerson(newPerson, NONEXISTENT_DOCUMENT);
		
		assertFalse(responseFinal);
	}
}
