package com.typeqast.meter.domain;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Profile {

    @Column(name = "profile_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_name")
    private String name;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Connection> connections;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ProfileRatio> profileRatios;

    public Profile(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProfileRatio> getProfileRatios() {
        return profileRatios;
    }

    public void setProfileRatios(Set<ProfileRatio> profileRatios) {
        this.profileRatios = profileRatios;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public void setConnections(Set<Connection> connections) {
        this.connections = connections;
    }

    public Profile() {
    }
}
