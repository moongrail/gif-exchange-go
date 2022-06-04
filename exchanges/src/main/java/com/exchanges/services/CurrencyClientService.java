package com.exchanges.services;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "currency", url = "https://openexchangerates.org/api")
public interface CurrencyClientService {


    @GetMapping(value = "historical/{sendDate}.json?app_id=0f82966a55114e62853dc3227668ce7b&base=USD&symbols={symbol}")
    String getYesterdayCurrency(@PathVariable("symbol") String symbol,
                                @PathVariable("sendDate") String sendDate);



    @GetMapping(value = "/latest.json?app_id=0f82966a55114e62853dc3227668ce7b&base=USD&symbols={symbol}")
    String getTodayCurrency(@PathVariable("symbol") String symbol);


}
