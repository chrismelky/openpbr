package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OrgUnitGroupSet;
import org.openpbr.domain.OrgUnitGroup;
import org.openpbr.domain.AttributeValue;
import org.openpbr.repository.OrgUnitGroupSetRepository;
import org.openpbr.repository.search.OrgUnitGroupSetSearchRepository;
import org.openpbr.service.OrgUnitGroupSetService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OrgUnitGroupSetCriteria;
import org.openpbr.service.OrgUnitGroupSetQueryService;

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
 * Test class for the OrgUnitGroupSetResource REST controller.
 *
 * @see OrgUnitGroupSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OrgUnitGroupSetResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private OrgUnitGroupSetRepository orgUnitGroupSetRepository;

    @Mock
    private OrgUnitGroupSetRepository orgUnitGroupSetRepositoryMock;

    @Mock
    private OrgUnitGroupSetService orgUnitGroupSetServiceMock;

    @Autowired
    private OrgUnitGroupSetService orgUnitGroupSetService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OrgUnitGroupSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrgUnitGroupSetSearchRepository mockOrgUnitGroupSetSearchRepository;

    @Autowired
    private OrgUnitGroupSetQueryService orgUnitGroupSetQueryService;

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

    private MockMvc restOrgUnitGroupSetMockMvc;

    private OrgUnitGroupSet orgUnitGroupSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetService, orgUnitGroupSetQueryService);
        this.restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
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
    public static OrgUnitGroupSet createEntity(EntityManager em) {
        OrgUnitGroupSet orgUnitGroupSet = new OrgUnitGroupSet()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        OrgUnitGroup orgUnitGroup = OrgUnitGroupResourceIntTest.createEntity(em);
        em.persist(orgUnitGroup);
        em.flush();
        orgUnitGroupSet.getOrgUnitGroups().add(orgUnitGroup);
        return orgUnitGroupSet;
    }

    @Before
    public void initTest() {
        orgUnitGroupSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupSet() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isCreated());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnitGroupSet testOrgUnitGroupSet = orgUnitGroupSetList.get(orgUnitGroupSetList.size() - 1);
        assertThat(testOrgUnitGroupSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrgUnitGroupSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrgUnitGroupSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUnitGroupSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrgUnitGroupSet.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testOrgUnitGroupSet.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the OrgUnitGroupSet in Elasticsearch
        verify(mockOrgUnitGroupSetSearchRepository, times(1)).save(testOrgUnitGroupSet);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet with an existing ID
        orgUnitGroupSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrgUnitGroupSet in Elasticsearch
        verify(mockOrgUnitGroupSetSearchRepository, times(0)).save(orgUnitGroupSet);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupSetRepository.findAll().size();
        // set the field null
        orgUnitGroupSet.setUid(null);

        // Create the OrgUnitGroupSet, which fails.

        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupSetRepository.findAll().size();
        // set the field null
        orgUnitGroupSet.setName(null);

        // Create the OrgUnitGroupSet, which fails.

        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSets() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllOrgUnitGroupSetsWithEagerRelationshipsIsEnabled() throws Exception {
        OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetServiceMock, orgUnitGroupSetQueryService);
        when(orgUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

        verify(orgUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOrgUnitGroupSetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetServiceMock, orgUnitGroupSetQueryService);
            when(orgUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

            verify(orgUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/{id}", orgUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnitGroupSet.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uid equals to DEFAULT_UID
        defaultOrgUnitGroupSetShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the orgUnitGroupSetList where uid equals to UPDATED_UID
        defaultOrgUnitGroupSetShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uid in DEFAULT_UID or UPDATED_UID
        defaultOrgUnitGroupSetShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the orgUnitGroupSetList where uid equals to UPDATED_UID
        defaultOrgUnitGroupSetShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uid is not null
        defaultOrgUnitGroupSetShouldBeFound("uid.specified=true");

        // Get all the orgUnitGroupSetList where uid is null
        defaultOrgUnitGroupSetShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code equals to DEFAULT_CODE
        defaultOrgUnitGroupSetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orgUnitGroupSetList where code equals to UPDATED_CODE
        defaultOrgUnitGroupSetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrgUnitGroupSetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orgUnitGroupSetList where code equals to UPDATED_CODE
        defaultOrgUnitGroupSetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code is not null
        defaultOrgUnitGroupSetShouldBeFound("code.specified=true");

        // Get all the orgUnitGroupSetList where code is null
        defaultOrgUnitGroupSetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name equals to DEFAULT_NAME
        defaultOrgUnitGroupSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orgUnitGroupSetList where name equals to UPDATED_NAME
        defaultOrgUnitGroupSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrgUnitGroupSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orgUnitGroupSetList where name equals to UPDATED_NAME
        defaultOrgUnitGroupSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name is not null
        defaultOrgUnitGroupSetShouldBeFound("name.specified=true");

        // Get all the orgUnitGroupSetList where name is null
        defaultOrgUnitGroupSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description equals to DEFAULT_DESCRIPTION
        defaultOrgUnitGroupSetShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the orgUnitGroupSetList where description equals to UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the orgUnitGroupSetList where description equals to UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description is not null
        defaultOrgUnitGroupSetShouldBeFound("description.specified=true");

        // Get all the orgUnitGroupSetList where description is null
        defaultOrgUnitGroupSetShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupSetShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupSetShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultOrgUnitGroupSetShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the orgUnitGroupSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupSetShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where sortOrder is not null
        defaultOrgUnitGroupSetShouldBeFound("sortOrder.specified=true");

        // Get all the orgUnitGroupSetList where sortOrder is null
        defaultOrgUnitGroupSetShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupSetShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupSetList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupSetShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultOrgUnitGroupSetShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the orgUnitGroupSetList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultOrgUnitGroupSetShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOrgUnitGroupSetShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the orgUnitGroupSetList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrgUnitGroupSetShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOrgUnitGroupSetShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the orgUnitGroupSetList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrgUnitGroupSetShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where isActive is not null
        defaultOrgUnitGroupSetShouldBeFound("isActive.specified=true");

        // Get all the orgUnitGroupSetList where isActive is null
        defaultOrgUnitGroupSetShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByOrgUnitGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        OrgUnitGroup orgUnitGroups = OrgUnitGroupResourceIntTest.createEntity(em);
        em.persist(orgUnitGroups);
        em.flush();
        orgUnitGroupSet.addOrgUnitGroups(orgUnitGroups);
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);
        Long orgUnitGroupsId = orgUnitGroups.getId();

        // Get all the orgUnitGroupSetList where orgUnitGroups equals to orgUnitGroupsId
        defaultOrgUnitGroupSetShouldBeFound("orgUnitGroupsId.equals=" + orgUnitGroupsId);

        // Get all the orgUnitGroupSetList where orgUnitGroups equals to orgUnitGroupsId + 1
        defaultOrgUnitGroupSetShouldNotBeFound("orgUnitGroupsId.equals=" + (orgUnitGroupsId + 1));
    }


    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByAttributeValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        AttributeValue attributeValues = AttributeValueResourceIntTest.createEntity(em);
        em.persist(attributeValues);
        em.flush();
        orgUnitGroupSet.addAttributeValues(attributeValues);
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);
        Long attributeValuesId = attributeValues.getId();

        // Get all the orgUnitGroupSetList where attributeValues equals to attributeValuesId
        defaultOrgUnitGroupSetShouldBeFound("attributeValuesId.equals=" + attributeValuesId);

        // Get all the orgUnitGroupSetList where attributeValues equals to attributeValuesId + 1
        defaultOrgUnitGroupSetShouldNotBeFound("attributeValuesId.equals=" + (attributeValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrgUnitGroupSetShouldBeFound(String filter) throws Exception {
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrgUnitGroupSetShouldNotBeFound(String filter) throws Exception {
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrgUnitGroupSet() throws Exception {
        // Get the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetService.save(orgUnitGroupSet);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrgUnitGroupSetSearchRepository);

        int databaseSizeBeforeUpdate = orgUnitGroupSetRepository.findAll().size();

        // Update the orgUnitGroupSet
        OrgUnitGroupSet updatedOrgUnitGroupSet = orgUnitGroupSetRepository.findById(orgUnitGroupSet.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnitGroupSet are not directly saved in db
        em.detach(updatedOrgUnitGroupSet);
        updatedOrgUnitGroupSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restOrgUnitGroupSetMockMvc.perform(put("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnitGroupSet)))
            .andExpect(status().isOk());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        OrgUnitGroupSet testOrgUnitGroupSet = orgUnitGroupSetList.get(orgUnitGroupSetList.size() - 1);
        assertThat(testOrgUnitGroupSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrgUnitGroupSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrgUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUnitGroupSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrgUnitGroupSet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testOrgUnitGroupSet.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the OrgUnitGroupSet in Elasticsearch
        verify(mockOrgUnitGroupSetSearchRepository, times(1)).save(testOrgUnitGroupSet);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitGroupSetMockMvc.perform(put("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgUnitGroupSet in Elasticsearch
        verify(mockOrgUnitGroupSetSearchRepository, times(0)).save(orgUnitGroupSet);
    }

    @Test
    @Transactional
    public void deleteOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetService.save(orgUnitGroupSet);

        int databaseSizeBeforeDelete = orgUnitGroupSetRepository.findAll().size();

        // Delete the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(delete("/api/org-unit-group-sets/{id}", orgUnitGroupSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrgUnitGroupSet in Elasticsearch
        verify(mockOrgUnitGroupSetSearchRepository, times(1)).deleteById(orgUnitGroupSet.getId());
    }

    @Test
    @Transactional
    public void searchOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetService.save(orgUnitGroupSet);
        when(mockOrgUnitGroupSetSearchRepository.search(queryStringQuery("id:" + orgUnitGroupSet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orgUnitGroupSet), PageRequest.of(0, 1), 1));
        // Search the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(get("/api/_search/org-unit-group-sets?query=id:" + orgUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnitGroupSet.class);
        OrgUnitGroupSet orgUnitGroupSet1 = new OrgUnitGroupSet();
        orgUnitGroupSet1.setId(1L);
        OrgUnitGroupSet orgUnitGroupSet2 = new OrgUnitGroupSet();
        orgUnitGroupSet2.setId(orgUnitGroupSet1.getId());
        assertThat(orgUnitGroupSet1).isEqualTo(orgUnitGroupSet2);
        orgUnitGroupSet2.setId(2L);
        assertThat(orgUnitGroupSet1).isNotEqualTo(orgUnitGroupSet2);
        orgUnitGroupSet1.setId(null);
        assertThat(orgUnitGroupSet1).isNotEqualTo(orgUnitGroupSet2);
    }
}
