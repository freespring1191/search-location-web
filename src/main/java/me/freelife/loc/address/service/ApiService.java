package me.freelife.loc.address.service;

import me.freelife.loc.address.domain.Address;
import me.freelife.loc.address.domain.ApiType;
import me.freelife.loc.address.domain.Keyword;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface ApiService {

    <T> ResponseEntity<T> externalApiTransfer(Map<String, Object> map, Class<T> c) throws UnsupportedEncodingException;

    Address searchKeyword(Map<String, Object> map) throws UnsupportedEncodingException;

    List<Keyword> selectKeywordList();

    ApiType getApiType();

    String apiTypeConverter(String apiType);
}
