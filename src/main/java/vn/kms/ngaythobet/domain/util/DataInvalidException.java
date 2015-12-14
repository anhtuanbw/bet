// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.util;

public class DataInvalidException extends NgaythobetException {
    public DataInvalidException(String messageKey, String... messageArgs) {
        super(messageKey, messageArgs);
    }
}
