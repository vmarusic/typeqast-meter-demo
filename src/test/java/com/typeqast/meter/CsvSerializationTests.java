package com.typeqast.meter;

import com.typeqast.meter.controller.converter.CsvHttpMessageConverter;
import com.typeqast.meter.dto.MeterReadingDto;
import com.typeqast.meter.dto.ProfileReadingDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CsvSerializationTests {

    private CsvHttpMessageConverter converter;

    private MockHttpOutputMessage outputMessage;

    private MediaType mediaType;


    @Before
    public void setUp() {
        this.converter = new CsvHttpMessageConverter();
        this.outputMessage = new MockHttpOutputMessage();
        this.mediaType = new MediaType("text", "csv");
    }


    @Test
    public void canRead() {
        assertTrue(this.converter.canRead(String.class, this.mediaType));
    }

    @Test
    public void canWrite() {
        assertTrue(this.converter.canWrite(String.class, this.mediaType));
        assertTrue(this.converter.canWrite(String.class, MediaType.ALL));
    }

    @Test
    public void testProfileSerialization() throws IOException {

        Object result = mockSerializationForObject("JAN,testProfile,2.3", ProfileReadingDto.class);

        assertTrue(result instanceof ProfileReadingDto);
    }

    @Test
    public void testMeterSerialization() throws IOException {
        Object result = mockSerializationForObject("0001,testProfile,JAN,140", MeterReadingDto.class);

        assertTrue(result instanceof MeterReadingDto);
    }

    private Object mockSerializationForObject(String serialization, Class forObject) throws IOException{
        MockHttpInputMessage inputMessage = new MockHttpInputMessage(serialization.getBytes(StandardCharsets.UTF_8));
        inputMessage.getHeaders().setContentType(new MediaType("text","csv"));
        return this.converter.read(forObject, inputMessage);
    }

}
