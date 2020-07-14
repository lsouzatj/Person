package com.br.person.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.person.entity.EntityPerson;
import com.sun.tools.sjavac.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryPersonTest {

	private static final String  NAME = "Michael Jackson";
	private static final int      AGE = 31;
	private static final int DOCUMENT = 1234567890;
	
	@Autowired
	RepositoryPerson repositoryPerson;
	
	EntityPerson entityPerson;
	
	@Before
	public void setUp() {
		entityPerson = new EntityPerson();
		
		entityPerson.setName(NAME);
		entityPerson.setAge(AGE);
		entityPerson.setDocument(DOCUMENT);
		
		repositoryPerson.save(entityPerson);
	}
	
	@After
	public void tearDown() {
		repositoryPerson.deleteAll();
	}
	
	@Test
	public void testSavePersonOk() {
		EntityPerson responseSaved = repositoryPerson.save(entityPerson);
		
		assertNotNull(responseSaved);
		assertEquals(NAME, responseSaved.getName());
		assertEquals(AGE, responseSaved.getAge());
		assertEquals(DOCUMENT, responseSaved.getDocument());
	}
	
	@Test
	public void testFindByDocument() {
		Optional<EntityPerson> entityPerson = repositoryPerson.findByDocument(DOCUMENT);
		
		assertTrue(entityPerson.isPresent());
		assertEquals(NAME, entityPerson.get().getName());
		assertEquals(AGE, entityPerson.get().getAge());
		assertEquals(DOCUMENT, entityPerson.get().getDocument());
	}
	
	@Test
	public void testDeletePersonByDocument() {
		Optional<EntityPerson> entityPerson = repositoryPerson.findByDocument(DOCUMENT);
		repositoryPerson.delete(entityPerson.get());
		
		assertFalse(repositoryPerson.findByDocument(DOCUMENT).isPresent());
	}
}
