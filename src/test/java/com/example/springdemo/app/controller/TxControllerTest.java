package com.example.springdemo.app.controller;

import com.example.springdemo.app.service.ListService;
import com.example.springdemo.app.service.TxService;
import com.example.springdemo.config.db.PostgresConfig;
import com.example.springdemo.config.db.SecondConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class) // junit run with spring boot test
@WebMvcTest(TxController.class)
@AutoConfigureDataJdbc// for autowired jdbcTemplate
@AutoConfigureDataJpa// for autowired entityManager
@Import({PostgresConfig.class, SecondConfig.class})
public class TxControllerTest {

    @Autowired
    private MockMvc mvc; // MockMvc autoconfigured by @WebMvcTest

    @MockBean // provide mock implementation for dependencies
    private TxService txService;

    @MockBean
    private ListService listService;

    @Test
    public void testExample() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/tx/updateTwoDatabase")).andReturn();
        MockHttpServletResponse response = result.getResponse();
        // System.out.println("updateTwoDatabase response status" + response.getStatus());
        Assert.assertEquals(200, response.getStatus());
    }
}
