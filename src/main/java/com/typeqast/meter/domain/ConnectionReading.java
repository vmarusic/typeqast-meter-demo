package com.typeqast.meter.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ConnectionReading {

    @Column(name = "connection_reading_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_taken")
    private Long timeTaken;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "unit")
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Connection connection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ConnectionReading() {
    }

    public ConnectionReading(Long timeTaken, BigDecimal value, String unit, Connection connection) {
        this.timeTaken = timeTaken;
        this.value = value;
        this.unit = unit;
        this.connection = connection;
    }
}
