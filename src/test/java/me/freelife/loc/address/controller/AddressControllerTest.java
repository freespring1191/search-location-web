package me.freelife.loc.address.controller;

import me.freelife.loc.accounts.AccountRepository;
import me.freelife.loc.address.repository.KeywordRepository;
import me.freelife.loc.common.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @RunWith(SpringRunner.class)
// @SpringBootTest
// @AutoConfigureMockMvc
public class AddressControllerTest {
/*
    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setUp() {
        // this.keywordRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    @Test
    public void 키워드_검색_결과OK() throws Exception {
        mockMvc.perform(get("/api/searchKeyword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .param("keyword","카카오프렌즈"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    */
/*
    @Test
    public void 키워드_검색_5페이지씩_조회() throws Exception {
        mockMvc.perform(get("/api/searchKeyword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .param("keyword","카카오프렌즈")
                .param("page", "1")
                .param("size", "2")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    */

}