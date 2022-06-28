package com.exchanges.controllers;

import com.exchanges.dto.CurrencyDto;
import com.exchanges.dto.GifDto;
import com.exchanges.dto.ResponseDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class ResolveController {

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


    @GetMapping("/resolve")
    public String getResolvePage(@RequestParam("symbol") String symbol, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        if (symbol.isEmpty()) {
            symbol = "RUB";
        }

        ResponseEntity<ResponseDto> currencyToday =
                restTemplate.getForEntity(uriGetCurrencyToday + symbol, ResponseDto.class, model);
        ResponseEntity<ResponseDto> currencyYesterday =
                restTemplate.getForEntity(uriGetCurrencyYesterday + symbol, ResponseDto.class, model);

        JSONObject todayObj = new JSONObject(currencyToday);
        JSONObject yesterdayObj = new JSONObject(currencyYesterday);

        CurrencyDto today = CurrencyDto
                .builder()
                .to(todayObj
                        .getJSONObject("body")
                        .getJSONObject("data")
                        .getJSONObject("Today")
                        .getString("to"))
                .rates(todayObj
                        .getJSONObject("body")
                        .getJSONObject("data")
                        .getJSONObject("Today")
                        .getDouble("rates"))
                .build();

        CurrencyDto yesterday = CurrencyDto
                .builder()
                .to(yesterdayObj
                        .getJSONObject("body")
                        .getJSONObject("data")
                        .getJSONObject("Yesterday")
                        .getString("to"))
                .rates(yesterdayObj
                        .getJSONObject("body")
                        .getJSONObject("data")
                        .getJSONObject("Yesterday")
                        .getDouble("rates"))
                .build();

        model.addAttribute("currencyName", today.getTo());
        model.addAttribute("currencyValue", today.getRates());
        model.addAttribute("yesterdayCurrencyValue", yesterday.getRates());


        boolean result = yesterday.getRates() <= today.getRates();

        if (result) {
            ResponseEntity<ResponseDto> gifRich = restTemplate.getForEntity(uriGetRichGif, ResponseDto.class);
            JSONObject gifObj = new JSONObject(gifRich);
            GifDto gifDto = getGifDto(gifObj);
            model.addAttribute("gifRandom", gifDto.getUrl());
        } else {
            ResponseEntity<ResponseDto> gifBroke = restTemplate.getForEntity(uriGetBrokeGif, ResponseDto.class);
            JSONObject gifObj = new JSONObject(gifBroke);
            GifDto gifDto = getGifDto(gifObj);
            model.addAttribute("gifRandom", gifDto.getUrl());
        }

        return "resolve";
    }

    private GifDto getGifDto(JSONObject gifObj) {
        return GifDto
                .builder()
                .url(gifObj.getJSONObject("body")
                        .getJSONObject("data")
                        .getString("url"))
                .build();
    }

}
