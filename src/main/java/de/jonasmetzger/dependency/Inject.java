package de.jonasmetzger.dependency;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String value() default DependencyInjector.DEFAULT_KEY;
}
