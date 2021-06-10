package pl.com.bottega.docflowjee.sandbox.reflectionapi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    public void getInstanceOfMetaClass() throws ClassNotFoundException {
        Class<ReflectionTest> reflectionTestClass1 = (Class<ReflectionTest>) this.getClass();
        Class<ReflectionTest> reflectionTestClass2 = (Class<ReflectionTest>) Class.forName("pl.com.bottega.docflowjee.sandbox.reflectionapi.ReflectionTest");
        Class<ReflectionTest> reflectionTestClass3 = ReflectionTest.class;

        assertThat(reflectionTestClass1).isEqualTo(reflectionTestClass3);
        assertThat(reflectionTestClass3).isEqualTo(reflectionTestClass2);
    }

    @Test
    public void canReadAnnotations() {
        Annotation[] annotations = Bar.class.getAnnotations();
        assertThat(annotations[0].annotationType()).isEqualTo(Component.class);
        assertThat(annotations.length).isEqualTo(1L);
    }

    @Test
    public void canInstantiateObjectWithReflection() throws Exception {
        Class<Bar> barClass = Bar.class;
        Class<Foo> fooClass = Foo.class;

        Bar bar = barClass.getConstructor().newInstance(); // new Bar()
        Foo foo = fooClass.getConstructor().newInstance();
        for(Field field : fooClass.getDeclaredFields()) {
            if(field.isAnnotationPresent(Autowired.class) && field.getType() == Bar.class) {
                field.setAccessible(true);
                field.set(foo, bar);
                field.setAccessible(false);
            }
        }

        assertThat(foo.getBar()).isEqualTo(bar);
    }
}
