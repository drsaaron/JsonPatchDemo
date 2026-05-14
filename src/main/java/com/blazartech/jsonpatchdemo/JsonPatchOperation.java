/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import lombok.Data;

/**
 * another representation of a JSON patch operation, that will be used in the
 * actual code.  We apparently can't use the schema object because the value
 * there is a string and it needs to be an object.
 * 
 * @author aar1069
 */
@Data
public class JsonPatchOperation {

    private String op;
    private String path;
    private Object value;

}
