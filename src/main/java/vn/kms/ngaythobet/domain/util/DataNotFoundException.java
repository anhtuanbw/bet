// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.util;

public class DataNotFoundException extends NgaythobetException {
    public DataNotFoundException(String messageKey, String... messageArgs) {
        super(messageKey, messageArgs);
    }
}
