package com.weitheshinobi.fakewootalk.websocket.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

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