package com.faziz.exercise.tradeledger.controller.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotNull;

@FilterValidationType
@JsonInclude(Include.NON_ABSENT)
public class Filter {

    @NotNull
    private String attribute;
    private String operator;
    private String value;
    private Range range;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }
    
    public static class Range {
        
        private String from;
        private String to;

        public Range(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public Range() {
        }
        
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return "Range{" + "from=" + from + ", to=" + to + '}';
        }
    }

    @Override
    public String toString() {
        return "Filter{" + "attribute=" + attribute + 
            ", operator=" + operator + 
            ", value=" + value + 
            ", range=" + range + '}';
    }
}
