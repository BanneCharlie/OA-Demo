package com.banne.template.api;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public interface DevConfigApi {
    String getValueByKey(String key);
}
