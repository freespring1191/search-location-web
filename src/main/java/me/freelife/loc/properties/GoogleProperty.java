package me.freelife.loc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google")
public class GoogleProperty extends ApiProperty{
    private String restApiHeaderName;
    private String restApiKey;
    private String restApiKeywordUrl;
}
