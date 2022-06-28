package com.exchanges.controllers;

import com.exchanges.dto.CurrencyDto;
import com.exchanges.dto.GifDto;
import com.exchanges.dto.ResponseDto;
import com.exchanges.services.FeignCurrencyClientService;
import com.exchanges.services.FeignGifClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/resolve")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final FeignCurrencyClientService feignCurrencyClientService;
    private final FeignGifClientService feignGifClientService;


    @Value(value = "${defaultCurrencyTo}")
    private String defaultCurrencyTo;

    @Value(value = "${uriGetBrokeGif}")
    private String uriGetBrokeGif;

    @Value(value = "${uriGetRichGif}")
    private String uriGetRichGif;


    @GetMapping("/v1/random-gif-rich")
    public ResponseEntity<ResponseDto> getRichRandomGif() {

        String jsonString = feignGifClientService.getRichRandomGif();

        JSONObject obj = new JSONObject(jsonString);

        String urlGif = obj.getJSONObject("data")
                .getJSONObject("images")
                .getJSONObject("original")
                .getString("url");

        GifDto gifDto = GifDto.builder()
                .url(urlGif)
                .build();

        if (gifDto.getUrl().contains(".gif")) {
            return ResponseEntity.ok().body(ResponseDto
                    .builder()
                    .data(Map.of("url", gifDto.getUrl()))
                    .success(true)
                    .build());
        }
        log.info("Illegal Argument " + gifDto);
        return ResponseEntity.badRequest().body(ResponseDto
                .builder()
                .data(Map.of("GifDto", gifDto))
                .error(List.of("Illegal Argument" + gifDto))
                .build());
    }


    @GetMapping("/v1/random-gif-broke")
    public ResponseEntity<ResponseDto> getBrokeRandomGif() {

        String jsonString = feignGifClientService.getBrokeRandomGif();
        JSONObject obj = new JSONObject(jsonString);

        String urlGif = obj.getJSONObject("data")
                .getJSONObject("images")
                .getJSONObject("original")
                .getString("url");

        GifDto gifDto = GifDto.builder()
                .url(urlGif)
                .build();

        if (gifDto.getUrl().contains(".gif")) {

            return ResponseEntity.ok().body(ResponseDto
                    .builder()
                    .data(Map.of("url", gifDto.getUrl()))
                    .success(true)
                    .build());
        }

        return ResponseEntity.badRequest().body(ResponseDto
                .builder()
                .data(Map.of("GifDto", gifDto))
                .error(List.of("Illegal Argument" + gifDto))
                .build());
    }

    @GetMapping("/v1/yesterday/{symbol}")
    public ResponseEntity<ResponseDto> getYesterdayCurrency(@PathVariable("symbol") String symbol) {
        LocalDate sendDateOne = LocalDate.now().minusDays(1);
        String sendDate = sendDateOne.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        String jsonYesterday = feignCurrencyClientService.getYesterdayCurrency(symbol, sendDate);
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
                .data(Map.of("Yesterday", yesterdayDto))
                .success(true)
                .build());
    }


    @GetMapping("/v1/today/{symbol}")
    public ResponseEntity<ResponseDto> getTodayCurrency(@PathVariable("symbol") String symbol) {

        String jsonToday = feignCurrencyClientService.getTodayCurrency(symbol);

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

            return ResponseEntity.ok().body(ResponseDto
                    .builder()
                    .data(Map.of("Today", todayDto))
                    .success(true)
                    .build());

        } catch (JSONException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency didn't found");
        }
    }
}
