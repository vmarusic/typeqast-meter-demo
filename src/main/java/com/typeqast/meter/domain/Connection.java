package com.typeqast.meter.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Connection {

    @Column(name = "connection_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long internalId;

    @Column(name = "connection_external_id")
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "connection", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ConnectionReading> connectionReadings;

    public Long getInternalId() {
        return internalId;
    }

    public Connection(String externalId, Profile profile) {
        this.externalId = externalId;
        this.profile = profile;
    }

    public Connection() {
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<ConnectionReading> getConnectionReadings() {
        return connectionReadings;
    }

    public void setConnectionReadings(Set<ConnectionReading> connectionReadings) {
        this.connectionReadings = connectionReadings;
    }
}
