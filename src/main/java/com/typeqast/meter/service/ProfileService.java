package com.typeqast.meter.service;

import com.typeqast.meter.dto.ProfileReadingDto;
import com.typeqast.meter.validation.EvaluableRule;

import java.util.List;
import java.util.Map;


public interface ProfileService {

    Map<EvaluableRule, List<ProfileReadingDto>> processProfiles(List<ProfileReadingDto> profileReadingDtos);
    List<ProfileReadingDto> retrieveAllProfileRations();
    boolean deleteAllProfileRatios();

}
