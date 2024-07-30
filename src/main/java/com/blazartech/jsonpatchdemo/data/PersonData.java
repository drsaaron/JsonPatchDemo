/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(exclude = { "roles" }) // exclude roles as person references role which references person which references ...
public class PersonData {

    public PersonData(String name, LocalDate birthDate, LocalDate deathDate, AddressData address) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.address = address;
    }
    
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
    private LocalDate birthDate;
    
    @Column(name = "death_dte", nullable = true) 
    @Schema(description = "the death date of the person")
    @Null
    private LocalDate deathDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private AddressData address;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private List<RoleData> roles;
    
}
