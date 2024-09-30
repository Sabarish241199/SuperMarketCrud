package com.api.supermarketcrud.repository;

import com.api.supermarketcrud.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryForBatch extends JpaRepository<Batch, Integer> {

}
