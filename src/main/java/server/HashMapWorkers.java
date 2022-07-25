package server;

// import jakarta.inject.Qualifier;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Alternative
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HashMapWorkers {
}
