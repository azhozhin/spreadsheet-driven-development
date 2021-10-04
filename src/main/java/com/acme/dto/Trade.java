package com.acme.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Trade {

    // Derivative properties
    Underlying underlying;
    String callOrPut;
    String type;

    // Bonds properties
    Float notional;
    Date expiration;
    Float rate;
    String index;


}

