package com.typeqast.meter;


import com.typeqast.meter.domain.Profile;
import com.typeqast.meter.domain.ProfileRatio;
import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.dto.ProfileReadingDto;
import com.typeqast.meter.repository.ProfileRatioRepository;
import com.typeqast.meter.repository.ProfileRepository;
import com.typeqast.meter.validation.MeterReadingRules;
import com.typeqast.meter.validation.ProfileRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.typeqast.meter.utils.LegacyConversionUtil.convertLegacyDateToMillis;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ValidationTests {

    private ProfileRepository profileRepositoryMock;

    private ProfileRatioRepository profileRatioRepositoryMock;

    @Before
    public void setUp() {


        profileRepositoryMock = mock(ProfileRepository.class);
        profileRatioRepositoryMock = mock(ProfileRatioRepository.class);

    }

    @Test
    public void testCorrectProfileRatio() {
        assertTrue(ProfileRules.IS_RATIO_1_0.eval(validProfileData()));
    }

    @Test
    public void testIncorrectProfileRatio() {
        assertFalse(ProfileRules.IS_RATIO_1_0.eval(invalidProfileData()));
    }

    @Test
    public void testCorrectMeterReadingSequence() {
        assertTrue(MeterReadingRules.ARE_IN_SEQUENCE.eval(validMeterData()));
    }

    @Test
    public void testProfileExistsForReading() {
        Profile validProfile = new Profile();
        validProfile.setName("A");
        when(profileRepositoryMock.findByName(anyString())).thenReturn(Optional.of(validProfile));
        MeterReadingRules.HAS_PROFILE.setProfileRepository(profileRepositoryMock);

        assertTrue(MeterReadingRules.HAS_PROFILE.eval(validMeterData()));
    }

    @Test
    public void testReadingIsWithinRatio() {
        Profile validProfile = new Profile();
        validProfile.setName("A");
        when(profileRepositoryMock.findByName(anyString())).thenReturn(Optional.of(validProfile));

        ProfileRatio validProfileRatio = new ProfileRatio();
        validProfileRatio.setProfile(validProfile);
        validProfileRatio.setApplicableTime(convertLegacyDateToMillis("JAN"));
        validProfileRatio.setRatio(new BigDecimal("1.0"));

        when(profileRatioRepositoryMock
                .findByProfile_NameAndAndApplicableTime(validProfile.getName(), convertLegacyDateToMillis("JAN"))
        ).thenReturn(Optional.of(validProfileRatio));

        MeterReadingRules.IS_WITHIN_RATIO.setProfileRatioRepository(profileRatioRepositoryMock);
        MeterReadingRules.IS_WITHIN_RATIO.setProfileRepository(profileRepositoryMock);
        MeterReadingDto[] list = {
                new MeterReadingDto("0001","A","JAN",new BigDecimal("1"))};


        assertTrue(MeterReadingRules.IS_WITHIN_RATIO.eval(Arrays.asList(list)));

    }


    private List<ProfileReadingDto> validProfileData() {
        ProfileReadingDto[] list = {
                new ProfileReadingDto("A","JAN", new BigDecimal("0.1")),
                new ProfileReadingDto("A","FEB", new BigDecimal("0.1")),
                new ProfileReadingDto("A","MAR", new BigDecimal("0.1")),
                new ProfileReadingDto("A","APR", new BigDecimal("0.1")),
                new ProfileReadingDto("A","MAY", new BigDecimal("0.1")),
                new ProfileReadingDto("A","JUN", new BigDecimal("0.05")),
                new ProfileReadingDto("A","JUL", new BigDecimal("0.05")),
                new ProfileReadingDto("A","AUG", new BigDecimal("0.05")),
                new ProfileReadingDto("A","SEP", new BigDecimal("0.05")),
                new ProfileReadingDto("A","OCT", new BigDecimal("0.1")),
                new ProfileReadingDto("A","NOV", new BigDecimal("0.1")),
                new ProfileReadingDto("A","DEC", new BigDecimal("0.1")),
        };

        return Arrays.asList(list);
    }

    private List<ProfileReadingDto> invalidProfileData() {
        ProfileReadingDto[] list = {
                new ProfileReadingDto("A","JAN", new BigDecimal("0.1")),
                new ProfileReadingDto("A","FEB", new BigDecimal("0.1")),
                new ProfileReadingDto("A","MAR", new BigDecimal("0.1")),
                new ProfileReadingDto("A","APR", new BigDecimal("0.1")),
                new ProfileReadingDto("A","MAY", new BigDecimal("0.1")),
                new ProfileReadingDto("A","JUN", new BigDecimal("0.1")),
                new ProfileReadingDto("A","JUL", new BigDecimal("0.1")),
                new ProfileReadingDto("A","AUG", new BigDecimal("0.1")),
                new ProfileReadingDto("A","SEP", new BigDecimal("0.1")),
                new ProfileReadingDto("A","OCT", new BigDecimal("0.1")),
                new ProfileReadingDto("A","NOV", new BigDecimal("0.1")),
                new ProfileReadingDto("A","DEC", new BigDecimal("0.1")),
        };

        return Arrays.asList(list);
    }

    private List<MeterReadingDto> invalidMeterData() {
        MeterReadingDto[] list = {
                new MeterReadingDto("0001","A","JAN",new BigDecimal("12")),
                new MeterReadingDto("0001","A","FEB",new BigDecimal("11")),
                new MeterReadingDto("0001","A","MAR",new BigDecimal("10")),
                new MeterReadingDto("0001","A","APR",new BigDecimal("9")),
                new MeterReadingDto("0001","A","MAY",new BigDecimal("8")),
                new MeterReadingDto("0001","A","JUN",new BigDecimal("7")),
                new MeterReadingDto("0001","A","JUL",new BigDecimal("6")),
                new MeterReadingDto("0001","A","AUG",new BigDecimal("5")),
                new MeterReadingDto("0001","A","SEP",new BigDecimal("4")),
                new MeterReadingDto("0001","A","OCT",new BigDecimal("3")),
                new MeterReadingDto("0001","A","NOV",new BigDecimal("2")),
                new MeterReadingDto("0001","A","DEC",new BigDecimal("1"))
        };

        return Arrays.asList(list);
    }

    private List<MeterReadingDto> validMeterData() {
        MeterReadingDto[] list = {
                new MeterReadingDto("0001","A","JAN",new BigDecimal("1")),
                new MeterReadingDto("0001","A","FEB",new BigDecimal("2")),
                new MeterReadingDto("0001","A","MAR",new BigDecimal("3")),
                new MeterReadingDto("0001","A","APR",new BigDecimal("4")),
                new MeterReadingDto("0001","A","MAY",new BigDecimal("5")),
                new MeterReadingDto("0001","A","JUN",new BigDecimal("6")),
                new MeterReadingDto("0001","A","JUL",new BigDecimal("7")),
                new MeterReadingDto("0001","A","AUG",new BigDecimal("8")),
                new MeterReadingDto("0001","A","SEP",new BigDecimal("9")),
                new MeterReadingDto("0001","A","OCT",new BigDecimal("10")),
                new MeterReadingDto("0001","A","NOV",new BigDecimal("11")),
                new MeterReadingDto("0001","A","DEC",new BigDecimal("12"))
        };

        return Arrays.asList(list);
    }


}
