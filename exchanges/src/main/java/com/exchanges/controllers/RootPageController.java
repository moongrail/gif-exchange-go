package com.exchanges.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/root")
public class RootPageController {


    @Value(value = "${uriExchangeUSD}")
    private String uriExchangeUSD;

    @Value(value = "${defaultCurrencyTo}")
    private String defaultCurrencyTo;

    @Value(value = "${defaultCurrencyBase}")
    private String defaultCurrencyBase;

    @GetMapping
    public String getHomePage(Model model) {

        model.addAttribute("uriExchangeUSD", uriExchangeUSD);
        model.addAttribute("defaultCurrencyTo", defaultCurrencyTo);
        model.addAttribute("defaultCurrencyBase", defaultCurrencyBase);


        return "rootPage";
    }


}
