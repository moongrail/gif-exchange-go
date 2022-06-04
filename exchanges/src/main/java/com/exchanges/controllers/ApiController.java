package com.exchanges.controllers;

import com.exchanges.dto.CurrencyDto;
import com.exchanges.dto.GifDto;
import com.exchanges.dto.ResponseDto;
import com.exchanges.services.CurrencyClientService;
import com.exchanges.services.GifClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/resolve")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final CurrencyClientService currencyClientService;
    private final GifClientService gifClientService;


    @Value(value = "${defaultCurrencyTo}")
    private String defaultCurrencyTo;

    @Value(value = "${uriGetBrokeGif}")
    private String uriGetBrokeGif;

    @Value(value = "${uriGetRichGif}")
    private String uriGetRichGif;


    @GetMapping("/v1/random-gif-rich")
    public ResponseEntity<ResponseDto> getRichRandomGif(Model model) {

        String jsonString = gifClientService.getRichRandomGif();

        JSONObject obj = new JSONObject(jsonString);

        String urlGif = obj.getJSONObject("data")
                .getJSONObject("images")
                .getJSONObject("original")
                .getString("url");

        GifDto gifDto = GifDto.builder()
                .url(urlGif)
                .build();

        if (gifDto.getUrl().contains(".gif")) {
            model.addAttribute("gifRandom", gifDto.getUrl());
            return ResponseEntity.ok().body(ResponseDto
                    .builder()
                    .data(Map.of("url",gifDto.getUrl()))
                    .success(true)
                    .build());
        }
        log.info("Illegal Argument " + gifDto);
        return ResponseEntity.badRequest().body(ResponseDto
                .builder()
                .data(Map.of("GifDto",gifDto))
                .error(List.of("Illegal Argument" + gifDto))
                .build());
    }


    @GetMapping("/v1/random-gif-broke")
    public ResponseEntity<ResponseDto> getBrokeRandomGif(Model model) {

        String jsonString = gifClientService.getBrokeRandomGif();
        JSONObject obj = new JSONObject(jsonString);

        String urlGif = obj.getJSONObject("data")
                .getJSONObject("images")
                .getJSONObject("original")
                .getString("url");

        GifDto gifDto = GifDto.builder()
                .url(urlGif)
                .build();

        if (gifDto.getUrl().contains(".gif")) {
            model.addAttribute("gifRandom", gifDto.getUrl());
            return ResponseEntity.ok().body(ResponseDto
                    .builder()
                    .data(Map.of("GifDto",gifDto))
                    .success(true)
                    .build());
        }

        return ResponseEntity.badRequest().body(ResponseDto
                .builder()
                .data(Map.of("GifDto",gifDto))
                .error(List.of("Illegal Argument" + gifDto))
                .build());
    }

    @GetMapping("/v1/yesterday/{symbol}")
    public ResponseEntity<ResponseDto> getYesterdayCurrencyGif(@PathVariable("symbol") String symbol,
                                                             Model model) {
        LocalDate sendDateOne = LocalDate.now().minusDays(1);
        String sendDate = sendDateOne.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));


        String jsonYesterday = currencyClientService.getYesterdayCurrency(symbol, sendDate);
        JSONObject yesterday = new JSONObject(jsonYesterday);

        CurrencyDto yesterdayDto = CurrencyDto.builder()
                .base(yesterday.getString("base"))
                .to(symbol)
                .rates(yesterday.getJSONObject("rates").getDouble(symbol))
                .build();

        if (yesterdayDto.getRates() == null) {
            log.error("Empty JSON rates, wrong currency");

            return ResponseEntity.badRequest().body(ResponseDto
                    .builder()
                    .data(Map.of("Bad request", yesterdayDto))
                    .error(List.of("Empty JSON rates, wrong currency " + yesterday))
                    .build());
        }

        return ResponseEntity.ok().body(ResponseDto
                .builder()
                .data(Map.of("Yesterday",yesterdayDto))
                        .success(true)
                        .build());
    }


    @GetMapping("/v1/today/{symbol}")
    public ResponseEntity<ResponseDto> getTodayCurrencyGif(@PathVariable("symbol") String symbol,
                                                             Model model) {

        model.addAttribute("uriGetBrokeGif", uriGetBrokeGif);
        model.addAttribute("uriGetRichGif", uriGetRichGif);

        String jsonToday = currencyClientService.getTodayCurrency(symbol);

        try {
            JSONObject today = new JSONObject(jsonToday);
            CurrencyDto todayDto = CurrencyDto.builder()
                    .base(today.getString("base"))
                    .to(symbol)
                    .rates(today.getJSONObject("rates").getDouble(symbol))
                    .build();

            if (todayDto.getRates() == null) {
                log.error("Empty JSON rates, wrong currency");

                return ResponseEntity.badRequest().body(ResponseDto
                        .builder()
                        .data(Map.of("Today", todayDto))
                        .error(List.of("Empty JSON rates, wrong currency "))
                        .build());
            }
            model.addAttribute("currencyValue", todayDto.getRates());
            model.addAttribute("currencyName", todayDto.getTo());

            return ResponseEntity.ok().body(ResponseDto
                        .builder()
                        .data(Map.of("Today",todayDto))
                        .success(true)
                        .build());

        } catch (JSONException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency didn't found");
        }
    }

    @GetMapping
    public String getResolvePage(@RequestParam("symbol") String symbol, Model model) {

        if (symbol.isEmpty()){
            symbol = "RUB";
        }

        ResponseEntity<ResponseDto> currencyToday = getTodayCurrencyGif(symbol,model);
        ResponseEntity<ResponseDto> currencyYesterday = getYesterdayCurrencyGif(symbol,model);

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

        model.addAttribute("currencyName",today.getTo());
        model.addAttribute("currencyValue",today.getRates());
        model.addAttribute("yesterdayCurrencyValue",yesterday.getRates());


        Boolean result = yesterday.getRates() <= today.getRates();
            if (result) {
                getRichRandomGif(model);
            }
            getBrokeRandomGif(model);

        return "resolve";
    }

    
}
