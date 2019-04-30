package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OptionValue;
import org.openpbr.domain.OptionSet;
import org.openpbr.repository.OptionValueRepository;
import org.openpbr.repository.search.OptionValueSearchRepository;
import org.openpbr.service.OptionValueService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OptionValueCriteria;
import org.openpbr.service.OptionValueQueryService;

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
 * Test class for the OptionValueResource REST controller.
 *
 * @see OptionValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OptionValueResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    @Autowired
    private OptionValueRepository optionValueRepository;

    @Autowired
    private OptionValueService optionValueService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OptionValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private OptionValueSearchRepository mockOptionValueSearchRepository;

    @Autowired
    private OptionValueQueryService optionValueQueryService;

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

    private MockMvc restOptionValueMockMvc;

    private OptionValue optionValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OptionValueResource optionValueResource = new OptionValueResource(optionValueService, optionValueQueryService);
        this.restOptionValueMockMvc = MockMvcBuilders.standaloneSetup(optionValueResource)
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
    public static OptionValue createEntity(EntityManager em) {
        OptionValue optionValue = new OptionValue()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .sortOrder(DEFAULT_SORT_ORDER);
        return optionValue;
    }

    @Before
    public void initTest() {
        optionValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createOptionValue() throws Exception {
        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();

        // Create the OptionValue
        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isCreated());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate + 1);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOptionValue.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionValue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOptionValue.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);

        // Validate the OptionValue in Elasticsearch
        verify(mockOptionValueSearchRepository, times(1)).save(testOptionValue);
    }

    @Test
    @Transactional
    public void createOptionValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();

        // Create the OptionValue with an existing ID
        optionValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the OptionValue in Elasticsearch
        verify(mockOptionValueSearchRepository, times(0)).save(optionValue);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionValueRepository.findAll().size();
        // set the field null
        optionValue.setUid(null);

        // Create the OptionValue, which fails.

        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionValueRepository.findAll().size();
        // set the field null
        optionValue.setName(null);

        // Create the OptionValue, which fails.

        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOptionValues() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList
        restOptionValueMockMvc.perform(get("/api/option-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }
    
    @Test
    @Transactional
    public void getOptionValue() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get the optionValue
        restOptionValueMockMvc.perform(get("/api/option-values/{id}", optionValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(optionValue.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    public void getAllOptionValuesByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where uid equals to DEFAULT_UID
        defaultOptionValueShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the optionValueList where uid equals to UPDATED_UID
        defaultOptionValueShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByUidIsInShouldWork() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where uid in DEFAULT_UID or UPDATED_UID
        defaultOptionValueShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the optionValueList where uid equals to UPDATED_UID
        defaultOptionValueShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where uid is not null
        defaultOptionValueShouldBeFound("uid.specified=true");

        // Get all the optionValueList where uid is null
        defaultOptionValueShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionValuesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where code equals to DEFAULT_CODE
        defaultOptionValueShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionValueList where code equals to UPDATED_CODE
        defaultOptionValueShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionValueShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionValueList where code equals to UPDATED_CODE
        defaultOptionValueShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where code is not null
        defaultOptionValueShouldBeFound("code.specified=true");

        // Get all the optionValueList where code is null
        defaultOptionValueShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionValuesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where name equals to DEFAULT_NAME
        defaultOptionValueShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the optionValueList where name equals to UPDATED_NAME
        defaultOptionValueShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOptionValueShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the optionValueList where name equals to UPDATED_NAME
        defaultOptionValueShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionValuesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where name is not null
        defaultOptionValueShouldBeFound("name.specified=true");

        // Get all the optionValueList where name is null
        defaultOptionValueShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionValuesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultOptionValueShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the optionValueList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOptionValueShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionValuesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultOptionValueShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the optionValueList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOptionValueShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionValuesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where sortOrder is not null
        defaultOptionValueShouldBeFound("sortOrder.specified=true");

        // Get all the optionValueList where sortOrder is null
        defaultOptionValueShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionValuesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultOptionValueShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the optionValueList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultOptionValueShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOptionValuesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultOptionValueShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the optionValueList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultOptionValueShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllOptionValuesByOptionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        OptionSet optionSet = OptionSetResourceIntTest.createEntity(em);
        em.persist(optionSet);
        em.flush();
        optionValue.setOptionSet(optionSet);
        optionValueRepository.saveAndFlush(optionValue);
        Long optionSetId = optionSet.getId();

        // Get all the optionValueList where optionSet equals to optionSetId
        defaultOptionValueShouldBeFound("optionSetId.equals=" + optionSetId);

        // Get all the optionValueList where optionSet equals to optionSetId + 1
        defaultOptionValueShouldNotBeFound("optionSetId.equals=" + (optionSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOptionValueShouldBeFound(String filter) throws Exception {
        restOptionValueMockMvc.perform(get("/api/option-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restOptionValueMockMvc.perform(get("/api/option-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOptionValueShouldNotBeFound(String filter) throws Exception {
        restOptionValueMockMvc.perform(get("/api/option-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionValueMockMvc.perform(get("/api/option-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOptionValue() throws Exception {
        // Get the optionValue
        restOptionValueMockMvc.perform(get("/api/option-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOptionValue() throws Exception {
        // Initialize the database
        optionValueService.save(optionValue);
        // As the test used the treeNodeService layer, reset the Elasticsearch mock repository
        reset(mockOptionValueSearchRepository);

        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Update the optionValue
        OptionValue updatedOptionValue = optionValueRepository.findById(optionValue.getId()).get();
        // Disconnect from session so that the updates on updatedOptionValue are not directly saved in db
        em.detach(updatedOptionValue);
        updatedOptionValue
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortOrder(UPDATED_SORT_ORDER);

        restOptionValueMockMvc.perform(put("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOptionValue)))
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOptionValue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionValue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOptionValue.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);

        // Validate the OptionValue in Elasticsearch
        verify(mockOptionValueSearchRepository, times(1)).save(testOptionValue);
    }

    @Test
    @Transactional
    public void updateNonExistingOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Create the OptionValue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionValueMockMvc.perform(put("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OptionValue in Elasticsearch
        verify(mockOptionValueSearchRepository, times(0)).save(optionValue);
    }

    @Test
    @Transactional
    public void deleteOptionValue() throws Exception {
        // Initialize the database
        optionValueService.save(optionValue);

        int databaseSizeBeforeDelete = optionValueRepository.findAll().size();

        // Delete the optionValue
        restOptionValueMockMvc.perform(delete("/api/option-values/{id}", optionValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OptionValue in Elasticsearch
        verify(mockOptionValueSearchRepository, times(1)).deleteById(optionValue.getId());
    }

    @Test
    @Transactional
    public void searchOptionValue() throws Exception {
        // Initialize the database
        optionValueService.save(optionValue);
        when(mockOptionValueSearchRepository.search(queryStringQuery("id:" + optionValue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(optionValue), PageRequest.of(0, 1), 1));
        // Search the optionValue
        restOptionValueMockMvc.perform(get("/api/_search/option-values?query=id:" + optionValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionValue.class);
        OptionValue optionValue1 = new OptionValue();
        optionValue1.setId(1L);
        OptionValue optionValue2 = new OptionValue();
        optionValue2.setId(optionValue1.getId());
        assertThat(optionValue1).isEqualTo(optionValue2);
        optionValue2.setId(2L);
        assertThat(optionValue1).isNotEqualTo(optionValue2);
        optionValue1.setId(null);
        assertThat(optionValue1).isNotEqualTo(optionValue2);
    }
}
