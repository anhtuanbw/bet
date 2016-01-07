// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChangeLogRepository extends MongoRepository<ChangeLog, String> {
    void deleteByEntityId(long entityId);
    List<ChangeLog> findTop10ByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);
    List<ChangeLog> findByEntityTypeAndUsername(String entityType, String username);
    List<ChangeLog> findByEntityType(String entityType);
    ChangeLog findFirst1ByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, Long entityId);
}
