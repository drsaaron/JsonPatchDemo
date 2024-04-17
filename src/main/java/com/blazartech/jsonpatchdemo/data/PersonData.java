/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author aar1069
 */
@Entity
@Table(name = "person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "person_id")
    @Schema(description = "the ID of the person")
    @NotNull
    private Long id;
    
    @Column(name = "name_txt", nullable = false)
    @Schema(description = "the name of the person")
    @NotNull
    private String name;
    
    @Column(name = "birth_dte", nullable = false) 
    @Schema(description = "the birth date of the person")
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    
    @Column(name = "death_dte", nullable = true) 
    @Schema(description = "the death date of the person")
    @Null
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate deathDate;

    
    
}
