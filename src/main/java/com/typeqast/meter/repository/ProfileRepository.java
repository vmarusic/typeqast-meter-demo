package com.typeqast.meter.repository;

import com.typeqast.meter.domain.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long>{

    Optional<Profile> findByName(String name);

}
