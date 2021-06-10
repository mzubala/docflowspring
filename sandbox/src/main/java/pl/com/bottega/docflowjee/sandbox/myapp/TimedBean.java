package pl.com.bottega.docflowjee.sandbox.myapp;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface TimedBean {
    void doSthMeasured();
    void doIt2(BigDecimal value);
}

@Component
class TimedBeanImpl implements TimedBean {

    @Timed
    public void doSthMeasured() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doIt2(BigDecimal value) {
        System.out.println("I'm doing it");
        doSthMeasured();
    }

}
