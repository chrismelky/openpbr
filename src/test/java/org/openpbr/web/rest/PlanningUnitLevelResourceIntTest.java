package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.PlanningUnitLevel;
import org.openpbr.repository.PlanningUnitLevelRepository;
import org.openpbr.repository.search.PlanningUnitLevelSearchRepository;
import org.openpbr.service.PlanningUnitLevelService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.PlanningUnitLevelCriteria;
import org.openpbr.service.PlanningUnitLevelQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static org.openpbr.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlanningUnitLevelResource REST controller.
 *
 * @see PlanningUnitLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class PlanningUnitLevelResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private PlanningUnitLevelRepository planningUnitLevelRepository;

    @Autowired
    private PlanningUnitLevelService planningUnitLevelService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.PlanningUnitLevelSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanningUnitLevelSearchRepository mockPlanningUnitLevelSearchRepository;

    @Autowired
    private PlanningUnitLevelQueryService planningUnitLevelQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPlanningUnitLevelMockMvc;

    private PlanningUnitLevel planningUnitLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningUnitLevelResource planningUnitLevelResource = new PlanningUnitLevelResource(planningUnitLevelService, planningUnitLevelQueryService);
        this.restPlanningUnitLevelMockMvc = MockMvcBuilders.standaloneSetup(planningUnitLevelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanningUnitLevel createEntity(EntityManager em) {
        PlanningUnitLevel planningUnitLevel = new PlanningUnitLevel()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL)
            .isActive(DEFAULT_IS_ACTIVE);
        return planningUnitLevel;
    }

    @Before
    public void initTest() {
        planningUnitLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanningUnitLevel() throws Exception {
        int databaseSizeBeforeCreate = planningUnitLevelRepository.findAll().size();

        // Create the PlanningUnitLevel
        restPlanningUnitLevelMockMvc.perform(post("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isCreated());

        // Validate the PlanningUnitLevel in the database
        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeCreate + 1);
        PlanningUnitLevel testPlanningUnitLevel = planningUnitLevelList.get(planningUnitLevelList.size() - 1);
        assertThat(testPlanningUnitLevel.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPlanningUnitLevel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPlanningUnitLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanningUnitLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testPlanningUnitLevel.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the PlanningUnitLevel in Elasticsearch
        verify(mockPlanningUnitLevelSearchRepository, times(1)).save(testPlanningUnitLevel);
    }

    @Test
    @Transactional
    public void createPlanningUnitLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningUnitLevelRepository.findAll().size();

        // Create the PlanningUnitLevel with an existing ID
        planningUnitLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningUnitLevelMockMvc.perform(post("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitLevel in the database
        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanningUnitLevel in Elasticsearch
        verify(mockPlanningUnitLevelSearchRepository, times(0)).save(planningUnitLevel);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitLevelRepository.findAll().size();
        // set the field null
        planningUnitLevel.setUid(null);

        // Create the PlanningUnitLevel, which fails.

        restPlanningUnitLevelMockMvc.perform(post("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitLevelRepository.findAll().size();
        // set the field null
        planningUnitLevel.setName(null);

        // Create the PlanningUnitLevel, which fails.

        restPlanningUnitLevelMockMvc.perform(post("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitLevelRepository.findAll().size();
        // set the field null
        planningUnitLevel.setLevel(null);

        // Create the PlanningUnitLevel, which fails.

        restPlanningUnitLevelMockMvc.perform(post("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevels() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlanningUnitLevel() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get the planningUnitLevel
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels/{id}", planningUnitLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planningUnitLevel.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where uid equals to DEFAULT_UID
        defaultPlanningUnitLevelShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the planningUnitLevelList where uid equals to UPDATED_UID
        defaultPlanningUnitLevelShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where uid in DEFAULT_UID or UPDATED_UID
        defaultPlanningUnitLevelShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the planningUnitLevelList where uid equals to UPDATED_UID
        defaultPlanningUnitLevelShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where uid is not null
        defaultPlanningUnitLevelShouldBeFound("uid.specified=true");

        // Get all the planningUnitLevelList where uid is null
        defaultPlanningUnitLevelShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where code equals to DEFAULT_CODE
        defaultPlanningUnitLevelShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the planningUnitLevelList where code equals to UPDATED_CODE
        defaultPlanningUnitLevelShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPlanningUnitLevelShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the planningUnitLevelList where code equals to UPDATED_CODE
        defaultPlanningUnitLevelShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where code is not null
        defaultPlanningUnitLevelShouldBeFound("code.specified=true");

        // Get all the planningUnitLevelList where code is null
        defaultPlanningUnitLevelShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where name equals to DEFAULT_NAME
        defaultPlanningUnitLevelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the planningUnitLevelList where name equals to UPDATED_NAME
        defaultPlanningUnitLevelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlanningUnitLevelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the planningUnitLevelList where name equals to UPDATED_NAME
        defaultPlanningUnitLevelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where name is not null
        defaultPlanningUnitLevelShouldBeFound("name.specified=true");

        // Get all the planningUnitLevelList where name is null
        defaultPlanningUnitLevelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where level equals to DEFAULT_LEVEL
        defaultPlanningUnitLevelShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the planningUnitLevelList where level equals to UPDATED_LEVEL
        defaultPlanningUnitLevelShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultPlanningUnitLevelShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the planningUnitLevelList where level equals to UPDATED_LEVEL
        defaultPlanningUnitLevelShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where level is not null
        defaultPlanningUnitLevelShouldBeFound("level.specified=true");

        // Get all the planningUnitLevelList where level is null
        defaultPlanningUnitLevelShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where level greater than or equals to DEFAULT_LEVEL
        defaultPlanningUnitLevelShouldBeFound("level.greaterOrEqualThan=" + DEFAULT_LEVEL);

        // Get all the planningUnitLevelList where level greater than or equals to UPDATED_LEVEL
        defaultPlanningUnitLevelShouldNotBeFound("level.greaterOrEqualThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where level less than or equals to DEFAULT_LEVEL
        defaultPlanningUnitLevelShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the planningUnitLevelList where level less than or equals to UPDATED_LEVEL
        defaultPlanningUnitLevelShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }


    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPlanningUnitLevelShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the planningUnitLevelList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitLevelShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPlanningUnitLevelShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the planningUnitLevelList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitLevelShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitLevelsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitLevelRepository.saveAndFlush(planningUnitLevel);

        // Get all the planningUnitLevelList where isActive is not null
        defaultPlanningUnitLevelShouldBeFound("isActive.specified=true");

        // Get all the planningUnitLevelList where isActive is null
        defaultPlanningUnitLevelShouldNotBeFound("isActive.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPlanningUnitLevelShouldBeFound(String filter) throws Exception {
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPlanningUnitLevelShouldNotBeFound(String filter) throws Exception {
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanningUnitLevel() throws Exception {
        // Get the planningUnitLevel
        restPlanningUnitLevelMockMvc.perform(get("/api/planning-unit-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningUnitLevel() throws Exception {
        // Initialize the database
        planningUnitLevelService.save(planningUnitLevel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanningUnitLevelSearchRepository);

        int databaseSizeBeforeUpdate = planningUnitLevelRepository.findAll().size();

        // Update the planningUnitLevel
        PlanningUnitLevel updatedPlanningUnitLevel = planningUnitLevelRepository.findById(planningUnitLevel.getId()).get();
        // Disconnect from session so that the updates on updatedPlanningUnitLevel are not directly saved in db
        em.detach(updatedPlanningUnitLevel);
        updatedPlanningUnitLevel
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanningUnitLevelMockMvc.perform(put("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanningUnitLevel)))
            .andExpect(status().isOk());

        // Validate the PlanningUnitLevel in the database
        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeUpdate);
        PlanningUnitLevel testPlanningUnitLevel = planningUnitLevelList.get(planningUnitLevelList.size() - 1);
        assertThat(testPlanningUnitLevel.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPlanningUnitLevel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPlanningUnitLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanningUnitLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testPlanningUnitLevel.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the PlanningUnitLevel in Elasticsearch
        verify(mockPlanningUnitLevelSearchRepository, times(1)).save(testPlanningUnitLevel);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanningUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = planningUnitLevelRepository.findAll().size();

        // Create the PlanningUnitLevel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningUnitLevelMockMvc.perform(put("/api/planning-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitLevel)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitLevel in the database
        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanningUnitLevel in Elasticsearch
        verify(mockPlanningUnitLevelSearchRepository, times(0)).save(planningUnitLevel);
    }

    @Test
    @Transactional
    public void deletePlanningUnitLevel() throws Exception {
        // Initialize the database
        planningUnitLevelService.save(planningUnitLevel);

        int databaseSizeBeforeDelete = planningUnitLevelRepository.findAll().size();

        // Delete the planningUnitLevel
        restPlanningUnitLevelMockMvc.perform(delete("/api/planning-unit-levels/{id}", planningUnitLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningUnitLevel> planningUnitLevelList = planningUnitLevelRepository.findAll();
        assertThat(planningUnitLevelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanningUnitLevel in Elasticsearch
        verify(mockPlanningUnitLevelSearchRepository, times(1)).deleteById(planningUnitLevel.getId());
    }

    @Test
    @Transactional
    public void searchPlanningUnitLevel() throws Exception {
        // Initialize the database
        planningUnitLevelService.save(planningUnitLevel);
        when(mockPlanningUnitLevelSearchRepository.search(queryStringQuery("id:" + planningUnitLevel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planningUnitLevel), PageRequest.of(0, 1), 1));
        // Search the planningUnitLevel
        restPlanningUnitLevelMockMvc.perform(get("/api/_search/planning-unit-levels?query=id:" + planningUnitLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningUnitLevel.class);
        PlanningUnitLevel planningUnitLevel1 = new PlanningUnitLevel();
        planningUnitLevel1.setId(1L);
        PlanningUnitLevel planningUnitLevel2 = new PlanningUnitLevel();
        planningUnitLevel2.setId(planningUnitLevel1.getId());
        assertThat(planningUnitLevel1).isEqualTo(planningUnitLevel2);
        planningUnitLevel2.setId(2L);
        assertThat(planningUnitLevel1).isNotEqualTo(planningUnitLevel2);
        planningUnitLevel1.setId(null);
        assertThat(planningUnitLevel1).isNotEqualTo(planningUnitLevel2);
    }
}
