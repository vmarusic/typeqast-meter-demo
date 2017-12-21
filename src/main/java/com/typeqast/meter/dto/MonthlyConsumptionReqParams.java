package com.typeqast.meter.dto;

public class MonthlyConsumptionReqParams {

    private String connectionId;

    private String month;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
