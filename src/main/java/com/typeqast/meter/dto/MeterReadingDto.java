package com.typeqast.meter.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Objects;

@JsonPropertyOrder({"connectionId", "profileBelongsTo", "month", "measuerement"})
public class MeterReadingDto {

    private String connectionId;

    private String profileBelongsTo;

    private String month;

    private BigDecimal measuerement;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getProfileBelongsTo() {
        return profileBelongsTo;
    }

    public void setProfileBelongsTo(String profileBelongsTo) {
        this.profileBelongsTo = profileBelongsTo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getMeasuerement() {
        return measuerement;
    }

    public void setMeasuerement(BigDecimal measuerement) {
        this.measuerement = measuerement;
    }

    public MeterReadingDto(String connectionId, String profileBelongsTo, String month, BigDecimal measuerement) {
        this.connectionId = connectionId;
        this.profileBelongsTo = profileBelongsTo;
        this.month = month;
        this.measuerement = measuerement;
    }

    public MeterReadingDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterReadingDto that = (MeterReadingDto) o;
        return Objects.equals(connectionId, that.connectionId) &&
                Objects.equals(profileBelongsTo, that.profileBelongsTo) &&
                Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {

        return Objects.hash(connectionId, profileBelongsTo, month);
    }
}
