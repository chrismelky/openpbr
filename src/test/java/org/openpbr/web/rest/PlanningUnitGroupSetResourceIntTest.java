package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.PlanningUnitGroupSet;
import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.repository.PlanningUnitGroupSetRepository;
import org.openpbr.repository.search.PlanningUnitGroupSetSearchRepository;
import org.openpbr.service.PlanningUnitGroupSetService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.PlanningUnitGroupSetCriteria;
import org.openpbr.service.PlanningUnitGroupSetQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the PlanningUnitGroupSetResource REST controller.
 *
 * @see PlanningUnitGroupSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class PlanningUnitGroupSetResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private PlanningUnitGroupSetRepository planningUnitGroupSetRepository;

    @Mock
    private PlanningUnitGroupSetRepository planningUnitGroupSetRepositoryMock;

    @Mock
    private PlanningUnitGroupSetService planningUnitGroupSetServiceMock;

    @Autowired
    private PlanningUnitGroupSetService planningUnitGroupSetService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.PlanningUnitGroupSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanningUnitGroupSetSearchRepository mockPlanningUnitGroupSetSearchRepository;

    @Autowired
    private PlanningUnitGroupSetQueryService planningUnitGroupSetQueryService;

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

    private MockMvc restPlanningUnitGroupSetMockMvc;

    private PlanningUnitGroupSet planningUnitGroupSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningUnitGroupSetResource planningUnitGroupSetResource = new PlanningUnitGroupSetResource(planningUnitGroupSetService, planningUnitGroupSetQueryService);
        this.restPlanningUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupSetResource)
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
    public static PlanningUnitGroupSet createEntity(EntityManager em) {
        PlanningUnitGroupSet planningUnitGroupSet = new PlanningUnitGroupSet()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        PlanningUnitGroup planningUnitGroup = PlanningUnitGroupResourceIntTest.createEntity(em);
        em.persist(planningUnitGroup);
        em.flush();
        planningUnitGroupSet.getPlanningUnitGroups().add(planningUnitGroup);
        return planningUnitGroupSet;
    }

    @Before
    public void initTest() {
        planningUnitGroupSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanningUnitGroupSet() throws Exception {
        int databaseSizeBeforeCreate = planningUnitGroupSetRepository.findAll().size();

        // Create the PlanningUnitGroupSet
        restPlanningUnitGroupSetMockMvc.perform(post("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroupSet)))
            .andExpect(status().isCreated());

        // Validate the PlanningUnitGroupSet in the database
        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeCreate + 1);
        PlanningUnitGroupSet testPlanningUnitGroupSet = planningUnitGroupSetList.get(planningUnitGroupSetList.size() - 1);
        assertThat(testPlanningUnitGroupSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPlanningUnitGroupSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPlanningUnitGroupSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanningUnitGroupSet.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testPlanningUnitGroupSet.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the PlanningUnitGroupSet in Elasticsearch
        verify(mockPlanningUnitGroupSetSearchRepository, times(1)).save(testPlanningUnitGroupSet);
    }

    @Test
    @Transactional
    public void createPlanningUnitGroupSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningUnitGroupSetRepository.findAll().size();

        // Create the PlanningUnitGroupSet with an existing ID
        planningUnitGroupSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningUnitGroupSetMockMvc.perform(post("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitGroupSet in the database
        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanningUnitGroupSet in Elasticsearch
        verify(mockPlanningUnitGroupSetSearchRepository, times(0)).save(planningUnitGroupSet);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitGroupSetRepository.findAll().size();
        // set the field null
        planningUnitGroupSet.setUid(null);

        // Create the PlanningUnitGroupSet, which fails.

        restPlanningUnitGroupSetMockMvc.perform(post("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroupSet)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitGroupSetRepository.findAll().size();
        // set the field null
        planningUnitGroupSet.setName(null);

        // Create the PlanningUnitGroupSet, which fails.

        restPlanningUnitGroupSetMockMvc.perform(post("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroupSet)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSets() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPlanningUnitGroupSetsWithEagerRelationshipsIsEnabled() throws Exception {
        PlanningUnitGroupSetResource planningUnitGroupSetResource = new PlanningUnitGroupSetResource(planningUnitGroupSetServiceMock, planningUnitGroupSetQueryService);
        when(planningUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPlanningUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

        verify(planningUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPlanningUnitGroupSetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PlanningUnitGroupSetResource planningUnitGroupSetResource = new PlanningUnitGroupSetResource(planningUnitGroupSetServiceMock, planningUnitGroupSetQueryService);
            when(planningUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPlanningUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

            verify(planningUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlanningUnitGroupSet() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get the planningUnitGroupSet
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets/{id}", planningUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planningUnitGroupSet.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where uid equals to DEFAULT_UID
        defaultPlanningUnitGroupSetShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the planningUnitGroupSetList where uid equals to UPDATED_UID
        defaultPlanningUnitGroupSetShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where uid in DEFAULT_UID or UPDATED_UID
        defaultPlanningUnitGroupSetShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the planningUnitGroupSetList where uid equals to UPDATED_UID
        defaultPlanningUnitGroupSetShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where uid is not null
        defaultPlanningUnitGroupSetShouldBeFound("uid.specified=true");

        // Get all the planningUnitGroupSetList where uid is null
        defaultPlanningUnitGroupSetShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where code equals to DEFAULT_CODE
        defaultPlanningUnitGroupSetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the planningUnitGroupSetList where code equals to UPDATED_CODE
        defaultPlanningUnitGroupSetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPlanningUnitGroupSetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the planningUnitGroupSetList where code equals to UPDATED_CODE
        defaultPlanningUnitGroupSetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where code is not null
        defaultPlanningUnitGroupSetShouldBeFound("code.specified=true");

        // Get all the planningUnitGroupSetList where code is null
        defaultPlanningUnitGroupSetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where name equals to DEFAULT_NAME
        defaultPlanningUnitGroupSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the planningUnitGroupSetList where name equals to UPDATED_NAME
        defaultPlanningUnitGroupSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlanningUnitGroupSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the planningUnitGroupSetList where name equals to UPDATED_NAME
        defaultPlanningUnitGroupSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where name is not null
        defaultPlanningUnitGroupSetShouldBeFound("name.specified=true");

        // Get all the planningUnitGroupSetList where name is null
        defaultPlanningUnitGroupSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupSetShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupSetShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultPlanningUnitGroupSetShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the planningUnitGroupSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupSetShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where sortOrder is not null
        defaultPlanningUnitGroupSetShouldBeFound("sortOrder.specified=true");

        // Get all the planningUnitGroupSetList where sortOrder is null
        defaultPlanningUnitGroupSetShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupSetShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupSetList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupSetShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupSetShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupSetList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupSetShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPlanningUnitGroupSetShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the planningUnitGroupSetList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupSetShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupSetShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the planningUnitGroupSetList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupSetShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);

        // Get all the planningUnitGroupSetList where isActive is not null
        defaultPlanningUnitGroupSetShouldBeFound("isActive.specified=true");

        // Get all the planningUnitGroupSetList where isActive is null
        defaultPlanningUnitGroupSetShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupSetsByPlanningUnitGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        PlanningUnitGroup planningUnitGroups = PlanningUnitGroupResourceIntTest.createEntity(em);
        em.persist(planningUnitGroups);
        em.flush();
        planningUnitGroupSet.addPlanningUnitGroups(planningUnitGroups);
        planningUnitGroupSetRepository.saveAndFlush(planningUnitGroupSet);
        Long planningUnitGroupsId = planningUnitGroups.getId();

        // Get all the planningUnitGroupSetList where planningUnitGroups equals to planningUnitGroupsId
        defaultPlanningUnitGroupSetShouldBeFound("planningUnitGroupsId.equals=" + planningUnitGroupsId);

        // Get all the planningUnitGroupSetList where planningUnitGroups equals to planningUnitGroupsId + 1
        defaultPlanningUnitGroupSetShouldNotBeFound("planningUnitGroupsId.equals=" + (planningUnitGroupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPlanningUnitGroupSetShouldBeFound(String filter) throws Exception {
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPlanningUnitGroupSetShouldNotBeFound(String filter) throws Exception {
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanningUnitGroupSet() throws Exception {
        // Get the planningUnitGroupSet
        restPlanningUnitGroupSetMockMvc.perform(get("/api/planning-unit-group-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningUnitGroupSet() throws Exception {
        // Initialize the database
        planningUnitGroupSetService.save(planningUnitGroupSet);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanningUnitGroupSetSearchRepository);

        int databaseSizeBeforeUpdate = planningUnitGroupSetRepository.findAll().size();

        // Update the planningUnitGroupSet
        PlanningUnitGroupSet updatedPlanningUnitGroupSet = planningUnitGroupSetRepository.findById(planningUnitGroupSet.getId()).get();
        // Disconnect from session so that the updates on updatedPlanningUnitGroupSet are not directly saved in db
        em.detach(updatedPlanningUnitGroupSet);
        updatedPlanningUnitGroupSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanningUnitGroupSetMockMvc.perform(put("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanningUnitGroupSet)))
            .andExpect(status().isOk());

        // Validate the PlanningUnitGroupSet in the database
        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        PlanningUnitGroupSet testPlanningUnitGroupSet = planningUnitGroupSetList.get(planningUnitGroupSetList.size() - 1);
        assertThat(testPlanningUnitGroupSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPlanningUnitGroupSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPlanningUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanningUnitGroupSet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testPlanningUnitGroupSet.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the PlanningUnitGroupSet in Elasticsearch
        verify(mockPlanningUnitGroupSetSearchRepository, times(1)).save(testPlanningUnitGroupSet);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanningUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = planningUnitGroupSetRepository.findAll().size();

        // Create the PlanningUnitGroupSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningUnitGroupSetMockMvc.perform(put("/api/planning-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitGroupSet in the database
        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanningUnitGroupSet in Elasticsearch
        verify(mockPlanningUnitGroupSetSearchRepository, times(0)).save(planningUnitGroupSet);
    }

    @Test
    @Transactional
    public void deletePlanningUnitGroupSet() throws Exception {
        // Initialize the database
        planningUnitGroupSetService.save(planningUnitGroupSet);

        int databaseSizeBeforeDelete = planningUnitGroupSetRepository.findAll().size();

        // Delete the planningUnitGroupSet
        restPlanningUnitGroupSetMockMvc.perform(delete("/api/planning-unit-group-sets/{id}", planningUnitGroupSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningUnitGroupSet> planningUnitGroupSetList = planningUnitGroupSetRepository.findAll();
        assertThat(planningUnitGroupSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanningUnitGroupSet in Elasticsearch
        verify(mockPlanningUnitGroupSetSearchRepository, times(1)).deleteById(planningUnitGroupSet.getId());
    }

    @Test
    @Transactional
    public void searchPlanningUnitGroupSet() throws Exception {
        // Initialize the database
        planningUnitGroupSetService.save(planningUnitGroupSet);
        when(mockPlanningUnitGroupSetSearchRepository.search(queryStringQuery("id:" + planningUnitGroupSet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planningUnitGroupSet), PageRequest.of(0, 1), 1));
        // Search the planningUnitGroupSet
        restPlanningUnitGroupSetMockMvc.perform(get("/api/_search/planning-unit-group-sets?query=id:" + planningUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningUnitGroupSet.class);
        PlanningUnitGroupSet planningUnitGroupSet1 = new PlanningUnitGroupSet();
        planningUnitGroupSet1.setId(1L);
        PlanningUnitGroupSet planningUnitGroupSet2 = new PlanningUnitGroupSet();
        planningUnitGroupSet2.setId(planningUnitGroupSet1.getId());
        assertThat(planningUnitGroupSet1).isEqualTo(planningUnitGroupSet2);
        planningUnitGroupSet2.setId(2L);
        assertThat(planningUnitGroupSet1).isNotEqualTo(planningUnitGroupSet2);
        planningUnitGroupSet1.setId(null);
        assertThat(planningUnitGroupSet1).isNotEqualTo(planningUnitGroupSet2);
    }
}
