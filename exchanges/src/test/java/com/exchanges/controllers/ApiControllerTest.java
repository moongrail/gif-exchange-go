package com.exchanges.controllers;

import com.exchanges.services.FeignCurrencyClientService;
import com.exchanges.services.FeignGifClientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiControllerTest {

    @Value(value = "${uriExchangeUSD}")
    private String uriExchangeUSD;

    @Value(value = "${defaultCurrencyTo}")
    private String defaultCurrencyTo;

    @Value(value = "${defaultCurrencyBase}")
    private String defaultCurrencyBase;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeignCurrencyClientService feignCurrencyClientService;

    @MockBean
    private FeignGifClientService feignGifClientService;

    @BeforeEach
    void setUp() {

        when(feignGifClientService.getRichRandomGif()).thenReturn("{\n" +
                "  \"data\": {\n" +
                "\"images\": {\n" +
                " \"original\":" +
                "  {\t\"url\":\"test.gif\"\n" + " }\n" +
                " }\n" +
                "}\n" +
                "}");

        when(feignGifClientService.getBrokeRandomGif()).thenReturn("{\n" +
                "  \"data\": {\n" +
                "\"images\": {\n" +
                " \"original\": {\t\"url\":\"test.gif\"\n" +
                " }\n" +
                " }\n" +
                "}\n" +
                "}");

        when(feignCurrencyClientService.getTodayCurrency(anyString())).thenReturn("{\n" +
                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                "    \"timestamp\": 1651708744,\n" +
                "    \"base\": \"USD\",\n" +
                "    \"rates\": {\n" +
                "        \"RUB\": 67.000005\n" +
                "    }\n" +
                "}");

        when(feignCurrencyClientService.getYesterdayCurrency(anyString(), anyString())).thenReturn("{\n" +
                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                "    \"timestamp\": 1654286314,\n" +
                "    \"base\": \"USD\",\n" +
                "    \"rates\": {\n" +
                "        \"RUB\": 63.625004\n" +
                "    }\n" +
                "}");
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class TestGifResponseTests {

        @Test
        void get_rich_random_gif_status_ok() throws Exception {
            mockMvc.perform(get("/resolve/v1/random-gif-rich")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void get_broke_random_gif_status_ok() throws Exception {
            mockMvc.perform(get("/resolve/v1/random-gif-broke")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void get_rich_random_gif_have_url() throws Exception {
            mockMvc.perform(get("/resolve/v1/random-gif-rich")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data.url", is("test.gif")));
        }

        @Test
        void get_broke_random_gif_have_url() throws Exception {
            mockMvc.perform(get("/resolve/v1/random-gif-broke")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data.url", is("test.gif")));
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class TestCurrencyResponseTests {
        @Test
        void get_yesterday_currency_status_ok() throws Exception {
            mockMvc.perform(get("/resolve/v1/yesterday/{defaultCurrencyTo}", defaultCurrencyTo)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void get_today_currency_status_ok() throws Exception {
            mockMvc.perform(get("/resolve/v1/today/{defaultCurrencyTo}", defaultCurrencyTo)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void yesterday_currency_have_exchanges() throws Exception {
            mockMvc.perform(get("/resolve/v1/yesterday/{defaultCurrencyTo}", defaultCurrencyTo)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data.Yesterday.base", is(defaultCurrencyBase)))
                    .andExpect(jsonPath("data.Yesterday.to", is(defaultCurrencyTo)))
                    .andExpect(jsonPath("data.Yesterday.rates", is(63.625004)));

        }

        @Test
        void get_today_currency_have_exchanges() throws Exception {
            mockMvc.perform(get("/resolve/v1/today/{defaultCurrencyTo}", defaultCurrencyTo)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data.Today.base", is(defaultCurrencyBase)))
                    .andExpect(jsonPath("data.Today.to", is(defaultCurrencyTo)))
                    .andExpect(jsonPath("data.Today.rates", is(67.000005)));
        }

    }

}