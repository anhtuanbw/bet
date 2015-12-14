// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChangeLogRepository extends MongoRepository<ChangeLog, String> {
    void deleteByEntityId(long entityId);
}
