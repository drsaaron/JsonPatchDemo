/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import com.blazartech.jsonpatchdemo.config.DataSourceConfiguration;
import com.blazartech.jsonpatchdemo.data.AddressData;
import com.blazartech.jsonpatchdemo.data.PersonData;
import com.blazartech.jsonpatchdemo.data.PersonDataRepository;
import com.blazartech.jsonpatchdemo.data.RoleData;
import com.blazartech.jsonpatchdemo.data.RoleDataRepository;
import com.blazartech.jsonpatchdemo.response.PersonView;
import com.blazartech.jsonpatchdemo.response.RoleView;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author aar1069
 */
@ExtendWith(SpringExtension.class)
@Slf4j
@WebMvcTest(PersonDataController.class)
@ContextConfiguration(classes = {
    PersonDataControllerTest.PersonDataControllerTestConfiguration.class,
    DataSourceConfiguration.class
})
@Transactional
public class PersonDataControllerTest {

    @Configuration
    @EnableJpaRepositories(basePackages = {"com.blazartech.jsonpatchdemo.data"})
    public static class PersonDataControllerTestConfiguration {

        @Autowired
        private DataSource dataSource;

        @Bean
        public PersonDataController instance() {
            return new PersonDataController();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter jpaVendorAdapter) {
            LocalContainerEntityManagerFactoryBean f = new LocalContainerEntityManagerFactoryBean();
            f.setDataSource(dataSource);
            f.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
            f.setPersistenceUnitName("jsonpatch-PU");
            f.setJpaVendorAdapter(jpaVendorAdapter);

            return f;
        }

        @Bean
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter va  = new HibernateJpaVendorAdapter();
            va.setShowSql(true);
            va.setDatabase(Database.H2);
            va.setGenerateDdl(true);
            return va;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
            JpaTransactionManager m = new JpaTransactionManager(emf);
            return m;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonDataController instance;

    @Autowired
    private PersonDataRepository personRepo;

    @Autowired
    private RoleDataRepository roleRepo;

    public PersonDataControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

        PersonData person1 = new PersonData("PERSON1", LocalDate.parse("1960-10-01"), null, new AddressData(null, "stree1", "state1"));
        personRepo.save(person1);

        RoleData role1 = new RoleData(null, RoleData.RoleType.Sales, LocalDate.parse("1980-12-01"), null, person1);
        ArrayList<RoleData> roles = new ArrayList<>();
        roles.add(role1);
        person1.setRoles(roles);
        personRepo.save(person1);
    }

    @AfterEach
    public void tearDown() {
    }

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test of getPersons method, of class PersonDataController.
     */
    @Test
    public void testGetPersons() throws Exception {
        log.info("getPersons");

        MvcResult result = mockMvc
                .perform(get("/person"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        PersonView[] persons = objectMapper.readValue(response, PersonView[].class);

        assertEquals(1, persons.length);

        List<RoleView> roles = persons[0].getRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    /**
     * Test of getPerson method, of class PersonDataController.
     */
    @Test
    public void testGetPerson() throws Exception {
        log.info("getPerson");
        
        PersonData person = personRepo.findAll().getFirst();
        Long personId = person.getId();
        
        PersonView pv = getPerson(personId);
        assertNotNull(pv);
        assertEquals("PERSON1", pv.getName());
        assertEquals(1, pv.getRoles().size());        
    }

    /**
     * Test of putRole method, of class PersonDataController.
     */
    //   @Test
    public void testPutRole() {
        System.out.println("putRole");
        long personId = 0L;
        long roleId = 0L;
        RoleView updatedRole = null;
        PersonDataController instance = new PersonDataController();
        RoleView expResult = null;
        RoleView result = instance.putRole(personId, roleId, updatedRole);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoles method, of class PersonDataController.
     */
    //  @Test
    public void testGetRoles() {
        System.out.println("getRoles");
        PersonDataController instance = new PersonDataController();
        List<RoleView> expResult = null;
        List<RoleView> result = instance.getRoles();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postRole method, of class PersonDataController.
     */
    //  @Test
    public void testPostRole() {
        System.out.println("postRole");
        long personId = 0L;
        RoleView newRole = null;
        PersonDataController instance = new PersonDataController();
        RoleView expResult = null;
        RoleView result = instance.postRole(personId, newRole);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private PersonView getPerson(long personId) throws Exception {
        MvcResult result = mockMvc
                .perform(get("/person/" + personId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        log.info("person response = {}", response);
        PersonView origPerson = objectMapper.readValue(response, PersonView.class);
        
        return origPerson;
    }
    
    /**
     * Test of putPerson method, of class PersonDataController.
     */
    @Test
    public void testPutPerson() throws Exception {
        log.info("putPerson");

        PersonData person = personRepo.findAll().getFirst();
        Long personId = person.getId();

        // get the current state
        PersonView origPerson = getPerson(personId);

        // end-date current roles and add new one.
        RoleView role1 = origPerson.getRoles().getFirst();
        role1.setEndDate(LocalDate.parse("2025-12-31"));

        RoleView role2 = new RoleView(null, RoleData.RoleType.Staff, LocalDate.parse("2026-01-01"), null, personId);
        origPerson.getRoles().add(role2);

        String jsonString = objectMapper.writeValueAsString(origPerson);
        log.info("jsonString = {}", jsonString);

        // update via put
        mockMvc
                .perform(
                        put("/person/" + personId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Now retrieve the person via API
        PersonView updatedPerson = getPerson(personId);

        // should now have two roles
        List<RoleView> roles = updatedPerson.getRoles();
        assertEquals(2, roles.size());
    }

    /**
     * Test of patchPerson method, of class PersonDataController.
     */
    @Test
    public void testPatchPerson() throws Exception {
        log.info("patchPerson");

        PersonData person = personRepo.findAll().getFirst();
        Long personId = person.getId();

        // update the person via patch
        mockMvc
                .perform(
                        patch("/person/" + personId)
                                .contentType("application/json-patch+json")
                                .content("[ { \"op\": \"replace\", \"path\": \"/deathDate\", \"value\": \"1600-02-15\" }, { \"op\": \"replace\", \"path\": \"/address/stateText\", \"value\": \"EU\" }, {    \"op\": \"replace\",    \"path\": \"/roles/0/endDate\",    \"value\": \"2024-12-31\"  },  {     \"op\": \"add\",    \"path\": \"/roles/-\",    \"value\": { \"roleType\": \"Staff\", \"startDate\": \"2025-01-01\", \"personId\": 2 }  } ]")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // call the API for the person and make sure it's actually updated
        PersonView updatedPerson = getPerson(personId);

        // check that death and address state were updated as requested
        assertNotNull(updatedPerson.getDeathDate());
        assertEquals("EU", updatedPerson.getAddress().getStateText());

        // we should now have 2 roles, since we end-dated one and added a new
        List<RoleView> roles = updatedPerson.getRoles();
        assertNotNull(roles);
        assertEquals(2, roles.size());

        // first role should now be end-dated
        RoleView role1 = roles.get(0);
        assertNotNull(role1.getEndDate());
    }

    /**
     * Test of addPerson method, of class PersonDataController.
     */
    //  @Test
    public void testAddPerson() {
        System.out.println("addPerson");
        PersonView person = null;
        PersonDataController instance = new PersonDataController();
        PersonView expResult = null;
        PersonView result = instance.addPerson(person);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
