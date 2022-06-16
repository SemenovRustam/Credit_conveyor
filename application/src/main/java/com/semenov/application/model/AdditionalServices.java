package com.semenov.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalServices {

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}