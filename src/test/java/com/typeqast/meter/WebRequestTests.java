package com.typeqast.meter;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MeterDemoApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebRequestTests {

    @Autowired
    MockMvc mockMvc;



    @Test
    public void testAProfilePartialSuccessRequestTest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/profiles")
                .contentType("text/csv")
                .content("JAN,A,0.2\n" +
                        "FEB,A,0.9\n" +
                        "JAN,A,0.2\n" +
                        "JAN,B,0.1\n" +
                        "FEB,B,0.1\n" +
                        "MAR,B,0.1\n" +
                        "APR,B,0.1\n" +
                        "MAY,B,0.1\n" +
                        "JUN,B,0.05\n" +
                        "JUL,B,0.05\n" +
                        "AUG,B,0.05\n" +
                        "SEP,B,0.05\n" +
                        "OCT,B,0.1\n" +
                        "NOV,B,0.1\n" +
                        "DEC,B,0.1\n");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Partially successful")));

    }

    @Test
    public void testASuccessRequestTest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/profiles")
                .contentType("text/csv")
                .content("" +
                        "JAN,B,0.1\n" +
                        "FEB,B,0.1\n" +
                        "MAR,B,0.1\n" +
                        "APR,B,0.1\n" +
                        "MAY,B,0.1\n" +
                        "JUN,B,0.05\n" +
                        "JUL,B,0.05\n" +
                        "AUG,B,0.05\n" +
                        "SEP,B,0.05\n" +
                        "OCT,B,0.1\n" +
                        "NOV,B,0.1\n" +
                        "DEC,B,0.1\n");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Success")));

    }
    @Test
    public void testProfileFailureRequest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/profiles")
                .contentType("text/csv")
                .content("JAN,A,1.2");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Failure")));

    }

    @Test
    public void testBConnectionPartialSuccessRequest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/connections")
                .contentType("text/csv")
                .content("0002,A,JAN,1\n" +
                        "0002,C,JAN,1\n" +
                        "0001,B,JAN,1\n" +
                        "0001,B,FEB,2\n" +
                        "0001,B,MAR,3\n" +
                        "0001,B,APR,4\n" +
                        "0001,B,MAY,5\n" +
                        "0001,B,JUN,5.5\n" +
                        "0001,B,JUL,6\n" +
                        "0001,B,AUG,6.5\n" +
                        "0001,B,SEP,7\n" +
                        "0001,B,OCT,8\n" +
                        "0001,B,NOV,9\n" +
                        "0001,B,DEC,10\n");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Partially successful")));
    }

    @Test
    public void testConnectionFailureRequest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/connections")
                .contentType("text/csv")
                .content("0002,A,JAN,1");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Failure")));
    }

    @Test
    public void testBConnectionSuccessRequest() throws Exception{
        RequestBuilder req = MockMvcRequestBuilders
                .post("/connections")
                .contentType("text/csv")
                .content("0001,B,JAN,1\n" +
                        "0001,B,FEB,2\n" +
                        "0001,B,MAR,3\n" +
                        "0001,B,APR,4\n" +
                        "0001,B,MAY,5\n" +
                        "0001,B,JUN,5.5\n" +
                        "0001,B,JUL,6\n" +
                        "0001,B,AUG,6.5\n" +
                        "0001,B,SEP,7\n" +
                        "0001,B,OCT,8\n" +
                        "0001,B,NOV,9\n" +
                        "0001,B,DEC,10\n");

        mockMvc
                .perform(req)
                .andExpect(jsonPath("$.message", is("Success")));
    }

    @Test
    public void testZRetrieveConsumption() throws Exception {
        RequestBuilder req = MockMvcRequestBuilders.
                get("/connections/consumption")
                .param("month","FEB")
                .param("connectionId","0001");

        mockMvc.perform(req)
                .andExpect(jsonPath("$.message", is("Success")));

    }
}
