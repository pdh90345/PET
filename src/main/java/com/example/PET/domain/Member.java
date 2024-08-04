package com.example.PET.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private String user_id;
    private String user_pw;
    private String user_name;
    private int question_goal;
    private int question_current;
    private int login_check;

}
