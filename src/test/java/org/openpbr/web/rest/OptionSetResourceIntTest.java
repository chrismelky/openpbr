package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OptionSet;
import org.openpbr.domain.OptionValue;
import org.openpbr.repository.OptionSetRepository;
import org.openpbr.repository.search.OptionSetSearchRepository;
import org.openpbr.service.OptionSetService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OptionSetCriteria;
import org.openpbr.service.OptionSetQueryService;

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

import org.openpbr.domain.enumeration.ValueType;
/**
 * Test class for the OptionSetResource REST controller.
 *
 * @see OptionSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OptionSetResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.TEXT;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.LONG_TEXT;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    @Autowired
    private OptionSetRepository optionSetRepository;

    @Autowired
    private OptionSetService optionSetService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OptionSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private OptionSetSearchRepository mockOptionSetSearchRepository;

    @Autowired
    private OptionSetQueryService optionSetQueryService;

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

    private MockMvc restOptionSetMockMvc;

    private OptionSet optionSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OptionSetResource optionSetResource = new OptionSetResource(optionSetService, optionSetQueryService);
        this.restOptionSetMockMvc = MockMvcBuilders.standaloneSetup(optionSetResource)
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
    public static OptionSet createEntity(EntityManager em) {
        OptionSet optionSet = new OptionSet()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .valueType(DEFAULT_VALUE_TYPE)
            .sortOrder(DEFAULT_SORT_ORDER);
        // Add required entity
        OptionValue optionValue = OptionValueResourceIntTest.createEntity(em);
        em.persist(optionValue);
        em.flush();
        optionSet.getOptionValues().add(optionValue);
        return optionSet;
    }

    @Before
    public void initTest() {
        optionSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createOptionSet() throws Exception {
        int databaseSizeBeforeCreate = optionSetRepository.findAll().size();

        // Create the OptionSet
        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isCreated());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeCreate + 1);
        OptionSet testOptionSet = optionSetList.get(optionSetList.size() - 1);
        assertThat(testOptionSet.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOptionSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOptionSet.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOptionSet.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);

        // Validate the OptionSet in Elasticsearch
        verify(mockOptionSetSearchRepository, times(1)).save(testOptionSet);
    }

    @Test
    @Transactional
    public void createOptionSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionSetRepository.findAll().size();

        // Create the OptionSet with an existing ID
        optionSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the OptionSet in Elasticsearch
        verify(mockOptionSetSearchRepository, times(0)).save(optionSet);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionSetRepository.findAll().size();
        // set the field null
        optionSet.setUid(null);

        // Create the OptionSet, which fails.

        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionSetRepository.findAll().size();
        // set the field null
        optionSet.setName(null);

        // Create the OptionSet, which fails.

        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionSetRepository.findAll().size();
        // set the field null
        optionSet.setValueType(null);

        // Create the OptionSet, which fails.

        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOptionSets() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }
    
    @Test
    @Transactional
    public void getOptionSet() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get the optionSet
        restOptionSetMockMvc.perform(get("/api/option-sets/{id}", optionSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(optionSet.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uid equals to DEFAULT_UID
        defaultOptionSetShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the optionSetList where uid equals to UPDATED_UID
        defaultOptionSetShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uid in DEFAULT_UID or UPDATED_UID
        defaultOptionSetShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the optionSetList where uid equals to UPDATED_UID
        defaultOptionSetShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uid is not null
        defaultOptionSetShouldBeFound("uid.specified=true");

        // Get all the optionSetList where uid is null
        defaultOptionSetShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code equals to DEFAULT_CODE
        defaultOptionSetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionSetList where code equals to UPDATED_CODE
        defaultOptionSetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionSetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionSetList where code equals to UPDATED_CODE
        defaultOptionSetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code is not null
        defaultOptionSetShouldBeFound("code.specified=true");

        // Get all the optionSetList where code is null
        defaultOptionSetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name equals to DEFAULT_NAME
        defaultOptionSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the optionSetList where name equals to UPDATED_NAME
        defaultOptionSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOptionSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the optionSetList where name equals to UPDATED_NAME
        defaultOptionSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name is not null
        defaultOptionSetShouldBeFound("name.specified=true");

        // Get all the optionSetList where name is null
        defaultOptionSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where valueType equals to DEFAULT_VALUE_TYPE
        defaultOptionSetShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the optionSetList where valueType equals to UPDATED_VALUE_TYPE
        defaultOptionSetShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultOptionSetShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the optionSetList where valueType equals to UPDATED_VALUE_TYPE
        defaultOptionSetShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where valueType is not null
        defaultOptionSetShouldBeFound("valueType.specified=true");

        // Get all the optionSetList where valueType is null
        defaultOptionSetShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultOptionSetShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the optionSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOptionSetShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionSetsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultOptionSetShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the optionSetList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOptionSetShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionSetsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where sortOrder is not null
        defaultOptionSetShouldBeFound("sortOrder.specified=true");

        // Get all the optionSetList where sortOrder is null
        defaultOptionSetShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultOptionSetShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the optionSetList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultOptionSetShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionSetsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultOptionSetShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the optionSetList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultOptionSetShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllOptionSetsByOptionValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        OptionValue optionValues = OptionValueResourceIntTest.createEntity(em);
        em.persist(optionValues);
        em.flush();
        optionSet.addOptionValues(optionValues);
        optionSetRepository.saveAndFlush(optionSet);
        Long optionValuesId = optionValues.getId();

        // Get all the optionSetList where optionValues equals to optionValuesId
        defaultOptionSetShouldBeFound("optionValuesId.equals=" + optionValuesId);

        // Get all the optionSetList where optionValues equals to optionValuesId + 1
        defaultOptionSetShouldNotBeFound("optionValuesId.equals=" + (optionValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOptionSetShouldBeFound(String filter) throws Exception {
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restOptionSetMockMvc.perform(get("/api/option-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOptionSetShouldNotBeFound(String filter) throws Exception {
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionSetMockMvc.perform(get("/api/option-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOptionSet() throws Exception {
        // Get the optionSet
        restOptionSetMockMvc.perform(get("/api/option-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOptionSet() throws Exception {
        // Initialize the database
        optionSetService.save(optionSet);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOptionSetSearchRepository);

        int databaseSizeBeforeUpdate = optionSetRepository.findAll().size();

        // Update the optionSet
        OptionSet updatedOptionSet = optionSetRepository.findById(optionSet.getId()).get();
        // Disconnect from session so that the updates on updatedOptionSet are not directly saved in db
        em.detach(updatedOptionSet);
        updatedOptionSet
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .valueType(UPDATED_VALUE_TYPE)
            .sortOrder(UPDATED_SORT_ORDER);

        restOptionSetMockMvc.perform(put("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOptionSet)))
            .andExpect(status().isOk());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeUpdate);
        OptionSet testOptionSet = optionSetList.get(optionSetList.size() - 1);
        assertThat(testOptionSet.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOptionSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOptionSet.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOptionSet.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);

        // Validate the OptionSet in Elasticsearch
        verify(mockOptionSetSearchRepository, times(1)).save(testOptionSet);
    }

    @Test
    @Transactional
    public void updateNonExistingOptionSet() throws Exception {
        int databaseSizeBeforeUpdate = optionSetRepository.findAll().size();

        // Create the OptionSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionSetMockMvc.perform(put("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OptionSet in Elasticsearch
        verify(mockOptionSetSearchRepository, times(0)).save(optionSet);
    }

    @Test
    @Transactional
    public void deleteOptionSet() throws Exception {
        // Initialize the database
        optionSetService.save(optionSet);

        int databaseSizeBeforeDelete = optionSetRepository.findAll().size();

        // Delete the optionSet
        restOptionSetMockMvc.perform(delete("/api/option-sets/{id}", optionSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OptionSet in Elasticsearch
        verify(mockOptionSetSearchRepository, times(1)).deleteById(optionSet.getId());
    }

    @Test
    @Transactional
    public void searchOptionSet() throws Exception {
        // Initialize the database
        optionSetService.save(optionSet);
        when(mockOptionSetSearchRepository.search(queryStringQuery("id:" + optionSet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(optionSet), PageRequest.of(0, 1), 1));
        // Search the optionSet
        restOptionSetMockMvc.perform(get("/api/_search/option-sets?query=id:" + optionSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionSet.class);
        OptionSet optionSet1 = new OptionSet();
        optionSet1.setId(1L);
        OptionSet optionSet2 = new OptionSet();
        optionSet2.setId(optionSet1.getId());
        assertThat(optionSet1).isEqualTo(optionSet2);
        optionSet2.setId(2L);
        assertThat(optionSet1).isNotEqualTo(optionSet2);
        optionSet1.setId(null);
        assertThat(optionSet1).isNotEqualTo(optionSet2);
    }
}
