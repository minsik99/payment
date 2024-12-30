package com.sparta.userservice.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T post(String url, Map<String, String> params, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);

        return response.getBody();
    }

    public <T> T get(String url, Map<String, String> headersMap, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headersMap.forEach(headers::set);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        return response.getBody();
    }
}