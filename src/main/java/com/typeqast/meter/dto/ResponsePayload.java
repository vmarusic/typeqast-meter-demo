package com.typeqast.meter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponsePayload {

    SUCCESS("1.0", "Success"),
    PARTIAL_SUCCESS("1.0","Partially successful"),
    FAILURE("1.0", "Failure");

    private final String version;
    private final String message;
    private Map<String,Object> payload;

    public String getVersion() {
        return version;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    ResponsePayload(String version, String message) {
        this.version = version;
        this.message = message;
    }
    public ResponsePayload withPayload(String key, Object payloadObject) {
        Map<String,Object> payload = new ConcurrentHashMap<>();
        payload.put(key, payloadObject);
        this.payload = payload;
        return this;
    }
}
