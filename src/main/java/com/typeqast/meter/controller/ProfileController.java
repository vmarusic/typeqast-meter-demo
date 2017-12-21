package com.typeqast.meter.controller;

import com.typeqast.meter.dto.ProfileReadingDto;
import com.typeqast.meter.dto.ResponsePayload;
import com.typeqast.meter.service.ProfileService;
import com.typeqast.meter.validation.EvaluableRule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.typeqast.meter.dto.ResponsePayload.*;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {"text/csv", MediaType.APPLICATION_JSON_VALUE},
            value = "/profiles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Process and create/update new profile ratios")
    public @ResponseBody ResponsePayload processProfiles(@RequestBody List<ProfileReadingDto> profileReadings) {


        Map<EvaluableRule,List<ProfileReadingDto>> invalidProfiles = profileService.processProfiles(profileReadings);
        int invalidProfilesSize = invalidProfiles.values()
                .stream()
                .mapToInt(List::size)
                .sum();


        if (invalidProfiles.isEmpty()) {
            return SUCCESS;
        } else if (profileReadings.size() > invalidProfilesSize){
            return PARTIAL_SUCCESS.withPayload("invalidProfiles", invalidProfiles);
        } else {
            return FAILURE.withPayload("invalidProfiles", invalidProfiles);
        }

    }
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/profiles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all stored profile ratios")
    public @ResponseBody ResponsePayload retrieveProfiles() {
        return SUCCESS.withPayload("profiles", profileService.retrieveAllProfileRations());
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/profiles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete all stored profile ratios")
    public @ResponseBody ResponsePayload deleteProfiles() {
        return SUCCESS.withPayload("deleted", profileService.deleteAllProfileRatios());
    }

}
