package com.omkar.blogeditor.infra.model.response;

import com.omkar.blogeditor.infra.entity.User;
import com.omkar.blogeditor.infra.model.BaseResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends BaseResponse {

    private String jwt;
    private User user;

    @Builder
    public LoginResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, String jwt, User user) {
        super(httpStatus, status, responseCode, responseDescription);
        this.jwt=jwt;
        this.user=user;
    }
}
