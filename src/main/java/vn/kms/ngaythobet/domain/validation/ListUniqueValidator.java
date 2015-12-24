package vn.kms.ngaythobet.domain.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ListUniqueValidator implements ConstraintValidator<ListUnique, List<?>> {

    @Override
    public void initialize(ListUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        if (list == null) {
            return false;
        }
        Set<Object> set = new HashSet<>(list);
        if (set.size() < list.size()) {
            return false;
        }
        return true;
    }

}
