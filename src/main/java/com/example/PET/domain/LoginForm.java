package com.example.PET.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotBlank(message = "값을 입력해주세요")
    private String user_id;

    @NotBlank(message = "값을 입력해주세요")
    private String user_pw;
}
