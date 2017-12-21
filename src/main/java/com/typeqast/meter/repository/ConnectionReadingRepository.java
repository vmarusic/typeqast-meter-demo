package com.typeqast.meter.repository;

import com.typeqast.meter.domain.ConnectionReading;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConnectionReadingRepository extends CrudRepository<ConnectionReading,Long> {

    Optional<ConnectionReading> findByConnection_ExternalIdAndTimeTaken(String externalId, Long timeTaken);

}
