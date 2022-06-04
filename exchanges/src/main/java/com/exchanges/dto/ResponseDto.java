package com.exchanges.dto;


import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ResponseDto {

    private boolean success = true;
    private List<String> error = null;
    private Object data = null;

}
