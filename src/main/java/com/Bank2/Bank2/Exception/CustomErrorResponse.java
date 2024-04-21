package com.Bank2.Bank2.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
    private String errorMessage;
    private String reasonCode;

}
