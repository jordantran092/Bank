package com.jordantran.bank.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getters and setters
@AllArgsConstructor // constructor with all attributes
@NoArgsConstructor // constructor with no attributes
@Builder // instantiate objects easier
public class BankDTO {


    private Long id;

    private boolean error;

    private String errorStr;


}

