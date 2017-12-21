package com.typeqast.meter.validation;

import com.fasterxml.jackson.annotation.JsonValue;
import com.typeqast.meter.dto.ProfileReadingDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public enum ProfileRules implements EvaluableRule<List<ProfileReadingDto>> {

    IS_RATIO_1_0("Business rule violated: Profile ratios don't accumulate to 1.0 value") {
        @Override
        public boolean eval(List<ProfileReadingDto> profileReadingDtos) {
            AtomicReference<BigDecimal> allRatios = new AtomicReference<>();
            allRatios.set(new BigDecimal(0));
            profileReadingDtos
                    .forEach(v -> allRatios.getAndUpdate(p -> p.add(v.getRatio())));

            return (allRatios.get().compareTo(new BigDecimal(1.0)) == 0);
        }
    };
    private String validationMessage;

    ProfileRules(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    @JsonValue
    public String getName() {
        return this.validationMessage;
    }


}
