// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

public interface RoundRepository {
    Round findByName(String name);
}
