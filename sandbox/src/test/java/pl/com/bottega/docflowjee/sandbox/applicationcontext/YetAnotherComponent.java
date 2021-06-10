package pl.com.bottega.docflowjee.sandbox.applicationcontext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class YetAnotherComponent {

    private ExampleInterface i1;
    private ExampleInterface i2;

    public YetAnotherComponent(@Qualifier("foo") ExampleInterface i1, @Qualifier("exampleComponent") ExampleInterface i2) {
        this.i1 = i1;
        this.i2 = i2;
    }
}
