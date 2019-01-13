package me.freelife.loc.address.repository;

import me.freelife.loc.address.domain.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long> {
    Optional<ApiInfo> findByApiId(Integer apiId);
}
