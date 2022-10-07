package com.testaccounting.app;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableJpaRepositories("com.testaccounting.data.repository")
@Getter
public class TestaccountingConfig {
    @Value("${reloadFilesOnStartup}")
    private boolean reloadFilesOnStartup;
}