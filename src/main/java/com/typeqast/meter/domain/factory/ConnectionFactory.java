package com.typeqast.meter.domain.factory;

import com.typeqast.meter.domain.Connection;
import com.typeqast.meter.domain.ConnectionReading;
import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.validation.exception.ValidationException;

import java.util.Optional;

import static com.typeqast.meter.utils.LegacyConversionUtil.*;

public class ConnectionFactory {

    public static ConnectionReading fromDto(Connection connection, MeterReadingDto meterReadingDto) {
        return new ConnectionReading(convertLegacyDateToMillis(meterReadingDto.getMonth()),
                meterReadingDto.getMeasuerement(),
                DEFAULT_UNIT_OF_MEASUREMENT,
                connection);
    }

    public static MeterReadingDto toDto(ConnectionReading connectionReading) {
        return new MeterReadingDto(
                connectionReading.getConnection().getExternalId(),
                connectionReading.getConnection().getProfile().getName(),
                convertMillisToLegacyDate(connectionReading.getTimeTaken()),
                connectionReading.getValue()
                );
    }

    public static ConnectionReading updateConnectionReading(Optional<ConnectionReading> oldReading, ConnectionReading newReading) throws ValidationException {
        if (oldReading.isPresent()) {
            ConnectionReading cr = oldReading.get();
            cr.setValue(newReading.getValue());
            return cr;
        } else {
            return newReading;
        }
    }
}
