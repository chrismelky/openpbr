package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.domain.PlanningUnit;
import org.openpbr.domain.PlanningUnitGroupSet;
import org.openpbr.repository.PlanningUnitGroupRepository;
import org.openpbr.repository.search.PlanningUnitGroupSearchRepository;
import org.openpbr.service.PlanningUnitGroupService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.PlanningUnitGroupCriteria;
import org.openpbr.service.PlanningUnitGroupQueryService;

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
 * Test class for the PlanningUnitGroupResource REST controller.
 *
 * @see PlanningUnitGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class PlanningUnitGroupResourceIntTest {

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
    private PlanningUnitGroupRepository planningUnitGroupRepository;

    @Mock
    private PlanningUnitGroupRepository planningUnitGroupRepositoryMock;

    @Mock
    private PlanningUnitGroupService planningUnitGroupServiceMock;

    @Autowired
    private PlanningUnitGroupService planningUnitGroupService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.PlanningUnitGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanningUnitGroupSearchRepository mockPlanningUnitGroupSearchRepository;

    @Autowired
    private PlanningUnitGroupQueryService planningUnitGroupQueryService;

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

    private MockMvc restPlanningUnitGroupMockMvc;

    private PlanningUnitGroup planningUnitGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningUnitGroupResource planningUnitGroupResource = new PlanningUnitGroupResource(planningUnitGroupService, planningUnitGroupQueryService);
        this.restPlanningUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupResource)
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
    public static PlanningUnitGroup createEntity(EntityManager em) {
        PlanningUnitGroup planningUnitGroup = new PlanningUnitGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        PlanningUnit planningUnit = PlanningUnitResourceIntTest.createEntity(em);
        em.persist(planningUnit);
        em.flush();
        planningUnitGroup.getPlanningUnits().add(planningUnit);
        return planningUnitGroup;
    }

    @Before
    public void initTest() {
        planningUnitGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanningUnitGroup() throws Exception {
        int databaseSizeBeforeCreate = planningUnitGroupRepository.findAll().size();

        // Create the PlanningUnitGroup
        restPlanningUnitGroupMockMvc.perform(post("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroup)))
            .andExpect(status().isCreated());

        // Validate the PlanningUnitGroup in the database
        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PlanningUnitGroup testPlanningUnitGroup = planningUnitGroupList.get(planningUnitGroupList.size() - 1);
        assertThat(testPlanningUnitGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPlanningUnitGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPlanningUnitGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanningUnitGroup.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testPlanningUnitGroup.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the PlanningUnitGroup in Elasticsearch
        verify(mockPlanningUnitGroupSearchRepository, times(1)).save(testPlanningUnitGroup);
    }

    @Test
    @Transactional
    public void createPlanningUnitGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningUnitGroupRepository.findAll().size();

        // Create the PlanningUnitGroup with an existing ID
        planningUnitGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningUnitGroupMockMvc.perform(post("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitGroup in the database
        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanningUnitGroup in Elasticsearch
        verify(mockPlanningUnitGroupSearchRepository, times(0)).save(planningUnitGroup);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitGroupRepository.findAll().size();
        // set the field null
        planningUnitGroup.setUid(null);

        // Create the PlanningUnitGroup, which fails.

        restPlanningUnitGroupMockMvc.perform(post("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroup)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planningUnitGroupRepository.findAll().size();
        // set the field null
        planningUnitGroup.setName(null);

        // Create the PlanningUnitGroup, which fails.

        restPlanningUnitGroupMockMvc.perform(post("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroup)))
            .andExpect(status().isBadRequest());

        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroups() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPlanningUnitGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        PlanningUnitGroupResource planningUnitGroupResource = new PlanningUnitGroupResource(planningUnitGroupServiceMock, planningUnitGroupQueryService);
        when(planningUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPlanningUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(planningUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPlanningUnitGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PlanningUnitGroupResource planningUnitGroupResource = new PlanningUnitGroupResource(planningUnitGroupServiceMock, planningUnitGroupQueryService);
            when(planningUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPlanningUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(planningUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(planningUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlanningUnitGroup() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get the planningUnitGroup
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups/{id}", planningUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planningUnitGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where uid equals to DEFAULT_UID
        defaultPlanningUnitGroupShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the planningUnitGroupList where uid equals to UPDATED_UID
        defaultPlanningUnitGroupShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where uid in DEFAULT_UID or UPDATED_UID
        defaultPlanningUnitGroupShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the planningUnitGroupList where uid equals to UPDATED_UID
        defaultPlanningUnitGroupShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where uid is not null
        defaultPlanningUnitGroupShouldBeFound("uid.specified=true");

        // Get all the planningUnitGroupList where uid is null
        defaultPlanningUnitGroupShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where code equals to DEFAULT_CODE
        defaultPlanningUnitGroupShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the planningUnitGroupList where code equals to UPDATED_CODE
        defaultPlanningUnitGroupShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPlanningUnitGroupShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the planningUnitGroupList where code equals to UPDATED_CODE
        defaultPlanningUnitGroupShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where code is not null
        defaultPlanningUnitGroupShouldBeFound("code.specified=true");

        // Get all the planningUnitGroupList where code is null
        defaultPlanningUnitGroupShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where name equals to DEFAULT_NAME
        defaultPlanningUnitGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the planningUnitGroupList where name equals to UPDATED_NAME
        defaultPlanningUnitGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlanningUnitGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the planningUnitGroupList where name equals to UPDATED_NAME
        defaultPlanningUnitGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where name is not null
        defaultPlanningUnitGroupShouldBeFound("name.specified=true");

        // Get all the planningUnitGroupList where name is null
        defaultPlanningUnitGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultPlanningUnitGroupShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the planningUnitGroupList where sortOrder equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where sortOrder is not null
        defaultPlanningUnitGroupShouldBeFound("sortOrder.specified=true");

        // Get all the planningUnitGroupList where sortOrder is null
        defaultPlanningUnitGroupShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultPlanningUnitGroupShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the planningUnitGroupList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultPlanningUnitGroupShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPlanningUnitGroupShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the planningUnitGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the planningUnitGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultPlanningUnitGroupShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);

        // Get all the planningUnitGroupList where isActive is not null
        defaultPlanningUnitGroupShouldBeFound("isActive.specified=true");

        // Get all the planningUnitGroupList where isActive is null
        defaultPlanningUnitGroupShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByPlanningUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        PlanningUnit planningUnits = PlanningUnitResourceIntTest.createEntity(em);
        em.persist(planningUnits);
        em.flush();
        planningUnitGroup.addPlanningUnits(planningUnits);
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);
        Long planningUnitsId = planningUnits.getId();

        // Get all the planningUnitGroupList where planningUnits equals to planningUnitsId
        defaultPlanningUnitGroupShouldBeFound("planningUnitsId.equals=" + planningUnitsId);

        // Get all the planningUnitGroupList where planningUnits equals to planningUnitsId + 1
        defaultPlanningUnitGroupShouldNotBeFound("planningUnitsId.equals=" + (planningUnitsId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanningUnitGroupsByPlanningUnitGroupSetIsEqualToSomething() throws Exception {
        // Initialize the database
        PlanningUnitGroupSet planningUnitGroupSet = PlanningUnitGroupSetResourceIntTest.createEntity(em);
        em.persist(planningUnitGroupSet);
        em.flush();
        planningUnitGroup.addPlanningUnitGroupSet(planningUnitGroupSet);
        planningUnitGroupRepository.saveAndFlush(planningUnitGroup);
        Long planningUnitGroupSetId = planningUnitGroupSet.getId();

        // Get all the planningUnitGroupList where planningUnitGroupSet equals to planningUnitGroupSetId
        defaultPlanningUnitGroupShouldBeFound("planningUnitGroupSetId.equals=" + planningUnitGroupSetId);

        // Get all the planningUnitGroupList where planningUnitGroupSet equals to planningUnitGroupSetId + 1
        defaultPlanningUnitGroupShouldNotBeFound("planningUnitGroupSetId.equals=" + (planningUnitGroupSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPlanningUnitGroupShouldBeFound(String filter) throws Exception {
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPlanningUnitGroupShouldNotBeFound(String filter) throws Exception {
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanningUnitGroup() throws Exception {
        // Get the planningUnitGroup
        restPlanningUnitGroupMockMvc.perform(get("/api/planning-unit-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningUnitGroup() throws Exception {
        // Initialize the database
        planningUnitGroupService.save(planningUnitGroup);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanningUnitGroupSearchRepository);

        int databaseSizeBeforeUpdate = planningUnitGroupRepository.findAll().size();

        // Update the planningUnitGroup
        PlanningUnitGroup updatedPlanningUnitGroup = planningUnitGroupRepository.findById(planningUnitGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPlanningUnitGroup are not directly saved in db
        em.detach(updatedPlanningUnitGroup);
        updatedPlanningUnitGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restPlanningUnitGroupMockMvc.perform(put("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanningUnitGroup)))
            .andExpect(status().isOk());

        // Validate the PlanningUnitGroup in the database
        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        PlanningUnitGroup testPlanningUnitGroup = planningUnitGroupList.get(planningUnitGroupList.size() - 1);
        assertThat(testPlanningUnitGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPlanningUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPlanningUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanningUnitGroup.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testPlanningUnitGroup.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the PlanningUnitGroup in Elasticsearch
        verify(mockPlanningUnitGroupSearchRepository, times(1)).save(testPlanningUnitGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanningUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = planningUnitGroupRepository.findAll().size();

        // Create the PlanningUnitGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningUnitGroupMockMvc.perform(put("/api/planning-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planningUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningUnitGroup in the database
        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanningUnitGroup in Elasticsearch
        verify(mockPlanningUnitGroupSearchRepository, times(0)).save(planningUnitGroup);
    }

    @Test
    @Transactional
    public void deletePlanningUnitGroup() throws Exception {
        // Initialize the database
        planningUnitGroupService.save(planningUnitGroup);

        int databaseSizeBeforeDelete = planningUnitGroupRepository.findAll().size();

        // Delete the planningUnitGroup
        restPlanningUnitGroupMockMvc.perform(delete("/api/planning-unit-groups/{id}", planningUnitGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningUnitGroup> planningUnitGroupList = planningUnitGroupRepository.findAll();
        assertThat(planningUnitGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanningUnitGroup in Elasticsearch
        verify(mockPlanningUnitGroupSearchRepository, times(1)).deleteById(planningUnitGroup.getId());
    }

    @Test
    @Transactional
    public void searchPlanningUnitGroup() throws Exception {
        // Initialize the database
        planningUnitGroupService.save(planningUnitGroup);
        when(mockPlanningUnitGroupSearchRepository.search(queryStringQuery("id:" + planningUnitGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planningUnitGroup), PageRequest.of(0, 1), 1));
        // Search the planningUnitGroup
        restPlanningUnitGroupMockMvc.perform(get("/api/_search/planning-unit-groups?query=id:" + planningUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningUnitGroup.class);
        PlanningUnitGroup planningUnitGroup1 = new PlanningUnitGroup();
        planningUnitGroup1.setId(1L);
        PlanningUnitGroup planningUnitGroup2 = new PlanningUnitGroup();
        planningUnitGroup2.setId(planningUnitGroup1.getId());
        assertThat(planningUnitGroup1).isEqualTo(planningUnitGroup2);
        planningUnitGroup2.setId(2L);
        assertThat(planningUnitGroup1).isNotEqualTo(planningUnitGroup2);
        planningUnitGroup1.setId(null);
        assertThat(planningUnitGroup1).isNotEqualTo(planningUnitGroup2);
    }
}
