package com.keessi.annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.keessi.annotation.service")
@Import({DataSourceConfig.class, TransactionConfig.class, MybatisConfig.class, ShiroConfig.class})
public class RootConfig {
}
