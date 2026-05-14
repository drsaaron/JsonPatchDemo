/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author aar1069
 */
public class JsonPatchSchema {
    @NotBlank
    public Op op;

    public enum Op {
        replace, add, remove, copy, move, test
    }

    @NotBlank
    @Schema(example = "/name")
    public String path;

    @NotBlank
    @Schema(description = "the value to patch in.  For an object, this would be the json representation of the object.  For a native type, it's just the value")
    public Object value; 
}
