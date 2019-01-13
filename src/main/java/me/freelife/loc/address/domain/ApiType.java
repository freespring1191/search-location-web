package me.freelife.loc.address.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ApiType {
    KAKAO, NAVER, GOOGLE
}
