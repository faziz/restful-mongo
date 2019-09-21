package com.faziz.exercise.tradeledger.controller.filter;

import com.faziz.exercise.tradeledger.controller.filter.Filter.Range;
import static java.util.Arrays.asList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilterValidator 
        implements ConstraintValidator<FilterValidationType, Filter> {
    
    private final static List<String> OPERATORS = asList("eq", "gte", "lte");

    @Override
    public boolean isValid(Filter filter, ConstraintValidatorContext context) {
        String attribute = filter.getAttribute();
        String operator  = filter.getOperator();
        String value     = filter.getValue();
        Range  range     = filter.getRange();

        //TODO: Un-uglify this logic.
        if (null == attribute || attribute.isEmpty()) {
            return false;
        }
        if (null != operator && !operator.isEmpty()) {
            // Unknown operators are not allowed.
            if (!OPERATORS.contains(operator.toLowerCase())) {
                return false;
            } 
            // When operator is `gte` *or* `lte` then `range` is not allowed.
            else if ((range != null && 
                    (operator.equalsIgnoreCase("lte") || operator.equalsIgnoreCase("gte")))) {
                return false;
            }
        }

        // Both `value` *and* `range` are not allowed.
        if (null != range && (null != value && !value.isEmpty() )) {
            return false;
        }

        return true;
    }
    
}
