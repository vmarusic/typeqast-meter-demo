package com.typeqast.meter.service.impl;

import com.typeqast.meter.domain.Profile;
import com.typeqast.meter.domain.ProfileRatio;
import com.typeqast.meter.domain.factory.ProfileFactory;
import com.typeqast.meter.dto.ProfileReadingDto;
import com.typeqast.meter.repository.ProfileRatioRepository;
import com.typeqast.meter.repository.ProfileRepository;
import com.typeqast.meter.service.ProfileService;
import com.typeqast.meter.validation.EvaluableRule;
import com.typeqast.meter.validation.ProfileRules;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.typeqast.meter.utils.ValidationUtils.validate;

@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    private ProfileRatioRepository profileRatioRepository;


    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileRatioRepository profileRatioRepository) {
        this.profileRepository = profileRepository;
        this.profileRatioRepository = profileRatioRepository;
    }

    @Override
    public Map<EvaluableRule, List<ProfileReadingDto>> processProfiles(List<ProfileReadingDto> profileReadings) {

        //Remove duplicates as updates and provide unique profile names

        Collection<ProfileReadingDto> profilesDistinct = profileReadings
                .stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> item,
                        (item1, item2) -> item2))
                .values();

        //Validate Readings
        Map<Optional<EvaluableRule>, List<Map.Entry<String, List<ProfileReadingDto>>>> validatedReadings = profilesDistinct.stream()
                .collect(Collectors.groupingBy(ProfileReadingDto::getProfileName))
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        dp -> validate(ProfileRules.values(), dp.getValue())
                ));

        return validatedReadings.entrySet()
                .stream()
                .flatMap(vr -> {
                    //Get only valid profile readings and persist them
                    if (!vr.getKey().isPresent()) {
                        vr.getValue()
                                .forEach(r -> {
                                    String profileName = r.getKey();
                                    Profile profile = profileRepository
                                            .findByName(profileName).orElse(new Profile(profileName));
                                    r.getValue().forEach(p ->{
                                        ProfileRatio profileRatio = ProfileFactory.fromDto(profile,p);
                                        profileRatioRepository.save(ProfileFactory.updateProfileRatio(
                                                profileRatioRepository.findByProfile_NameAndAndApplicableTime(profileName,profileRatio.getApplicableTime()),
                                                profileRatio
                                        ));
                                            });
                                });
                        return Stream.empty();
                        //Get invalidated items and return them as part of the error report
                    } else {
                        return vr.getValue()
                                .stream()
                                .collect(Collectors.toMap(
                                        it -> vr.getKey().get(),
                                        Map.Entry::getValue,
                                        (it1, it2) -> {
                                            it1.addAll(it2);
                                            return it1;
                                        }
                                )).entrySet()
                                .stream();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    @Override
    public List<ProfileReadingDto> retrieveAllProfileRations() {
        List<ProfileReadingDto> profileRatios = new ArrayList<>();

        profileRatioRepository.findAll().forEach(pr -> {
            profileRatios.add(ProfileFactory.toDto(pr));
        });

        return profileRatios;
    }

    @Override
    public boolean deleteAllProfileRatios() {
        profileRatioRepository.findAll().forEach(profileRatio -> {
            profileRatio.setProfile(null);
            profileRatioRepository.delete(profileRatio);
        });
        return true;
    }
}
