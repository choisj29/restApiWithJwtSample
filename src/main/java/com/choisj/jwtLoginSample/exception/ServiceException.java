package com.choisj.jwtLoginSample.exception;

import com.choisj.jwtLoginSample.global.entity.ResultCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Exception을 상속 받은 공통 Exception Class이다.
 * 원하는 서비스단 로직에서 이 Exception을 throw 해주면 된다.
 * 두 번째 생성자는 ResultCode Enum Class에서 메세지를 ${field}와 같이 정의하여 쓸 수 있다. *
 * Ex) ${field} 필드는 필수 값입니다.
 */

@RequiredArgsConstructor
public class ServiceException extends Exception {
    private final int resultCode;
    private final String resultMessage;

    public ServiceException(@NonNull ResultCode resultCodeEnum) {
        this.resultCode = resultCodeEnum.getResultCode();
        this.resultMessage = resultCodeEnum.getResultMessage();
    }

    public ServiceException(@NonNull ResultCode resultCodeEnum, @NonNull Map<String, Object> params) {
        this.resultCode = resultCodeEnum.getResultCode();
        String messageTemplate = resultCodeEnum.getResultMessage();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            messageTemplate = messageTemplate.replaceAll(String.format("\\$\\{%s\\}", entry.getKey()), String.valueOf(entry.getValue()));
        }

        this.resultMessage = messageTemplate;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
