package pl.com.bottega.docflowjee.sandbox.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.sandbox.myapp.MyApp;
import pl.com.bottega.docflowjee.sandbox.myapp.TimedBean;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyApp.class)
public class TimedBeanTest {

    @Autowired
    private TimedBean timedBean;

    @Test
    public void measuresTime() {
        timedBean.doSthMeasured();

        assertThat(timedBean).isNotExactlyInstanceOf(TimedBean.class);
    }

    @Test
    public void callsLoggingAspect() {
        timedBean.doIt2(BigDecimal.TEN);
    }

}
