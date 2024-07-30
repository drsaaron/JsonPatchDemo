/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo.response;

import com.blazartech.jsonpatchdemo.data.RoleData.RoleType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author aar1069
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleView {

    private Long id;
    private RoleType roleType;
    private LocalDate startDate;
    private LocalDate endDate;
    private long personId;
}
