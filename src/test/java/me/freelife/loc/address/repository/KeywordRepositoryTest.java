package me.freelife.loc.address.repository;

import me.freelife.loc.address.domain.Keyword;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

// @RunWith(SpringRunner.class)
// @SpringBootTest
// @ActiveProfiles("test")
public class KeywordRepositoryTest {
/*
    @Autowired
    KeywordRepository key;


    @Before
    public void init() {

        IntStream.rangeClosed(1, 100).forEach(index ->
            this.key.save(Keyword.builder()
                .word("테스트"+ index)
                .searchCount(index)
                .build())
        );

        String keyword = "카카오프렌즈";
        Optional<Keyword> result = this.key.findByWord(keyword);
        if(result.isEmpty()){
            this.key.save(
            Keyword.builder()
                    .word(keyword)
                    .searchCount(1)
                    .build()
            );
        }
    }
    */
/*
    @Test
    public void 검색조회수_높은순정렬_상위10개조회() {

        List<Keyword> data = key.findTop10ByOrderBySearchCountDesc();
        assertThat(data.size()).isEqualTo(10);
        assertThat(data.get(0).getSearchCount()).isEqualTo(100);
        assertThat(data.get(9).getSearchCount()).isEqualTo(91);
    }
    */
 /*
    @Test
    public void 키워드_업데이트_테스트() {

        String keyword = "카카오프렌즈";
        Optional<Keyword> result = this.key.findByWord(keyword);
        assertThat(result).isNotEmpty();
        Keyword postData = result.get();
        if(!result.isEmpty()){
            postData.setSearchCount(result.get().getSearchCount()+1);
            this.key.save(postData);
        }

        result = this.key.findByWord(keyword);
        assertThat(result.get().getSearchCount()).isEqualTo(2);

        Keyword saveData= Keyword.builder().build();
        int count = 1;
        if(!result.isEmpty()){
            count = result.get().getSearchCount()+1;
            saveData = Keyword.builder()
                    .word(keyword)
                    .searchCount(count)
                    .build();
        }else{
            saveData = result.get().builder().searchCount(count).build();
        }
        this.key.save(saveData);

    }
    */



}