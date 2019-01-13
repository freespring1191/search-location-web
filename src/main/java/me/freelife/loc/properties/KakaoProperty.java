package me.freelife.loc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperty extends ApiProperty {
    private String restApiHeaderName;
    private String restApiKey;
    private String restApiKeywordUrl;
}
