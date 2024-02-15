package com.choisj.jwtLoginSample.web.dto.member;

import com.choisj.jwtLoginSample.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

public class AuthDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginMemberDto {
        @NotEmpty
        @Schema(description = "로그인 아이디", nullable = false)
        private String loginId;    //아이디

        @NotEmpty
        @Schema(description = "비밀번호", nullable = false)
        private String password;   //비밀번호

        @Builder
        public LoginMemberDto(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveMemberDto {
        @NotEmpty
        @Schema(description = "로그인 아이디", nullable = false)
        private String loginId;    //아이디

        @NotEmpty
        @Schema(description = "비밀번호", nullable = false)
        private String password;   //비밀번호

        @NotEmpty
        @Schema(description = "비밀번호", nullable = false)
        private String name;       //닉네임
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenInfo {
        //private String grantType;
        private String accessToken;
        private String refreshToken;

        public TokenInfo(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }


}
