package pl.com.bottega.docflowjee.sandbox.myapp;

import org.springframework.stereotype.Component;

@Component
public class Bean1 implements IBean {
    private String testValue;

    public Bean1() {
        this.testValue = "ala";
    }

    public Bean1(String testValue) {
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }
}
