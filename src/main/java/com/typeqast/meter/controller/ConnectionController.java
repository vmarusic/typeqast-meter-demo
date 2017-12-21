package com.typeqast.meter.controller;

import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.dto.MonthlyConsumptionReqParams;
import com.typeqast.meter.dto.ResponsePayload;
import com.typeqast.meter.service.ConnectionService;
import com.typeqast.meter.validation.EvaluableRule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.typeqast.meter.dto.ResponsePayload.*;
import static com.typeqast.meter.utils.LegacyConversionUtil.convertLegacyDateToMillis;

@RestController
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/connections",
            consumes = {"text/csv", MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Process and create/update new meter readings")
    public @ResponseBody
    ResponsePayload processConnectionReadings(@RequestBody List<MeterReadingDto> readings) {

        Map<EvaluableRule, List<MeterReadingDto>> invalidReadings = connectionService.processConnectionReadings(readings);
        int invalidReadingsSize = invalidReadings.values()
                .stream()
                .mapToInt(List::size)
                .sum();

        if (invalidReadings.isEmpty()) {
            return SUCCESS;
        } else if (readings.size() > invalidReadingsSize){
            return PARTIAL_SUCCESS.withPayload("invalidReadings", invalidReadings);
        } else {

            return FAILURE.withPayload("invalidReadings", invalidReadings);
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/connections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all stored meter readings")
    public @ResponseBody ResponsePayload retrieveMeterReadings() {
        return SUCCESS.withPayload("profiles", connectionService.retrieveAllMeterReadings());
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/connections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete all stored meter readings")
    public @ResponseBody ResponsePayload deleteMeterReadings() {
        return SUCCESS.withPayload("deleted", connectionService.deleteAllMeterReadings());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/connections/consumption",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get meter consumption for a supplied month")
    public @ResponseBody
    ResponsePayload getMonthlyConsumptionForConnection(@ModelAttribute MonthlyConsumptionReqParams params) {

        Optional<BigDecimal> consumptionValue = connectionService.monthlyConsumptionForConnection(
                params.getConnectionId(),
                convertLegacyDateToMillis(params.getMonth()));

        if (consumptionValue.isPresent()) {
            return SUCCESS.withPayload("consumption", consumptionValue.get());
        } else {
            return FAILURE;
        }

    }


}
