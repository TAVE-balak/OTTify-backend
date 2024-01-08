package tavebalak.OTTify.error.reviewCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RatingCheckValidator implements ConstraintValidator<ReviewRatingCheck,Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(value>=0.5 && value <=5 && (value%0.5 ==0)){
            return true;
        }
        else{
            return false;
        }
    }
}
