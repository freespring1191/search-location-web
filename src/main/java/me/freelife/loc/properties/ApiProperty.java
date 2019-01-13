package me.freelife.loc.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApiProperty {
    private String restApiHeaderName;
    private String restApiKey;
    private String restApiKeywordUrl;
}
