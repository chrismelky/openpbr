package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OrgUnitGroup;
import org.openpbr.domain.OrganisationUnit;
import org.openpbr.repository.OrgUnitGroupRepository;
import org.openpbr.repository.search.OrgUnitGroupSearchRepository;
import org.openpbr.service.OrgUnitGroupService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OrgUnitGroupCriteria;
import org.openpbr.service.OrgUnitGroupQueryService;

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
 * Test class for the OrgUnitGroupResource REST controller.
 *
 * @see OrgUnitGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OrgUnitGroupResourceIntTest {

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
    private OrgUnitGroupRepository orgUnitGroupRepository;

    @Mock
    private OrgUnitGroupRepository orgUnitGroupRepositoryMock;

    @Mock
    private OrgUnitGroupService orgUnitGroupServiceMock;

    @Autowired
    private OrgUnitGroupService orgUnitGroupService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OrgUnitGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrgUnitGroupSearchRepository mockOrgUnitGroupSearchRepository;

    @Autowired
    private OrgUnitGroupQueryService orgUnitGroupQueryService;

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

    private MockMvc restOrgUnitGroupMockMvc;

    private OrgUnitGroup orgUnitGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupService, orgUnitGroupQueryService);
        this.restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
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
    public static OrgUnitGroup createEntity(EntityManager em) {
        OrgUnitGroup orgUnitGroup = new OrgUnitGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        return orgUnitGroup;
    }

    @Before
    public void initTest() {
        orgUnitGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUnitGroup() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup
        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isCreated());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnitGroup testOrgUnitGroup = orgUnitGroupList.get(orgUnitGroupList.size() - 1);
        assertThat(testOrgUnitGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrgUnitGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrgUnitGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUnitGroup.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testOrgUnitGroup.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the OrgUnitGroup in Elasticsearch
        verify(mockOrgUnitGroupSearchRepository, times(1)).save(testOrgUnitGroup);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup with an existing ID
        orgUnitGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrgUnitGroup in Elasticsearch
        verify(mockOrgUnitGroupSearchRepository, times(0)).save(orgUnitGroup);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupRepository.findAll().size();
        // set the field null
        orgUnitGroup.setUid(null);

        // Create the OrgUnitGroup, which fails.

        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupRepository.findAll().size();
        // set the field null
        orgUnitGroup.setName(null);

        // Create the OrgUnitGroup, which fails.

        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroups() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllOrgUnitGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupServiceMock, orgUnitGroupQueryService);
        when(orgUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(orgUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOrgUnitGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupServiceMock, orgUnitGroupQueryService);
            when(orgUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(orgUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/{id}", orgUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnitGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid equals to DEFAULT_UID
        defaultOrgUnitGroupShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the orgUnitGroupList where uid equals to UPDATED_UID
        defaultOrgUnitGroupShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid in DEFAULT_UID or UPDATED_UID
        defaultOrgUnitGroupShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the orgUnitGroupList where uid equals to UPDATED_UID
        defaultOrgUnitGroupShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid is not null
        defaultOrgUnitGroupShouldBeFound("uid.specified=true");

        // Get all the orgUnitGroupList where uid is null
        defaultOrgUnitGroupShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code equals to DEFAULT_CODE
        defaultOrgUnitGroupShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orgUnitGroupList where code equals to UPDATED_CODE
        defaultOrgUnitGroupShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrgUnitGroupShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orgUnitGroupList where code equals to UPDATED_CODE
        defaultOrgUnitGroupShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code is not null
        defaultOrgUnitGroupShouldBeFound("code.specified=true");

        // Get all the orgUnitGroupList where code is null
        defaultOrgUnitGroupShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name equals to DEFAULT_NAME
        defaultOrgUnitGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orgUnitGroupList where name equals to UPDATED_NAME
        defaultOrgUnitGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrgUnitGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orgUnitGroupList where name equals to UPDATED_NAME
        defaultOrgUnitGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name is not null
        defaultOrgUnitGroupShouldBeFound("name.specified=true");

        // Get all the orgUnitGroupList where name is null
        defaultOrgUnitGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultOrgUnitGroupShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the orgUnitGroupList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where sortOrder is not null
        defaultOrgUnitGroupShouldBeFound("sortOrder.specified=true");

        // Get all the orgUnitGroupList where sortOrder is null
        defaultOrgUnitGroupShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllOrgUnitGroupsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOrgUnitGroupShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the orgUnitGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrgUnitGroupShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOrgUnitGroupShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the orgUnitGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrgUnitGroupShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where isActive is not null
        defaultOrgUnitGroupShouldBeFound("isActive.specified=true");

        // Get all the orgUnitGroupList where isActive is null
        defaultOrgUnitGroupShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByOrganisationUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        OrganisationUnit organisationUnits = OrganisationUnitResourceIntTest.createEntity(em);
        em.persist(organisationUnits);
        em.flush();
        orgUnitGroup.addOrganisationUnits(organisationUnits);
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);
        Long organisationUnitsId = organisationUnits.getId();

        // Get all the orgUnitGroupList where organisationUnits equals to organisationUnitsId
        defaultOrgUnitGroupShouldBeFound("organisationUnitsId.equals=" + organisationUnitsId);

        // Get all the orgUnitGroupList where organisationUnits equals to organisationUnitsId + 1
        defaultOrgUnitGroupShouldNotBeFound("organisationUnitsId.equals=" + (organisationUnitsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrgUnitGroupShouldBeFound(String filter) throws Exception {
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrgUnitGroupShouldNotBeFound(String filter) throws Exception {
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrgUnitGroup() throws Exception {
        // Get the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupService.save(orgUnitGroup);
        // As the test used the treeNodeService layer, reset the Elasticsearch mock repository
        reset(mockOrgUnitGroupSearchRepository);

        int databaseSizeBeforeUpdate = orgUnitGroupRepository.findAll().size();

        // Update the orgUnitGroup
        OrgUnitGroup updatedOrgUnitGroup = orgUnitGroupRepository.findById(orgUnitGroup.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnitGroup are not directly saved in db
        em.detach(updatedOrgUnitGroup);
        updatedOrgUnitGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restOrgUnitGroupMockMvc.perform(put("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnitGroup)))
            .andExpect(status().isOk());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        OrgUnitGroup testOrgUnitGroup = orgUnitGroupList.get(orgUnitGroupList.size() - 1);
        assertThat(testOrgUnitGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrgUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrgUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUnitGroup.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testOrgUnitGroup.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the OrgUnitGroup in Elasticsearch
        verify(mockOrgUnitGroupSearchRepository, times(1)).save(testOrgUnitGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitGroupMockMvc.perform(put("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgUnitGroup in Elasticsearch
        verify(mockOrgUnitGroupSearchRepository, times(0)).save(orgUnitGroup);
    }

    @Test
    @Transactional
    public void deleteOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupService.save(orgUnitGroup);

        int databaseSizeBeforeDelete = orgUnitGroupRepository.findAll().size();

        // Delete the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(delete("/api/org-unit-groups/{id}", orgUnitGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrgUnitGroup in Elasticsearch
        verify(mockOrgUnitGroupSearchRepository, times(1)).deleteById(orgUnitGroup.getId());
    }

    @Test
    @Transactional
    public void searchOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupService.save(orgUnitGroup);
        when(mockOrgUnitGroupSearchRepository.search(queryStringQuery("id:" + orgUnitGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orgUnitGroup), PageRequest.of(0, 1), 1));
        // Search the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(get("/api/_search/org-unit-groups?query=id:" + orgUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnitGroup.class);
        OrgUnitGroup orgUnitGroup1 = new OrgUnitGroup();
        orgUnitGroup1.setId(1L);
        OrgUnitGroup orgUnitGroup2 = new OrgUnitGroup();
        orgUnitGroup2.setId(orgUnitGroup1.getId());
        assertThat(orgUnitGroup1).isEqualTo(orgUnitGroup2);
        orgUnitGroup2.setId(2L);
        assertThat(orgUnitGroup1).isNotEqualTo(orgUnitGroup2);
        orgUnitGroup1.setId(null);
        assertThat(orgUnitGroup1).isNotEqualTo(orgUnitGroup2);
    }
}
