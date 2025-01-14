package vn.kms.ngaythobet.domain.util;

public class Constants {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#$%&'()*+,-./:!<=>?@\\^_`\\[\\]{|}~;])\\S{6,50}$";
    public static final String XAUTH_TOKEN_HEADER_NAME = "x-auth-token";
    public static final String WHITE_SPACE_REGEX = "^\\S.*\\S$";
    public static final String DEFAULT_ROUND_NAME = "circle";
    public static final int RANDOM_NAME_LENGTH = 20;
    public static final String FILE_NAME_DELIMETER = "_";
}
