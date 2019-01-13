package me.freelife.loc.address.repository;

import me.freelife.loc.address.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByWord(String word);

    /**
     * 키워드 검색 TOP 조회
     * @return
     */
    List<Keyword> findTop10ByOrderBySearchCountDesc();
}
