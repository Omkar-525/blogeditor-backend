package com.omkar.blogeditor.infra.model.request;

import com.omkar.blogeditor.infra.model.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest extends BaseRequest {

    public String oldPassword;

    public String newPassword;
}
