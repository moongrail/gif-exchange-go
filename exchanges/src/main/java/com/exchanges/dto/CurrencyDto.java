package com.exchanges.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CurrencyDto {

    String base;
    String to;
    Double rates;
    Long timestamp;
}
