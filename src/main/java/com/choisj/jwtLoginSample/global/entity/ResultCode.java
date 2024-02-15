package com.choisj.jwtLoginSample.global.entity;

/**
 * Error Code와 Message를 정의한 Common Enum Class를 생성한다.
 * 필요한 Code와 Message들을 추가 하여 사용하면 된다.
 */
public enum ResultCode {
    SUCCESS(200, ResultMessage.SUCCESS),
    UNAUTHORIZED(401, ResultMessage.UNAUTHORIZED),
    NO_AUTH(403, ResultMessage.NO_AUTH),
    INTERNAL_ERROR(500, ResultMessage.INTERNAL_ERROR),
    ACCESS_NO_AUTH(1_000, ResultMessage.ACCESS_NO_AUTH),
    ACCESS_TOKEN_EXPIRED(1_001, ResultMessage.ACCESS_TOKEN_EXPIRED),
    REFRESH_TOKEN_EXPIRED(1_002, ResultMessage.REFRESH_TOKEN_EXPIRED),
    VALID_NOT_PHONE_NUM(1_007, ResultMessage.VALID_NOT_PHONE_NUM),
    VALID_NOT_PASSWORD(1_008, ResultMessage.VALID_NOT_PASSWORD),
    MEMBER_NOT_EXIST(1_012, ResultMessage.MEMBER_NOT_EXIST),
    LOGIN_REQUIRED(1_019, ResultMessage.LOGIN_REQUIRED),
    PARAM_NOT_VALID(2_000, ResultMessage.PARAM_NOT_VALID),
    VALID_NOT_NULL(1_020, ResultMessage.VALID_NOT_NULL),
    VALID_NOT_REGEXP(1_021, ResultMessage.VALID_NOT_REGEXP),
    ;

    private final int resultCode;
    private final String resultMessage;

    ResultCode(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public interface ResultMessage {
        String SUCCESS = "완료 되었습니다.";
        String UNAUTHORIZED = "인증에 실패하였습니다.";
        String NO_AUTH = "접근 권한이 없습니다.";
        String ACCESS_NO_AUTH = "접근 권한이 없습니다.";
        String ACCESS_TOKEN_EXPIRED = "Access Token이 만료 되었습니다.";
        String REFRESH_TOKEN_EXPIRED = "Refresh Token이 만료 되었습니다.";
        String VALID_NOT_PHONE_NUM = "가입되지 않은 핸드폰 번호 입니다.";
        String VALID_NOT_PASSWORD = "잘못된 비밀번호 입니다.";
        String MEMBER_NOT_EXIST = "존재하지 않는 사용자 입니다.";
        String LOGIN_REQUIRED = "로그인이 필요합니다.";
        String PARAM_NOT_VALID = "파라미터 오류입니다.";
        String INTERNAL_ERROR = "시스템 오류가 발생하였습니다. 다시 시도해주세요.";

        String VALID_NOT_NULL = "값을 입력하여 주세요.";
        String VALID_NOT_REGEXP = "잘못된 입력 형식입니다.";
    }
}
