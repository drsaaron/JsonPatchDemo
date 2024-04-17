/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import com.blazartech.jsonpatchdemo.data.PersonData;
import com.blazartech.jsonpatchdemo.data.PersonDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aar1069
 */
@RestController
@Slf4j
@OpenAPIDefinition(
        info = @Info(
                title = "JSON Patch Demo",
                version = "1.0"
        ),
        extensions = {
            @Extension(
                    name = "blazarCustomTags2",
                    properties = {
                        @ExtensionProperty(name = "domain", value = "blazar"),
                        @ExtensionProperty(name = "author", value = "me, myself, and I")
                    })
        },
        tags = {
            @Tag(name = "myTag")
        }
)
public class PersonDataController {

    @Autowired
    private PersonDataRepository personRepository;

    @GetMapping(path = "/person")
    @Operation(summary = "get the list of all people")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got the list",
                content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonData.class)))
                })
    })
    public List<PersonData> getPersons() {
        return personRepository.findAll();
    }

    @GetMapping(path = "/person/{id}")
    @Operation(summary = "get person by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonData.class))
                })
    })
    public PersonData getPerson(@PathVariable long id) {
        log.info("getting person by ID {}", id);

        Optional<PersonData> person = personRepository.findById(id);
        return person.orElse(null);
    }

    /**
     * the main thing, the JSON patch.  Derived from https://stackoverflow.com/questions/72491333/how-to-document-json-merge-patch-endpoints-in-openapi
     * 
     * @param id ID of the object to be patched
     * @param jsonPatch the patches
     * @return patched object
     * @throws JsonPatchException
     * @throws JsonProcessingException 
     */
    @PatchMapping(path = "/person/{id}", consumes = "application/json-patch+json")
    @Operation(summary = "patch update a person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successfully updated person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonData.class))
                })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(array = @ArraySchema(schema = @Schema(implementation = JsonPatchSchema.class))))
    public PersonData updatePerson(@Parameter(description = "id of the object to be patched") @PathVariable long id, @Parameter(description = "the patches") @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        log.info("updating person {}", id);

        PersonData person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        PersonData patchedPerson = applyPatchToPerson(jsonPatch, person);
        
        log.info("updated person = {}", patchedPerson);
        
        personRepository.save(patchedPerson);
        
        return patchedPerson;
    }

    @Autowired
    private ObjectMapper objectMapper;

    private PersonData applyPatchToPerson(JsonPatch patch, PersonData targetPerson) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetPerson, JsonNode.class));
        return objectMapper.treeToValue(patched, PersonData.class);
    }
    
    @PostMapping(path = "/person")
    @Operation(summary = "add a new person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "added person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonData.class))
                })
    })
    @ResponseStatus(HttpStatus.CREATED)
    public PersonData addPerson(@RequestBody PersonData person) {
        log.info("adding person {}", person);
        personRepository.save(person);
        return person;
    }
}
