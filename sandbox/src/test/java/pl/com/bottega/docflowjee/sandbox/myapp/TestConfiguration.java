package pl.com.bottega.docflowjee.sandbox.myapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public IBean ibean() {
        return new TestBeanImpl();
    }

}
