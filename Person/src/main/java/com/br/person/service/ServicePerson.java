package com.br.person.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.person.entity.EntityPerson;
import com.br.person.repository.RepositoryPerson;

@Service
public class ServicePerson {

	@Autowired
	RepositoryPerson repositoryPerson;
	
	public EntityPerson savePerson(EntityPerson entityPerson) {
		return repositoryPerson.save(entityPerson);
	}
	
	public Optional<EntityPerson> findByDocument(int document) {
		return repositoryPerson.findByDocument(document);
	}
	
	public boolean deletePerson(int document) {
		Optional<EntityPerson> personRecord = repositoryPerson.findByDocument(document);
		
		if (!personRecord.isPresent()) {
			return false;
		}
		
		repositoryPerson.delete(personRecord.get());
		return true;
	}
	
	public boolean updatePerson(EntityPerson newPerson, int document) {
		Optional<EntityPerson> personRecord = repositoryPerson.findByDocument(document);
		
		if (!personRecord.isPresent()) {
			return false;
		}
		
		personRecord.get().setName(newPerson.getName());
		personRecord.get().setAge(newPerson.getAge());
		personRecord.get().setDocument(newPerson.getDocument());
		
		repositoryPerson.save(personRecord.get());
		return true;
	}
}
