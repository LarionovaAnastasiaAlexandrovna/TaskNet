package annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * Валидация email адреса.
 * Пример использования: @Email String email
 */
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "Некорректный формат email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}