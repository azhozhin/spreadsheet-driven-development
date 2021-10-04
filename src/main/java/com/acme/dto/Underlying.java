package com.acme.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Underlying {
    private String type;
}
