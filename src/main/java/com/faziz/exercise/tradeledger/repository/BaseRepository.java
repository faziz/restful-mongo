package com.faziz.exercise.tradeledger.repository;

import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.domain.BaseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class BaseRepository<T extends BaseEntity> {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    protected abstract Class<T> getEntityClass();
    
    private final Map<String, Function<Filter, Criteria>> operators;

    public BaseRepository() {
        operators = new HashMap<>();
        operators.put("eq",  this::eq);
        operators.put("lte", this::lte);
        operators.put("gte", this::gte);
    }
    
    public BaseRepository(MongoTemplate mongoTemplate) {
        this();
        this.mongoTemplate = mongoTemplate;
    }
    
    public Optional<T> getById(String id) {
        //TODO: Figure out why Mongodb is not getting me the object for id.
//        return ofNullable(mongoTemplate.findById(id, getEntityClass()));
        return getAll().stream().filter(e -> e.getId().equals(id)).findFirst();
    }
    
    public List<T> search(final @Valid List<Filter> filters) {
        return mongoTemplate.find(query(criteria(filters)), getEntityClass());
    }
    
    public List<T> getAll() {
        return mongoTemplate.findAll(getEntityClass());
    }
    
    private Criteria criteria(List<Filter> searchFilters) {
        Criteria[] orExpression = searchFilters.stream().
            filter(f -> operators.containsKey(f.getOperator())).
                map(f -> new OperatorFunction(operators.get(f.getOperator()), f)).
                    map(o -> o.function.apply(o.filter)).
                        toArray(Criteria[]::new);
        return new Criteria().orOperator(orExpression);
    }
    
    private Criteria eq(Filter filter) {
        Criteria criteria = null;
        // Run plain 'eq' query.
        if (null != filter.getValue() && null == filter.getRange()) {
            criteria = new Criteria().and(filter.getAttribute()).is(filter.getValue());
        }
        // Run the range query.
        else if (null == filter.getValue() && null != filter.getRange()) {
            criteria = new Criteria().and(filter.getAttribute()).
                gte(filter.getRange().getFrom()).
                    lte(filter.getRange().getTo());
        }
        return criteria;
    }

    private Criteria lte(Filter filter) {
        return new Criteria().and(filter.getAttribute()).lte(filter.getValue());
    }
    
    private Criteria gte(Filter filter) {
        return new Criteria().and(filter.getAttribute()).gte(filter.getValue());
    }
    
    private class OperatorFunction {
        public final Function<Filter, Criteria> function;
        public final Filter filter;

        public OperatorFunction(Function<Filter, Criteria> function, Filter filter) {
            this.function = function;
            this.filter = filter;
        }
    }
}
