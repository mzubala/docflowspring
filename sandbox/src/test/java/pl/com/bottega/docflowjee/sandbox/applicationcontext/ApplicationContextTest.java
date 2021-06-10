package pl.com.bottega.docflowjee.sandbox.applicationcontext;

import org.junit.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ApplicationContextTest {

    @Test
    public void createsApplicationContextAndGetsBeans() {
        ApplicationContext context = new AnnotationConfigApplicationContext(getClass().getPackage().getName());

        assertThat(context.getBean("exampleComponent")).isNotNull();
        assertThat(context.getBean("foo")).isNotNull();
        assertThat(context.getBean(ExampleComponent.class)).isNotNull();
        assertThatThrownBy(() -> context.getBean(ExampleInterface.class)).isInstanceOf(NoUniqueBeanDefinitionException.class);
    }

}
