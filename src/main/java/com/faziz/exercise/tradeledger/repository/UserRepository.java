package com.faziz.exercise.tradeledger.repository;

import com.faziz.exercise.tradeledger.domain.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super();
    }

    //Only to be used from the tests.
    public UserRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
