package com.example.demo.model;

import lombok.Data;

@Data
public class Response<T> {
    public static final String TECHNICAL_ERROR_101 = "TE-101";
    public static final String BUSINESS_ERROR = "BE-101";
    private T data;
    private String message;
    private String code;
}
