package me.freelife.loc.address.domain;

import java.util.Arrays;
import java.util.List;

public enum ApiType {
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE");

    private String name;

    ApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 해당 API 타입과 같은 지 검사
     * @param apiType
     * @return
     */
    public boolean isEquals(String apiType){
        return this.name.equals(apiType.toUpperCase());
    }

    /**
     * API TYPE 에 있는지 검사
     * @param apiType
     * @return
     */
    public static ApiType findByApiType(String apiType){
        return
                //ApiType의 Enum 상수들을 순회하며
                Arrays.stream(ApiType.values())
                        //ApiType를 갖고 있는게 있는지 확인합니다.
                        .filter(v -> apiType.equals(v.name))
                        .findAny()
                        .orElse(null);
    }

}