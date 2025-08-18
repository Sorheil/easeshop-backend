package com.nexora.easeshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
    private Instant timestamp = Instant.now();


    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }
}
