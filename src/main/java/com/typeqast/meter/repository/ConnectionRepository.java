package com.typeqast.meter.repository;

import com.typeqast.meter.domain.Connection;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConnectionRepository extends CrudRepository<Connection,Long>{

    Optional<Connection> findByExternalId(String externalId);
}
