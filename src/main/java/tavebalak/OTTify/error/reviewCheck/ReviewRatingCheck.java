package tavebalak.OTTify.error.reviewCheck;

import tavebalak.OTTify.error.reviewCheck.RatingCheckValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RatingCheckValidator.class)
public @interface ReviewRatingCheck {
    String message() default "리뷰 허용 숫자가 아닙니다.";

    Class[] groups() default {};
    Class[] payload() default {};
}
