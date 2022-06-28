package com.exchanges.services;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "gif", url = "https://api.giphy.com/v1/gifs")
public interface FeignGifClientService {

    @GetMapping("/random?api_key=XyJ8GBbPm8HFLOBmYCTb0X2QsxdI66JM&tag=rich")
    String getRichRandomGif();

    @GetMapping("/random?api_key=XyJ8GBbPm8HFLOBmYCTb0X2QsxdI66JM&tag=broke")
    String getBrokeRandomGif();
}
