package me.freelife.loc.address.service;

import me.freelife.loc.address.domain.Address;
import me.freelife.loc.address.domain.ApiInfo;
import me.freelife.loc.address.domain.ApiType;
import me.freelife.loc.address.domain.Keyword;
import me.freelife.loc.address.repository.ApiInfoRepository;
import me.freelife.loc.address.repository.KeywordRepository;
import me.freelife.loc.external.kakao.address.domain.KakaoResKeyword;
import me.freelife.loc.external.naver.address.domain.NaverResKeyword;
import me.freelife.loc.properties.ApiProperty;
import me.freelife.loc.properties.GoogleProperty;
import me.freelife.loc.properties.KakaoProperty;
import me.freelife.loc.properties.NaverProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.freelife.loc.commons.ApiTypeName.*;

@Service
public class ApiServiceImpl implements ApiService {
    private Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

    private final RestTemplate restTemplate;
    private final KakaoProperty kakao;
    private final NaverProperty naver;
    private final GoogleProperty google;
    private final AddressAdapter adapter;
    private final KeywordRepository keywordRepository;
    private final ApiInfoRepository apiInfoRepository;

    public ApiServiceImpl(RestTemplateBuilder restTemplateBuilder, KakaoProperty kakao, NaverProperty naver, GoogleProperty google, AddressAdapter adapter, KeywordRepository keywordRepository, ApiInfoRepository apiInfoRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakao = kakao;
        this.naver = naver;
        this.google = google;
        this.adapter = adapter;
        this.keywordRepository = keywordRepository;
        this.apiInfoRepository = apiInfoRepository;
    }

    /**
     * 외부 REST API 응답 요청
     * @param apiMap
     * @param c
     * @param <T>
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public <T> ResponseEntity<T> externalApiTransfer(Map<String, Object> apiMap, Class<T> c) throws UnsupportedEncodingException {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("query", String.valueOf(apiMap.get("keyword")));
        map.add("page",  String.valueOf(apiMap.get("page")));
        map.add("size", String.valueOf(apiMap.get("size")));
        URI uri = getUri(String.valueOf(apiMap.get("url")), map);
        System.out.println("uri = "+uri);
        RequestEntity request = RequestEntity.get(uri)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .header(String.valueOf(apiMap.get("headerName")), String.valueOf(apiMap.get("key")))
                    .build();

        ResponseEntity<T> res = restTemplate.exchange(request, c);
        return res;
    }

    /**
     * 각 주소API 타입에서 받은 주소정보를 공통 주소 형식으로 변환
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public Address searchKeyword(Map<String, Object> map) throws UnsupportedEncodingException {

        String keyword = String.valueOf(map.get("keyword"));
        Optional<Keyword> result = this.keywordRepository.findByWord(keyword);
        Keyword postData = new Keyword();
        int count = 1;
        if(result.isEmpty()){
            postData = Keyword.builder()
                .word(keyword)
                .searchCount(count)
                .build();
        }else{
            postData = result.get();
            count = postData.getSearchCount()+1;
            postData.setSearchCount(count);
        }
        this.keywordRepository.save(postData);

        ApiType apiType = getApiType();

        String paramApiType = String.valueOf(map.get("type"));
        if(paramApiType != null) {
            ApiType paramType = ApiType.findByApiType(paramApiType);
            if(paramType != null){
                apiType = apiType.isEquals(paramApiType) ? apiType : paramType ;
                map.put("type",paramApiType);
            }
        }

        log.info("## 파라메터 타입 우선 적용으로 "+ apiType + " API 으로 검색 요청");
        map = SetApiType(map);
        ResponseEntity resData =  externalApiTransfer(map, getClassType(map));

        //공통 주소 형식으로 변환
        Address address = adapter.externalAddressConverter(resData, apiType);
        return address;
    }

    @Override
    public List<Keyword> selectKeywordList() {
        return this.keywordRepository.findTop10ByOrderBySearchCountDesc();
    }

    @Override
    public ApiType getApiType() {
        Optional<ApiInfo> apiInfo = this.apiInfoRepository.findByApiId(0);
        return apiInfo.get().getApiType();
    }


    @Override
    public String apiTypeConverter(String apiType) {
        Optional<ApiInfo> apiInfo = this.apiInfoRepository.findByApiId(0);
        ApiInfo updateApiInfo = apiInfo.get();

        if(ApiType.findByApiType(apiType) != null) {
            updateApiInfo.setApiType(ApiType.valueOf(apiType));
            this.apiInfoRepository.save(updateApiInfo);
            log.info("## "+ apiType + " 타입 으로 변환");
        }
        return getApiType().getName();
    }

    /**
     * 타입에 따라 API 리턴 클래스 타입 변경
     * @param map
     * @return
     */
    private Class getClassType(Map<String, Object> map) {
        switch (String.valueOf(map.get("type"))) {
            case KAKAO :
                return KakaoResKeyword.class;
            case NAVER :
                return NaverResKeyword.class;
            case GOOGLE :
                return NaverResKeyword.class;
        }
        return KakaoResKeyword.class;
    }

    /**
     * 타입에 따라 API 프로퍼티 변환
     * @param map
     * @return
     */
    private Map<String, Object> SetApiType(Map<String, Object> map) {
        switch (String.valueOf(map.get("type"))) {
            case KAKAO :
                map = keyMapping(map, kakao);
                break;
            case NAVER :
                map = keyMapping(map, naver);
                break;
            case GOOGLE :
                map = keyMapping(map, google);
                break;
        }
        return map;
    }

    /**
     * 타입 프로퍼티 값 맵핑
     * @param map
     * @param e
     * @param <E>
     * @return
     */
    private <E extends ApiProperty> Map<String, Object> keyMapping(Map<String, Object> map, E e) {
        map.put("key", String.valueOf(e.getRestApiKey()));
        map.put("url",e.getRestApiKeywordUrl());
        map.put("headerName",e.getRestApiHeaderName());
        return map;
    }

    /**
     * URI 가져오기
     * @Method Name : getUri
     * @param url
     * @param query
     * @return
     * @throws UnsupportedEncodingException
     */
    private URI getUri(String url, MultiValueMap<String,String> data) throws UnsupportedEncodingException{

        URI uri= UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(data)
                .encode()
                .build()
                .toUri();
        return uri;
    }
}
