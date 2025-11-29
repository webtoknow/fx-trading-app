package com.fx.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    private Integer id;
    private String username;
    private String primaryCcy;
    private String secondaryCcy;
    private Double rate;
    private String action;
    private long notional;
    private String tenor;
    private long date;
}
