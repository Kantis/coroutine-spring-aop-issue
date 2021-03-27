package se.codeboss.aop.issue

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun someAspect() = SampleAspect()
}
