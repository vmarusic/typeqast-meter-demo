package com.typeqast.meter.validation;

import com.fasterxml.jackson.annotation.JsonValue;
import com.typeqast.meter.domain.Profile;
import com.typeqast.meter.domain.ProfileRatio;
import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.repository.ProfileRatioRepository;
import com.typeqast.meter.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.typeqast.meter.utils.LegacyConversionUtil.convertLegacyDateToMillis;

@Component
public enum MeterReadingRules implements EvaluableRule<List<MeterReadingDto>> {

    HAS_PROFILE("Business rule violated: Profile does not exist for the reading") {
        @Override
        public boolean eval(List<MeterReadingDto> meterReadingDtos) {
            return meterReadingDtos.stream()
                    .allMatch(m ->
                            profileRepository.findByName(m.getProfileBelongsTo()).isPresent());

        }


    },

    ARE_IN_SEQUENCE("Business rule violated: Reading values are not incrementing through time") {
        @Override
        public boolean eval(List<MeterReadingDto> meterReadingDtos) {

            meterReadingDtos.sort((i1, i2) -> convertLegacyDateToMillis(i1.getMonth())
                    .compareTo(convertLegacyDateToMillis(i2.getMonth())));


            AtomicReference<BigDecimal> val = new AtomicReference<>(BigDecimal.valueOf(Long
                    .MIN_VALUE));
            return meterReadingDtos.stream()
                    .allMatch(m -> m.getMeasuerement().compareTo(val.getAndSet(m.getMeasuerement())) >= 0);

        }
    },
    IS_WITHIN_RATIO("Business rule violated: Reading value does not match profile's ratio") {
        @Override
        public boolean eval(List<MeterReadingDto> meterReadingDtos) {
            Double TOLERANCE = 0.25;

            meterReadingDtos.sort((i1, i2) -> convertLegacyDateToMillis(i1.getMonth())
                    .compareTo(convertLegacyDateToMillis(i2.getMonth())));

            BigDecimal endOfYearMeasurement = meterReadingDtos.get(meterReadingDtos.size() -1).getMeasuerement();

            BigDecimal yearConsumption = endOfYearMeasurement;


            AtomicReference<BigDecimal> val = new AtomicReference<>(BigDecimal.ZERO);
            return meterReadingDtos.stream()
                    .allMatch(m -> {
                        //at this point Ratio should belong to a profile
                        Profile profile = profileRepository.findByName(m.getProfileBelongsTo()).get();
                        ProfileRatio profileRatio = profileRatioRepository
                                .findByProfile_NameAndAndApplicableTime(profile.getName(), convertLegacyDateToMillis(m.getMonth()))
                                .get();
                        BigDecimal ratioConsumption = yearConsumption.multiply(profileRatio.getRatio());
                        BigDecimal consumption = m.getMeasuerement().subtract(val.getAndSet(m.getMeasuerement()));


                        BigDecimal upperbound = ratioConsumption.multiply(BigDecimal.valueOf(1 + TOLERANCE));
                        BigDecimal lowerbound = ratioConsumption.multiply(BigDecimal.valueOf(1 - TOLERANCE));

                        return consumption.compareTo(upperbound) <= 0 && consumption.compareTo(lowerbound) >= 0;

                    });

        }
    };

    private String validationMessage;

    MeterReadingRules(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") String validationMessage) {
        this.validationMessage = validationMessage;
    }

    @JsonValue
    public String getName() {
        return this.validationMessage;
    }



        public ProfileRepository profileRepository;

        public ProfileRatioRepository profileRatioRepository;

        public void setProfileRatioRepository(ProfileRatioRepository profileRatioRepository) {
            this.profileRatioRepository = profileRatioRepository;
        }

        public void setProfileRepository(ProfileRepository profileRepository) {
            this.profileRepository = profileRepository;
        }


        @Component
        public static class ServiceInjector {

            @Autowired
            private ProfileRepository profileRepository;

            @Autowired
            private ProfileRatioRepository profileRatioRepository;

            @PostConstruct
            void inject() {
                EnumSet.allOf(MeterReadingRules.class)
                        .forEach(m -> {
                            m.setProfileRepository(profileRepository);
                            m.setProfileRatioRepository(profileRatioRepository);
                        });
            }
        }


}
