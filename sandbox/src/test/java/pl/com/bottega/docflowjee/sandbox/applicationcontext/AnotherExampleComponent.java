package pl.com.bottega.docflowjee.sandbox.applicationcontext;

public class AnotherExampleComponent implements ExampleInterface {

    private ExampleComponent exampleComponent;

    public AnotherExampleComponent(ExampleComponent exampleComponent) {
        this.exampleComponent = exampleComponent;
    }
}
