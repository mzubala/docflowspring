package pl.com.bottega.docflowjee.sandbox.myapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {MyApp.class, TestConfiguration.class})
@RunWith(SpringRunner.class)
public class Bean1Test {

    @Autowired
    private IBean bean1;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void shouldDoIt() {
        assertThat(bean1).isNotNull();
        assertThat(applicationContext.getBean(Bean2.class)).isNotNull();
        assertThat(bean1).isInstanceOf(TestBeanImpl.class);
    }

}
