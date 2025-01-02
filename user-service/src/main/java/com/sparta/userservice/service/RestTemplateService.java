package com.sparta.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T post(String url, Map<String, String> params, Class<T> responseType) {
        return restTemplate.postForObject(url, params, responseType);
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        // Headers 처리
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        headers.forEach(httpHeaders::set);
        org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(httpHeaders);

        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, responseType).getBody();
    }
}