package com.exchanges.controllers;

import com.exchanges.services.FeignCurrencyClientService;
import com.exchanges.services.FeignGifClientService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ResolveControllerTest {

    @Value(value = "${defaultCurrencyTo}")
    private String defaultCurrencyTo;

    @Value(value = "${uriGetBrokeGif}")
    private String uriGetBrokeGif;

    @Value(value = "${uriGetRichGif}")
    private String uriGetRichGif;

    @Value(value = "${uriGetCurrencyToday}")
    private String uriGetCurrencyToday;

    @Value(value = "${uriGetCurrencyYesterday}")
    private String uriGetCurrencyYesterday;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeignCurrencyClientService feignCurrencyClientService;

    @MockBean
    private FeignGifClientService feignGifClientService;


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class TestResolvePage {

        @Test
        void return_bad_request_incorrect_url() throws Exception {

            mockMvc.perform(get("/resolve"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }


}