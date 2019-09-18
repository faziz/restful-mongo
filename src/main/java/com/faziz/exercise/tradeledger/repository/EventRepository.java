package com.faziz.exercise.tradeledger.repository;

import com.faziz.exercise.tradeledger.domain.Event;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository extends BaseRepository<Event> {

    public EventRepository() {
        super();
    }
    
    EventRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected Class<Event> getEntityClass() {
        return Event.class;
    }    
}
