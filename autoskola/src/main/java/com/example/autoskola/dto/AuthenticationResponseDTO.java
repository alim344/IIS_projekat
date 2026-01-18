package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AuthenticationResponseDTO {


    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private Long expiresIn;

    /*@Getter
    @Setter
    private List<String> roles;*/


    public AuthenticationResponseDTO() {
        this.token = null;
        this.expiresIn = null;
      //  this.roles = null;
    }

    public AuthenticationResponseDTO(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
       // this.roles = roles;
    }


}
