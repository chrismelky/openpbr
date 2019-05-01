package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.PlanningUnit;
import org.openpbr.domain.PlanningUnit;
import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.repository.PlanningUnitRepository;
import org.openpbr.repository.search.PlanningUnitSearchRepository;
import org.openpbr.service.PlanningUnitService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.PlanningUnitCriteria;
import org.openpbr.service.PlanningUnitQueryService;

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
 * Test class for the PlanningUnitResource REST controller.
 *
 * @see PlanningUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class PlanningUnitResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private PlanningUnitRepository planningUnitRepository;

    @Autowired
    private PlanningUnitService planningUnitService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.PlanningUnitSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanningUnitSearchRepository mockPlanningUnitSearchRepository;

    @Autowired
    private PlanningUnitQueryService planningUnitQueryService;

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

    private MockMvc restPlanningUnitMockMvc;

    private PlanningUnit planningUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningUnitResource planningUnitResource = new PlanningUnitResource(planningUnitService, planningUnitQueryService);
        this.restPlanningUnitMockMvc = MockMvcBuilders.standaloneSetup(planningUnitResource)
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
    public static PlanningUnit createEntity(EntityManager em) {
        PlanningUnit planningUnit = new PlanningUnit()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        return planningUnit;
    }

    @Before
    public void initTest() {
        planningUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanningUnit() throws Exception {
        int databaseSizeBeforeCreate = planningUnitRepository.findAll().size();

        // Create the PlanningUnit
        restPlanningUnitMockMvc.perform(post("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isCreated());

        // Validate the PlanningUnit in the database
        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeCreate + 1);
        PlanningUnit testPlanningUnit = planningUnitList.get(planningUnitList.size() - 1);
        assertThat(testPlanningUnit.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPlanningUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPlanningUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanningUnit.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testPlanningUnit.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testPlanningUnit.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the PlanningUnit in Elasticsearch
        verify(mockPlanningUnitSearchRepository, times(1)).save(testPlanningUnit);
    }

    @Test
    @Transactional
    public void createPlanningUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningUnitRepository.findAll().size();

        // Create the PlanningUnit with an existing ID
        planningUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningUnitMockMvc.perform(post("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnit in the database
        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanningUnit in Elasticsearch
        verify(mockPlanningUnitSearchRepository, times(0)).save(planningUnit);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitRepository.findAll().size();
        // set the field null
        planningUnit.setUid(null);

        // Create the PlanningUnit, which fails.

        restPlanningUnitMockMvc.perform(post("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isBadRequest());

        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitRepository.findAll().size();
        // set the field null
        planningUnit.setName(null);

        // Create the PlanningUnit, which fails.

        restPlanningUnitMockMvc.perform(post("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isBadRequest());

        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitRepository.findAll().size();
        // set the field null
        planningUnit.setLevel(null);

        // Create the PlanningUnit, which fails.

        restPlanningUnitMockMvc.perform(post("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isBadRequest());

        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanningUnits() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList
        restPlanningUnitMockMvc.perform(get("/api/planning-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlanningUnit() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get the planningUnit
        restPlanningUnitMockMvc.perform(get("/api/planning-units/{id}", planningUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planningUnit.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where uid equals to DEFAULT_UID
        defaultPlanningUnitShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the planningUnitList where uid equals to UPDATED_UID
        defaultPlanningUnitShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where uid in DEFAULT_UID or UPDATED_UID
        defaultPlanningUnitShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the planningUnitList where uid equals to UPDATED_UID
        defaultPlanningUnitShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where uid is not null
        defaultPlanningUnitShouldBeFound("uid.specified=true");

        // Get all the planningUnitList where uid is null
        defaultPlanningUnitShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where code equals to DEFAULT_CODE
        defaultPlanningUnitShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the planningUnitList where code equals to UPDATED_CODE
        defaultPlanningUnitShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPlanningUnitShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the planningUnitList where code equals to UPDATED_CODE
        defaultPlanningUnitShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where code is not null
        defaultPlanningUnitShouldBeFound("code.specified=true");

        // Get all the planningUnitList where code is null
        defaultPlanningUnitShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where name equals to DEFAULT_NAME
        defaultPlanningUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the planningUnitList where name equals to UPDATED_NAME
        defaultPlanningUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlanningUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the planningUnitList where name equals to UPDATED_NAME
        defaultPlanningUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where name is not null
        defaultPlanningUnitShouldBeFound("name.specified=true");

        // Get all the planningUnitList where name is null
        defaultPlanningUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where level equals to DEFAULT_LEVEL
        defaultPlanningUnitShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the planningUnitList where level equals to UPDATED_LEVEL
        defaultPlanningUnitShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultPlanningUnitShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the planningUnitList where level equals to UPDATED_LEVEL
        defaultPlanningUnitShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where level is not null
        defaultPlanningUnitShouldBeFound("level.specified=true");

        // Get all the planningUnitList where level is null
        defaultPlanningUnitShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where level greater than or equals to DEFAULT_LEVEL
        defaultPlanningUnitShouldBeFound("level.greaterOrEqualThan=" + DEFAULT_LEVEL);

        // Get all the planningUnitList where level greater than or equals to UPDATED_LEVEL
        defaultPlanningUnitShouldNotBeFound("level.greaterOrEqualThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where level less than or equals to DEFAULT_LEVEL
        defaultPlanningUnitShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the planningUnitList where level less than or equals to UPDATED_LEVEL
        defaultPlanningUnitShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }


    @Test
    @Transactional
    public void getAllPlanningUnitsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultPlanningUnitShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the planningUnitList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where sortOrder is not null
        defaultPlanningUnitShouldBeFound("sortOrder.specified=true");

        // Get all the planningUnitList where sortOrder is null
        defaultPlanningUnitShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllPlanningUnitsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPlanningUnitShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the planningUnitList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPlanningUnitShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the planningUnitList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitRepository.saveAndFlush(planningUnit);

        // Get all the planningUnitList where isActive is not null
        defaultPlanningUnitShouldBeFound("isActive.specified=true");

        // Get all the planningUnitList where isActive is null
        defaultPlanningUnitShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        PlanningUnit parent = PlanningUnitResourceIntTest.createEntity(em);
        em.persist(parent);
        em.flush();
        planningUnit.setParent(parent);
        planningUnitRepository.saveAndFlush(planningUnit);
        Long parentId = parent.getId();

        // Get all the planningUnitList where parent equals to parentId
        defaultPlanningUnitShouldBeFound("parentId.equals=" + parentId);

        // Get all the planningUnitList where parent equals to parentId + 1
        defaultPlanningUnitShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanningUnitsByPlanningUnitGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        PlanningUnitGroup planningUnitGroup = PlanningUnitGroupResourceIntTest.createEntity(em);
        em.persist(planningUnitGroup);
        em.flush();
        planningUnit.addPlanningUnitGroup(planningUnitGroup);
        planningUnitRepository.saveAndFlush(planningUnit);
        Long planningUnitGroupId = planningUnitGroup.getId();

        // Get all the planningUnitList where planningUnitGroup equals to planningUnitGroupId
        defaultPlanningUnitShouldBeFound("planningUnitGroupId.equals=" + planningUnitGroupId);

        // Get all the planningUnitList where planningUnitGroup equals to planningUnitGroupId + 1
        defaultPlanningUnitShouldNotBeFound("planningUnitGroupId.equals=" + (planningUnitGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPlanningUnitShouldBeFound(String filter) throws Exception {
        restPlanningUnitMockMvc.perform(get("/api/planning-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPlanningUnitMockMvc.perform(get("/api/planning-units/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPlanningUnitShouldNotBeFound(String filter) throws Exception {
        restPlanningUnitMockMvc.perform(get("/api/planning-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanningUnitMockMvc.perform(get("/api/planning-units/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanningUnit() throws Exception {
        // Get the planningUnit
        restPlanningUnitMockMvc.perform(get("/api/planning-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningUnit() throws Exception {
        // Initialize the database
        planningUnitService.save(planningUnit);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanningUnitSearchRepository);

        int databaseSizeBeforeUpdate = planningUnitRepository.findAll().size();

        // Update the planningUnit
        PlanningUnit updatedPlanningUnit = planningUnitRepository.findById(planningUnit.getId()).get();
        // Disconnect from session so that the updates on updatedPlanningUnit are not directly saved in db
        em.detach(updatedPlanningUnit);
        updatedPlanningUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanningUnitMockMvc.perform(put("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanningUnit)))
            .andExpect(status().isOk());

        // Validate the PlanningUnit in the database
        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeUpdate);
        PlanningUnit testPlanningUnit = planningUnitList.get(planningUnitList.size() - 1);
        assertThat(testPlanningUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPlanningUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPlanningUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanningUnit.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testPlanningUnit.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testPlanningUnit.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the PlanningUnit in Elasticsearch
        verify(mockPlanningUnitSearchRepository, times(1)).save(testPlanningUnit);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanningUnit() throws Exception {
        int databaseSizeBeforeUpdate = planningUnitRepository.findAll().size();

        // Create the PlanningUnit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningUnitMockMvc.perform(put("/api/planning-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnit)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnit in the database
        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanningUnit in Elasticsearch
        verify(mockPlanningUnitSearchRepository, times(0)).save(planningUnit);
    }

    @Test
    @Transactional
    public void deletePlanningUnit() throws Exception {
        // Initialize the database
        planningUnitService.save(planningUnit);

        int databaseSizeBeforeDelete = planningUnitRepository.findAll().size();

        // Delete the planningUnit
        restPlanningUnitMockMvc.perform(delete("/api/planning-units/{id}", planningUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningUnit> planningUnitList = planningUnitRepository.findAll();
        assertThat(planningUnitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanningUnit in Elasticsearch
        verify(mockPlanningUnitSearchRepository, times(1)).deleteById(planningUnit.getId());
    }

    @Test
    @Transactional
    public void searchPlanningUnit() throws Exception {
        // Initialize the database
        planningUnitService.save(planningUnit);
        when(mockPlanningUnitSearchRepository.search(queryStringQuery("id:" + planningUnit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planningUnit), PageRequest.of(0, 1), 1));
        // Search the planningUnit
        restPlanningUnitMockMvc.perform(get("/api/_search/planning-units?query=id:" + planningUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningUnit.class);
        PlanningUnit planningUnit1 = new PlanningUnit();
        planningUnit1.setId(1L);
        PlanningUnit planningUnit2 = new PlanningUnit();
        planningUnit2.setId(planningUnit1.getId());
        assertThat(planningUnit1).isEqualTo(planningUnit2);
        planningUnit2.setId(2L);
        assertThat(planningUnit1).isNotEqualTo(planningUnit2);
        planningUnit1.setId(null);
        assertThat(planningUnit1).isNotEqualTo(planningUnit2);
    }
}
