package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.DeleteSpendingExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({DeleteSpendingExtension.class})
public @interface DeleteSpendings {
    String categoryId() default "";
}