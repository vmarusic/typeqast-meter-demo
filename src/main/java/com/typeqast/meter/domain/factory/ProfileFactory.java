package com.typeqast.meter.domain.factory;

import com.typeqast.meter.domain.Profile;
import com.typeqast.meter.domain.ProfileRatio;
import com.typeqast.meter.dto.ProfileReadingDto;
import com.typeqast.meter.validation.exception.ValidationException;

import java.util.Optional;

import static com.typeqast.meter.utils.LegacyConversionUtil.convertLegacyDateToMillis;
import static com.typeqast.meter.utils.LegacyConversionUtil.convertMillisToLegacyDate;

public class ProfileFactory {

    public static ProfileRatio fromDto(Profile profile, ProfileReadingDto profileReadingDto) throws ValidationException{
        return new ProfileRatio(convertLegacyDateToMillis(profileReadingDto.getMonth()), profileReadingDto.getRatio(), profile);
    }

    public static ProfileRatio updateProfileRatio(Optional<ProfileRatio> oldProfileRatio, ProfileRatio newProfileRatio) throws ValidationException {
        if (oldProfileRatio.isPresent()) {
            ProfileRatio pr = oldProfileRatio.get();
            pr.setRatio(newProfileRatio.getRatio());
            return pr;
        } else {
            return newProfileRatio;
        }
    }

    public static ProfileReadingDto toDto(ProfileRatio profileRatio) {
        return new ProfileReadingDto(
                profileRatio.getProfile().getName(),
                convertMillisToLegacyDate(profileRatio.getApplicableTime()),
                profileRatio.getRatio());
    }

}
