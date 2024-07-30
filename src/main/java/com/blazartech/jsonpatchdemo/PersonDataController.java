/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import com.blazartech.jsonpatchdemo.data.AddressData;
import com.blazartech.jsonpatchdemo.data.PersonData;
import com.blazartech.jsonpatchdemo.data.PersonDataRepository;
import com.blazartech.jsonpatchdemo.data.RoleData;
import com.blazartech.jsonpatchdemo.data.RoleDataRepository;
import com.blazartech.jsonpatchdemo.response.AddressView;
import com.blazartech.jsonpatchdemo.response.PersonView;
import com.blazartech.jsonpatchdemo.response.RoleView;
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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @Autowired
    private RoleDataRepository roleRepository;
    
    private RoleView buildRoleView(RoleData roleData) {
        RoleView v = new RoleView();
        v.setId(roleData.getRoleId());
        v.setRoleType(roleData.getRoleType());
        v.setStartDate(roleData.getStartDate());
        v.setEndDate(roleData.getEndDate());
        v.setPersonId(roleData.getPerson().getId());
        
        return v;
    }
    
    private RoleData buildRoleData(RoleView v) {
        RoleData d = new RoleData();
        d.setEndDate(v.getEndDate());
        d.setPerson(personRepository.findById(v.getPersonId()).orElseThrow());
        d.setRoleId(v.getId());
        d.setRoleType(v.getRoleType());
        d.setStartDate(v.getStartDate());
        
        return d;
    }
    
    private AddressView buildAddressView(AddressData d) {
        AddressView v = new AddressView();
        v.setId(d.getId());
        v.setStateText(d.getStateText());
        v.setStreetText(d.getStreetText());
        return v;
    }
    
    private AddressData buildAddressData(AddressView v) {
        AddressData d = new AddressData();
        d.setId(v.getId());
        d.setStateText(v.getStateText());
        d.setStreetText(v.getStateText());
        return d;
    }
    
    private PersonView buildPersonView(PersonData d) {
        if (d == null) { return null; }
        
        PersonView v = new PersonView();
        v.setId(d.getId());
        v.setAddress(buildAddressView(d.getAddress()));
        v.setBirthDate(d.getBirthDate());
        v.setDeathDate(d.getDeathDate());
        v.setName(d.getName());
        
        List<RoleView> roles = d.getRoles()
                .stream()
                .map(r -> buildRoleView(r))
                .collect(Collectors.toList());
        v.setRoles(roles);
        
        return v;
    }
    
    private PersonData buildPersonData(PersonView v) {
        PersonData d = new PersonData();
        d.setAddress(buildAddressData(v.getAddress()));
        d.setBirthDate(v.getBirthDate());
        d.setDeathDate(v.getDeathDate());
        d.setId(v.getId());
        d.setName(v.getName());
        
        List<RoleData> roles = v.getRoles().stream()
                .map(r -> buildRoleData(r))
                .collect(Collectors.toList());
        
        d.setRoles(roles);
        
        return d;
    }

    @GetMapping(path = "/person")
    @Operation(summary = "get the list of all people")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got the list",
                content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonView.class)))
                })
    })
    public List<PersonView> getPersons() {
        return personRepository.findAll().stream()
                .map(p -> buildPersonView(p))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/person/{id}")
    @Operation(summary = "get person by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonView.class))
                })
    })
    public PersonView getPerson(@PathVariable long id) {
        log.info("getting person by ID {}", id);

        PersonData person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return buildPersonView(person);
    }

    @PutMapping(path = "/person/{personId}/roles/{roleId}")
    @Operation(summary = "update via PUT a role for a person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "updated role",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleView.class))
                })
    })
    public RoleView putRole(@PathVariable long personId, @PathVariable long roleId, @RequestBody RoleView updatedRole) {
        log.info("getting role by ID {}", roleId);

        RoleData role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new IllegalArgumentException("no role " + roleId + " found");
        }
        
        role.setEndDate(updatedRole.getEndDate());
        role.setRoleType(updatedRole.getRoleType());
        role.setStartDate(updatedRole.getStartDate());
        roleRepository.save(role);
        
        return updatedRole;
    }
    
    @GetMapping(path = "/roles")
    public List<RoleView> getRoles() {
        log.info("getting all roles");
        
        return roleRepository.findAll()
                .stream()
                .map(r -> buildRoleView(r))
                .collect(Collectors.toList());
    }
    
    @PostMapping(path = "/person/{personId}/roles")
    @Operation(summary = "add a new role for a person")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created role",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleView.class))
                })
    })
    @Transactional
    public RoleView postRole(@PathVariable long personId, @RequestBody RoleView newRole) {
        log.info("adding new role {} to person ID {}", newRole, personId);

        RoleData r = new RoleData();
        r.setEndDate(newRole.getEndDate());
        r.setRoleType(newRole.getRoleType());
        r.setStartDate(newRole.getStartDate());
        
        PersonData person = personRepository.findById(personId).orElseThrow(IllegalArgumentException::new);
        
        r.setPerson(person);

        roleRepository.saveAndFlush(r);

        person.getRoles().add(r);        
        personRepository.save(person);
        
        newRole.setId(r.getRoleId());
        return newRole;
    }
    
    @PutMapping(path = "/person/{id}")
    @Operation(summary = "update a person via PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successfully updated person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonView.class))
                })
    })
    public PersonView putPerson(@PathVariable long id, @RequestBody PersonView updatedPerson) {
        log.info("updating person {} via PUT", id);
        
        PersonData p = personRepository.findById(id).orElseThrow();
        List<RoleData> newRoles = updatedPerson.getRoles().stream()
                .map(r -> buildRoleData(r))
                .collect(Collectors.toList());
        p.setRoles(newRoles);
        
        personRepository.saveAndFlush(p);
        return updatedPerson;
    }

    /**
     * the main thing, the JSON patch. Derived from
     * https://stackoverflow.com/questions/72491333/how-to-document-json-merge-patch-endpoints-in-openapi
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
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonView.class))
                })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(array = @ArraySchema(schema = @Schema(implementation = JsonPatchSchema.class))))
    @Transactional
    public PersonView patchPerson(@Parameter(description = "id of the object to be patched") @PathVariable long id, @Parameter(description = "the patches") @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        log.info("updating person {}", id);

        PersonData person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        
        PersonView personView = buildPersonView(person);
        
        PersonView patchedPerson = applyPatchToPerson(jsonPatch, personView);

        log.info("updated person = {}", patchedPerson);

        personRepository.saveAndFlush(buildPersonData(patchedPerson));

        return patchedPerson;
    }

    @Autowired
    private ObjectMapper objectMapper;

    private PersonView applyPatchToPerson(JsonPatch patch, PersonView targetPerson) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetPerson, JsonNode.class));
        return objectMapper.treeToValue(patched, PersonView.class);
    }

    @PostMapping(path = "/person")
    @Operation(summary = "add a new person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "added person",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonView.class))
                })
    })
    @ResponseStatus(HttpStatus.CREATED)
    public PersonView addPerson(@RequestBody PersonView person) {
        log.info("adding person {}", person);
        
        PersonData p = buildPersonData(person);
        personRepository.save(p);
        person.setId(p.getId());
        
        return person;
    }
}
