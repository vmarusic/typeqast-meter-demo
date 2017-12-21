package com.typeqast.meter.repository;

import com.typeqast.meter.domain.ProfileRatio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRatioRepository extends CrudRepository<ProfileRatio,Long>  {

    Optional<ProfileRatio> findByProfile_NameAndAndApplicableTime(String profile_name, Long applicableTime);


}
