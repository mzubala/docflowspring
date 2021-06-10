package pl.com.bottega.docflowjee.sandbox.reflectionapi;

import org.springframework.beans.factory.annotation.Autowired;

public class Foo {

    @Autowired
    private Bar bar;

    @Autowired
    private Baz baz;

    public Bar getBar() {
        return bar;
    }
}
