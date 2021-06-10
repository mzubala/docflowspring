package pl.com.bottega.docflowjee.sandbox.applicationcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ExampleConfiguration {

    @Bean
    @Lazy
    public AnotherExampleComponent foo(ExampleComponent exampleComponent) {
        return new AnotherExampleComponent(exampleComponent);
    }
}
