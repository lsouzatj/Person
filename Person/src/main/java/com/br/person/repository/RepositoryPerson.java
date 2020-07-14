package com.br.person.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.person.entity.EntityPerson;

@Repository
public interface RepositoryPerson extends JpaRepository<EntityPerson, Long>{

	Optional<EntityPerson> findByDocument(int document);
}
