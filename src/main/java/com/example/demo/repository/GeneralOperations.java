package com.example.demo.repository;

import com.example.demo.model.Response;


public interface GeneralOperations<T> {
    public Response<T> create(T model);
    public Response<T> findByCode(String code);
}
