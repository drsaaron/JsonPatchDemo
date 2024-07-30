/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author aar1069
 */
@Data
public class PersonView {
    
    private Long id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private AddressView address;
    private List<RoleView> roles;
}
