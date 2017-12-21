package com.typeqast.meter.service.impl;

import com.typeqast.meter.domain.Connection;
import com.typeqast.meter.domain.ConnectionReading;
import com.typeqast.meter.domain.Profile;
import com.typeqast.meter.domain.factory.ConnectionFactory;
import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.repository.ConnectionReadingRepository;
import com.typeqast.meter.repository.ConnectionRepository;
import com.typeqast.meter.repository.ProfileRepository;
import com.typeqast.meter.service.ConnectionService;
import com.typeqast.meter.validation.EvaluableRule;
import com.typeqast.meter.validation.MeterReadingRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.typeqast.meter.utils.ValidationUtils.validate;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    ConnectionReadingRepository connectionReadingRepository;

    @Override
    public Map<EvaluableRule, List<MeterReadingDto>> processConnectionReadings(List<MeterReadingDto> connectionReadings) {

        //Remove duplicates as redundant

        Set<MeterReadingDto> connectionsDistinct = new HashSet<>(connectionReadings);

        //Validate Readings

        Map<Optional<EvaluableRule>, List<Map.Entry<String, List<MeterReadingDto>>>> validatedReadings = connectionsDistinct.stream()
                .collect(Collectors.groupingBy(MeterReadingDto::getConnectionId))
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        dp -> validate(MeterReadingRules.values(), dp.getValue())
                ));


        //Get only valid meter readings and persist them
        return validatedReadings.entrySet()
                .stream()
                .flatMap(vr -> {
                    if (!vr.getKey().isPresent()) {
                        vr.getValue()
                                .forEach(r -> {
                                    String profileName = r.getValue().get(0).getProfileBelongsTo();
                                    String connectionId = r.getKey();
                                    Profile profile = profileRepository.findByName(profileName).get();
                                    Connection connection = connectionRepository.
                                            findByExternalId(connectionId).orElse(new Connection(connectionId, profile));
                                    r.getValue().forEach(p -> {
                                        ConnectionReading connectionReading = ConnectionFactory.fromDto(connection,p);
                                        connectionReadingRepository.save(
                                                ConnectionFactory.updateConnectionReading(
                                                        connectionReadingRepository.findByConnection_ExternalIdAndTimeTaken(connectionId,connectionReading.getTimeTaken()),
                                                        connectionReading
                                                ));
                                         }
                                    );
                                });
                        return Stream.empty();
                    } else {
                        return vr.getValue()
                                .stream()
                                .collect(Collectors.toMap(
                                        it -> vr.getKey().get(),
                                        Map.Entry::getValue,
                                        (it1, it2) -> {
                                            it1.addAll(it2);
                                            return it1;
                                        }
                                )).entrySet()
                                .stream();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

    }


    @Override
    public Optional<BigDecimal> monthlyConsumptionForConnection(String connectionId, Long time) {

        Long timeFrom = Instant.ofEpochMilli(time)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime().minusMonths(1)
                .with(lastDayOfMonth())
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        return connectionReadingRepository.findByConnection_ExternalIdAndTimeTaken(connectionId, time)
                .map(cr -> {
                        Optional<ConnectionReading> opt = connectionReadingRepository.findByConnection_ExternalIdAndTimeTaken(connectionId, timeFrom);
                        return (opt.isPresent() ? cr.getValue().subtract(opt.get().getValue()) : cr.getValue());
                        }
                );
    }

    @Override
    public List<MeterReadingDto> retrieveAllMeterReadings() {
        List<MeterReadingDto> meterReadings = new ArrayList<>();

        connectionReadingRepository.findAll().forEach(cr -> {
            meterReadings.add(ConnectionFactory.toDto(cr));
        });

        return meterReadings;
    }

    @Override
    public boolean deleteAllMeterReadings() {
        connectionReadingRepository.findAll().forEach(cr -> {
            cr.setConnection(null);
            connectionReadingRepository.delete(cr);
        });
        return true;
    }
}
