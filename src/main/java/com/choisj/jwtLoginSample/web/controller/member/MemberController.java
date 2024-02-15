package com.choisj.jwtLoginSample.web.controller.member;

import com.choisj.jwtLoginSample.global.entity.ResultCode;
import com.choisj.jwtLoginSample.service.member.AuthService;
import com.choisj.jwtLoginSample.service.member.MemberService;
import com.choisj.jwtLoginSample.web.dto.common.ApiResult;
import com.choisj.jwtLoginSample.web.dto.member.AuthDto;
import com.choisj.jwtLoginSample.web.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Tag(name = "MemberController", description = "MemberController API입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final long COOKIE_EXPIRATION = 7776000; // 90일

    @PostMapping("/members/new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입", description = "아이디,비밀번호,닉네임을 사용하여 회원가입")
    @Parameter(name= "loginId", description = "not null, unique")
    @Parameter(name= "password", description = "not null")
    @Parameter(name= "name", description = "not null, unique")
    public ApiResult<?> join(@Valid @RequestBody AuthDto.SaveMemberDto dto, BindingResult bindingResult) throws IOException, BindException {
        log.info("members/new in ====");
        // loginId 중복 체크
        if(!memberService.checkLoginIdDuplicate(dto.getLoginId())) {
            bindingResult.addError(new FieldError("member", "loginId", dto.getLoginId(),false,null,null,"로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if(memberService.checkNameDuplicate(dto.getName())) {
            bindingResult.addError(new FieldError("member", "name", dto.getName(),false,null,null,"닉네임이 중복됩니다."));
        }
        // 빈 칸 체크
        if (bindingResult.hasErrors()) {
//            return ApiResult.createFail(bindingResult);
            throw new BindException(bindingResult);
        }
        MemberDto join = memberService.join(dto);
        return ApiResult.createSuccess(join);
    }


    // 로그인 -> 토큰 발급
    @Operation(summary = "로그인", description = "아이디,비밀번호,닉네임을 사용하여 로그인")
    @PostMapping("/members/login")
    public ApiResult<?> login(@RequestBody @Valid AuthDto.LoginMemberDto loginDto) {
        // User 등록 및 Refresh Token 저장
        AuthDto.TokenInfo tokenDto = authService.login(loginDto);

        // Refresh Token 저장
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
                .build();

        ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                // AccessToken 저장
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .build();

        return ApiResult.createSuccess(tokenDto);
    }

    @Operation(summary = "확인", description = "토큰 재발급 필요 여부 확인")
    @PostMapping("/members/validate")
    public ApiResult<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ApiResult.createSuccess("재발급 불필요"); // 재발급 필요X
        } else {
            return  ApiResult.builder()
                    .resultCode(ResultCode.UNAUTHORIZED.getResultCode())
                    .resultMessage(ResultCode.UNAUTHORIZED.getResultMessage())
                    .data("재발급 필요")
                    .build();
        }
    }

    @Operation(summary = "재발급", description = "토큰 재발급")
    @PostMapping("/members/reissue")
    public ApiResult<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        AuthDto.TokenInfo reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            // Refresh Token 저장
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();

            ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // Access Token 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .build();
            return ApiResult.builder()
                    .resultCode(ResultCode.SUCCESS.getResultCode())
                    .resultMessage("토큰 재발급 성공")
                    .data("Bearer " + reissuedTokenDto.getAccessToken())
                    .build();

        } else { // Refresh Token 탈취 가능성
            // Cookie 삭제 후 재로그인 유도
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
            return ApiResult.builder()
                    .resultCode(ResultCode.UNAUTHORIZED.getResultCode())
                    .resultMessage("Refresh Token 탈취 가능성")
                    .data("Cookie 삭제 후 재로그인 유도")
                    .build();
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/members/logout")
    public ApiResult<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        log.info("logout controller in, requestAccessToken is {}",requestAccessToken);
        authService.logout(requestAccessToken);

        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
        return ApiResult.builder()
                .resultCode(ResultCode.SUCCESS.getResultCode())
                .resultMessage("로그아웃 성공")
                .data("responseCookie " + responseCookie.toString())
                .build();
    }

}
