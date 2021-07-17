package com.weitheshinobi.fakewootalk.websocket.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SecretReceiver.class)
class SecretReceiverTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void receiveSecret() throws Exception {
        String paramName = "secret";
        String testCase = "test";
        String resultCase = (String) mockMvc.perform(post("/sendSecret")
                .param(paramName, testCase))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession()
                .getAttribute(paramName);

        assertEquals(testCase, resultCase);
    }

}