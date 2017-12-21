package com.typeqast.meter.service;

import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.validation.EvaluableRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConnectionService {

    Map<EvaluableRule, List<MeterReadingDto>> processConnectionReadings(List<MeterReadingDto> connectionReadings);
    List<MeterReadingDto> retrieveAllMeterReadings();
    boolean deleteAllMeterReadings();

    Optional<BigDecimal> monthlyConsumptionForConnection(String connectionId, Long time);
}
