package me.freelife.loc.address.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.freelife.loc.address.domain.Address;
import me.freelife.loc.address.domain.ApiType;
import me.freelife.loc.address.domain.Keyword;
import me.freelife.loc.address.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ApiController {
    private Logger log = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    ApiService addressService;

    @Autowired
    ApiService apiService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 키워드 검색
     * @param pageable
     * @param errors
     * @param keyword
     * @param model
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/searchKeyword")
    public ResponseEntity searchAddress(Pageable pageable, String keyword, String type, Model model) throws UnsupportedEncodingException {
        Map<String, Object> apiMap = new HashMap<>();
        apiMap.put("keyword", keyword);
        apiMap.put("type", type);
        apiMap.put("page", pageable.getPageNumber());
        apiMap.put("size", pageable.getPageSize());

        Address address = addressService.searchKeyword(apiMap);
        List<Keyword> keywordList = addressService.selectKeywordList();

        List list = address.getAddress();
        model.addAttribute("address",address);
        model.addAttribute("keywordList",keywordList);
        // Resource
        // Page<KakaoResKeyword.Documents> page = new PageImpl<>(list, pageable, address.getTotalCount());
        // var pagedResources = assembler.toResource(page);
        // pagedResources.add(new Link("/docs/list.html#resources-address-list").withRel("profile"));
        return ResponseEntity.ok(model);

    }

    /**
     * 최초 로딩 시 키워드 검색 TOP 10
     * @return
     */
    @GetMapping("/keywordList")
    public ResponseEntity keywordList(){
        List<Keyword> list = addressService.selectKeywordList();
        return ResponseEntity.ok(list);
    }

    /**
     * API 타입 변경
     * @return
     */
    @PutMapping("/apiType")
    public ResponseEntity apiTypeConverter(@RequestBody String apiType, Model model) throws IOException {
        Map<String, Object> map = objectMapper.readValue(apiType, new TypeReference<Map<String, String>>(){});
        apiType = (String) map.get("apiType");
        if(!apiType.isBlank())
            apiType = apiService.apiTypeConverter(apiType);

        model.addAttribute(apiType);
        return ResponseEntity.ok(model);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(errors);
    }
}
