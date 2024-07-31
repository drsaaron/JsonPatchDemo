/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import com.blazartech.jsonpatchdemo.data.AddressData;
import com.blazartech.jsonpatchdemo.data.PersonData;
import com.blazartech.jsonpatchdemo.data.PersonDataRepository;
import com.blazartech.jsonpatchdemo.data.RoleData;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
@Profile("!unittest")
public class DataInitializerCommandLineRunner implements CommandLineRunner {

    @Autowired
    private PersonDataRepository personRepository;
    
    private PersonData addRoles(PersonData person) {
        List<RoleData> simpleRoles = List.of(new RoleData(null, RoleData.RoleType.Manager, LocalDate.parse("2024-01-01"), null, person));
        person.setRoles(simpleRoles);
        return person;
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("initializing data");     

        List<PersonData> people = List.of(
                new PersonData("Scott", LocalDate.parse("2000-01-01"), null, new AddressData(null, "circle drive", "DE")),
                new PersonData("Henry", LocalDate.parse("1480-10-01"), null, new AddressData(null, "palace", "UK")),
                new PersonData("Lancelot", LocalDate.parse("1210-06-20"), null, new AddressData(null, "camelot", "UK"))
        );
        
        people.stream()
                .map(p -> addRoles(p))
                .forEach(p -> personRepository.save(p));
        
    }
    
}
