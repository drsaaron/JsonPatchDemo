/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "person_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleData {
    
    public static enum RoleType { Manager, Staff, Sales };
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id")
    @Schema(description = "the ID of the role")
    @NotNull
    private Long roleId; 
    
    @Column(name = "role_type", nullable = false)
    @Schema(description = "the role type")
    @NotNull
    private RoleType roleType;
    
    @Column(name = "start_dte", nullable = false) 
    @Schema(description = "start date of the role")
    @NotNull
    private LocalDate startDate;
    
    @Column(name = "end_dte", nullable = true) 
    @Schema(description = "ebd date of the role")
    @Null
    private LocalDate endDate;

/*    @Column(name = "person_id", nullable = false) 
    @Schema(description = "id of the person to whom the role is attached")
    private Long personId;*/

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne(optional = false)
    private PersonData person;
}
