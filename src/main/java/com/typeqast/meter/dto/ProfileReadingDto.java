package com.typeqast.meter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Objects;

@JsonPropertyOrder({"month", "profileName", "ratio"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileReadingDto {

    private String profileName;
    private String month;
    private BigDecimal ratio;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public ProfileReadingDto(String profileName, String month, BigDecimal ratio) {
        this.profileName = profileName;
        this.month = month;
        this.ratio = ratio;
    }

    public ProfileReadingDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileReadingDto that = (ProfileReadingDto) o;
        return Objects.equals(profileName, that.profileName) &&
                Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {

        return Objects.hash(profileName, month);
    }
}
