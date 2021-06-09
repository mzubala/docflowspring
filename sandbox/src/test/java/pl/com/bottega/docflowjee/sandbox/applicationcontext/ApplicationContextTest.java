package pl.com.bottega.docflowjee.sandbox.applicationcontext;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApplicationContextTest {

    @Test
    public void createsApplicationContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(getClass().getPackage().getName());

        assertThat(context.getBean(ExampleComponent.class)).isNotNull();
    }

}
