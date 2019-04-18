package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OrgUnitLevel;
import org.openpbr.repository.OrgUnitLevelRepository;
import org.openpbr.repository.search.OrgUnitLevelSearchRepository;
import org.openpbr.service.OrgUnitLevelService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OrgUnitLevelCriteria;
import org.openpbr.service.OrgUnitLevelQueryService;

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
 * Test class for the OrgUnitLevelResource REST controller.
 *
 * @see OrgUnitLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OrgUnitLevelResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Autowired
    private OrgUnitLevelRepository orgUnitLevelRepository;

    @Autowired
    private OrgUnitLevelService orgUnitLevelService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OrgUnitLevelSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrgUnitLevelSearchRepository mockOrgUnitLevelSearchRepository;

    @Autowired
    private OrgUnitLevelQueryService orgUnitLevelQueryService;

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

    private MockMvc restOrgUnitLevelMockMvc;

    private OrgUnitLevel orgUnitLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUnitLevelResource orgUnitLevelResource = new OrgUnitLevelResource(orgUnitLevelService, orgUnitLevelQueryService);
        this.restOrgUnitLevelMockMvc = MockMvcBuilders.standaloneSetup(orgUnitLevelResource)
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
    public static OrgUnitLevel createEntity(EntityManager em) {
        OrgUnitLevel orgUnitLevel = new OrgUnitLevel()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL);
        return orgUnitLevel;
    }

    @Before
    public void initTest() {
        orgUnitLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUnitLevel() throws Exception {
        int databaseSizeBeforeCreate = orgUnitLevelRepository.findAll().size();

        // Create the OrgUnitLevel
        restOrgUnitLevelMockMvc.perform(post("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isCreated());

        // Validate the OrgUnitLevel in the database
        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnitLevel testOrgUnitLevel = orgUnitLevelList.get(orgUnitLevelList.size() - 1);
        assertThat(testOrgUnitLevel.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrgUnitLevel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrgUnitLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUnitLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);

        // Validate the OrgUnitLevel in Elasticsearch
        verify(mockOrgUnitLevelSearchRepository, times(1)).save(testOrgUnitLevel);
    }

    @Test
    @Transactional
    public void createOrgUnitLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUnitLevelRepository.findAll().size();

        // Create the OrgUnitLevel with an existing ID
        orgUnitLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitLevelMockMvc.perform(post("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitLevel in the database
        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrgUnitLevel in Elasticsearch
        verify(mockOrgUnitLevelSearchRepository, times(0)).save(orgUnitLevel);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitLevelRepository.findAll().size();
        // set the field null
        orgUnitLevel.setUid(null);

        // Create the OrgUnitLevel, which fails.

        restOrgUnitLevelMockMvc.perform(post("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isBadRequest());

        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitLevelRepository.findAll().size();
        // set the field null
        orgUnitLevel.setName(null);

        // Create the OrgUnitLevel, which fails.

        restOrgUnitLevelMockMvc.perform(post("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isBadRequest());

        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitLevelRepository.findAll().size();
        // set the field null
        orgUnitLevel.setLevel(null);

        // Create the OrgUnitLevel, which fails.

        restOrgUnitLevelMockMvc.perform(post("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isBadRequest());

        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevels() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getOrgUnitLevel() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get the orgUnitLevel
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels/{id}", orgUnitLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnitLevel.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where uid equals to DEFAULT_UID
        defaultOrgUnitLevelShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the orgUnitLevelList where uid equals to UPDATED_UID
        defaultOrgUnitLevelShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where uid in DEFAULT_UID or UPDATED_UID
        defaultOrgUnitLevelShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the orgUnitLevelList where uid equals to UPDATED_UID
        defaultOrgUnitLevelShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where uid is not null
        defaultOrgUnitLevelShouldBeFound("uid.specified=true");

        // Get all the orgUnitLevelList where uid is null
        defaultOrgUnitLevelShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where code equals to DEFAULT_CODE
        defaultOrgUnitLevelShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orgUnitLevelList where code equals to UPDATED_CODE
        defaultOrgUnitLevelShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrgUnitLevelShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orgUnitLevelList where code equals to UPDATED_CODE
        defaultOrgUnitLevelShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where code is not null
        defaultOrgUnitLevelShouldBeFound("code.specified=true");

        // Get all the orgUnitLevelList where code is null
        defaultOrgUnitLevelShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where name equals to DEFAULT_NAME
        defaultOrgUnitLevelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orgUnitLevelList where name equals to UPDATED_NAME
        defaultOrgUnitLevelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrgUnitLevelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orgUnitLevelList where name equals to UPDATED_NAME
        defaultOrgUnitLevelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where name is not null
        defaultOrgUnitLevelShouldBeFound("name.specified=true");

        // Get all the orgUnitLevelList where name is null
        defaultOrgUnitLevelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where level equals to DEFAULT_LEVEL
        defaultOrgUnitLevelShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the orgUnitLevelList where level equals to UPDATED_LEVEL
        defaultOrgUnitLevelShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultOrgUnitLevelShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the orgUnitLevelList where level equals to UPDATED_LEVEL
        defaultOrgUnitLevelShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where level is not null
        defaultOrgUnitLevelShouldBeFound("level.specified=true");

        // Get all the orgUnitLevelList where level is null
        defaultOrgUnitLevelShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where level greater than or equals to DEFAULT_LEVEL
        defaultOrgUnitLevelShouldBeFound("level.greaterOrEqualThan=" + DEFAULT_LEVEL);

        // Get all the orgUnitLevelList where level greater than or equals to UPDATED_LEVEL
        defaultOrgUnitLevelShouldNotBeFound("level.greaterOrEqualThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrgUnitLevelsByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        orgUnitLevelRepository.saveAndFlush(orgUnitLevel);

        // Get all the orgUnitLevelList where level less than or equals to DEFAULT_LEVEL
        defaultOrgUnitLevelShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the orgUnitLevelList where level less than or equals to UPDATED_LEVEL
        defaultOrgUnitLevelShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrgUnitLevelShouldBeFound(String filter) throws Exception {
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));

        // Check, that the count call also returns 1
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrgUnitLevelShouldNotBeFound(String filter) throws Exception {
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrgUnitLevel() throws Exception {
        // Get the orgUnitLevel
        restOrgUnitLevelMockMvc.perform(get("/api/org-unit-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUnitLevel() throws Exception {
        // Initialize the database
        orgUnitLevelService.save(orgUnitLevel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrgUnitLevelSearchRepository);

        int databaseSizeBeforeUpdate = orgUnitLevelRepository.findAll().size();

        // Update the orgUnitLevel
        OrgUnitLevel updatedOrgUnitLevel = orgUnitLevelRepository.findById(orgUnitLevel.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnitLevel are not directly saved in db
        em.detach(updatedOrgUnitLevel);
        updatedOrgUnitLevel
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL);

        restOrgUnitLevelMockMvc.perform(put("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnitLevel)))
            .andExpect(status().isOk());

        // Validate the OrgUnitLevel in the database
        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeUpdate);
        OrgUnitLevel testOrgUnitLevel = orgUnitLevelList.get(orgUnitLevelList.size() - 1);
        assertThat(testOrgUnitLevel.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrgUnitLevel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrgUnitLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUnitLevel.getLevel()).isEqualTo(UPDATED_LEVEL);

        // Validate the OrgUnitLevel in Elasticsearch
        verify(mockOrgUnitLevelSearchRepository, times(1)).save(testOrgUnitLevel);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUnitLevel() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitLevelRepository.findAll().size();

        // Create the OrgUnitLevel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitLevelMockMvc.perform(put("/api/org-unit-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitLevel)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitLevel in the database
        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrgUnitLevel in Elasticsearch
        verify(mockOrgUnitLevelSearchRepository, times(0)).save(orgUnitLevel);
    }

    @Test
    @Transactional
    public void deleteOrgUnitLevel() throws Exception {
        // Initialize the database
        orgUnitLevelService.save(orgUnitLevel);

        int databaseSizeBeforeDelete = orgUnitLevelRepository.findAll().size();

        // Delete the orgUnitLevel
        restOrgUnitLevelMockMvc.perform(delete("/api/org-unit-levels/{id}", orgUnitLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgUnitLevel> orgUnitLevelList = orgUnitLevelRepository.findAll();
        assertThat(orgUnitLevelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrgUnitLevel in Elasticsearch
        verify(mockOrgUnitLevelSearchRepository, times(1)).deleteById(orgUnitLevel.getId());
    }

    @Test
    @Transactional
    public void searchOrgUnitLevel() throws Exception {
        // Initialize the database
        orgUnitLevelService.save(orgUnitLevel);
        when(mockOrgUnitLevelSearchRepository.search(queryStringQuery("id:" + orgUnitLevel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orgUnitLevel), PageRequest.of(0, 1), 1));
        // Search the orgUnitLevel
        restOrgUnitLevelMockMvc.perform(get("/api/_search/org-unit-levels?query=id:" + orgUnitLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnitLevel.class);
        OrgUnitLevel orgUnitLevel1 = new OrgUnitLevel();
        orgUnitLevel1.setId(1L);
        OrgUnitLevel orgUnitLevel2 = new OrgUnitLevel();
        orgUnitLevel2.setId(orgUnitLevel1.getId());
        assertThat(orgUnitLevel1).isEqualTo(orgUnitLevel2);
        orgUnitLevel2.setId(2L);
        assertThat(orgUnitLevel1).isNotEqualTo(orgUnitLevel2);
        orgUnitLevel1.setId(null);
        assertThat(orgUnitLevel1).isNotEqualTo(orgUnitLevel2);
    }
}
