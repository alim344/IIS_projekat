package com.example.autoskola.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstPracticalRequestDTO {

    private long id;
    private String text;
    private String declineWeek;
    private String email;
    private String candidate_name;
    private String candidate_lastname;
    private String status;


    public InstPracticalRequestDTO() {
    }

    public InstPracticalRequestDTO(long id,String text, String email, String candidate_name, String candidate_lastname, String status, String declineWeek) {
        this.text = text;
        this.email = email;
        this.candidate_name = candidate_name;
        this.candidate_lastname = candidate_lastname;
        this.status = status;
        this.id = id;
        this.declineWeek = declineWeek;
    }
}
