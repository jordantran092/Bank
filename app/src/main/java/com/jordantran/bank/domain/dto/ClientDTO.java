package com.jordantran.bank.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getters and setters
@AllArgsConstructor // constructor with all attributes
@NoArgsConstructor // constructor with no attributes
@Builder // instantiate objects easier

public class ClientDTO {

    private Long id;

    private String name;

    private double balance;

    private BankDTO bankDTO;



}

