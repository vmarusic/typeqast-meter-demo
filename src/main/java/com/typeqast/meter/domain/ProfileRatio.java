package com.typeqast.meter.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ProfileRatio {

    @Column(name = "profile_ratio_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "applicable_time")
    private Long applicableTime;

    @Column(name = "ratio")
    private BigDecimal ratio;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Profile profile;

    public ProfileRatio(Long applicableTime, BigDecimal ratio, Profile profile) {
        this.applicableTime = applicableTime;
        this.ratio = ratio;
        this.profile = profile;
    }

    public ProfileRatio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicableTime() {
        return applicableTime;
    }

    public void setApplicableTime(Long applicableTime) {
        this.applicableTime = applicableTime;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
