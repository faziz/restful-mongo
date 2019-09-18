package com.faziz.exercise.tradeledger.controller.filter;

import com.faziz.exercise.tradeledger.controller.filter.FilterValidator;
import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.controller.filter.Filter.Range;
import javax.validation.ConstraintValidatorContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilterValidatorTest {

    //TODO: This test class could use more test cases.
    
    @Mock
    private ConstraintValidatorContext context;
    private FilterValidator filterValidator = new FilterValidator();
    
    @Before
    public void setup() {
        filterValidator = new FilterValidator();
    }
    
    @Test
    public void testFilterIsValid() {
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setOperator("eq");
        filter.setValue("XXXXXX");
        
        assertTrue(filterValidator.isValid(filter, context));
    }

    @Test
    public void testAbsentAttribute_shouldFail() {
        Filter filter = new Filter();
        filter.setOperator("eq");
        filter.setValue("XXXXXX");
        
        assertFalse(filterValidator.isValid(filter, context));
    }

    @Test
    public void testUnsupportedOperation_shouldFail() {
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setOperator("test");
        filter.setValue("XXXXXX");
        
        assertFalse(filterValidator.isValid(filter, context));
    }

    @Test
    public void testSupportedOperationEq() {
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setOperator("eq");
        filter.setValue("XXXXXX");
        
        assertTrue(filterValidator.isValid(filter, context));
    }

    @Test
    public void testUnsupportedValueAndRange_shouldFail() {
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setValue("XXXXXX");
        filter.setRange(new Range("ttt", "iii"));
        
        assertFalse(filterValidator.isValid(filter, context));
    }
    
    @Test
    public void testUnsupportedLteAndGteAndRange_shouldFail() {
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setOperator("lte");
        filter.setValue("XXXXXX");
        filter.setRange(new Range("ttt", "iii"));
        
        assertFalse(filterValidator.isValid(filter, context));
        
        filter.setOperator("gte");
        assertFalse(filterValidator.isValid(filter, context));
    }
}
