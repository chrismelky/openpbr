package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.Attribute;
import org.openpbr.domain.OptionSet;
import org.openpbr.repository.AttributeRepository;
import org.openpbr.repository.search.AttributeSearchRepository;
import org.openpbr.service.AttributeService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.AttributeCriteria;
import org.openpbr.service.AttributeQueryService;

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
 * Test class for the AttributeResource REST controller.
 *
 * @see AttributeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class AttributeResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.TEXT;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.LONG_TEXT;

    private static final Boolean DEFAULT_IS_MANDATORY = false;
    private static final Boolean UPDATED_IS_MANDATORY = true;

    private static final Boolean DEFAULT_IS_UNIQUE = false;
    private static final Boolean UPDATED_IS_UNIQUE = true;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Boolean DEFAULT_IS_USER_ATTRIBUTE = false;
    private static final Boolean UPDATED_IS_USER_ATTRIBUTE = true;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeService attributeService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.AttributeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AttributeSearchRepository mockAttributeSearchRepository;

    @Autowired
    private AttributeQueryService attributeQueryService;

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

    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeResource attributeResource = new AttributeResource(attributeService, attributeQueryService);
        this.restAttributeMockMvc = MockMvcBuilders.standaloneSetup(attributeResource)
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
    public static Attribute createEntity(EntityManager em) {
        Attribute attribute = new Attribute()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .valueType(DEFAULT_VALUE_TYPE)
            .isMandatory(DEFAULT_IS_MANDATORY)
            .isUnique(DEFAULT_IS_UNIQUE)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isUserAttribute(DEFAULT_IS_USER_ATTRIBUTE);
        return attribute;
    }

    @Before
    public void initTest() {
        attribute = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttribute() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isCreated());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate + 1);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAttribute.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttribute.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testAttribute.isIsMandatory()).isEqualTo(DEFAULT_IS_MANDATORY);
        assertThat(testAttribute.isIsUnique()).isEqualTo(DEFAULT_IS_UNIQUE);
        assertThat(testAttribute.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testAttribute.isIsUserAttribute()).isEqualTo(DEFAULT_IS_USER_ATTRIBUTE);

        // Validate the Attribute in Elasticsearch
        verify(mockAttributeSearchRepository, times(1)).save(testAttribute);
    }

    @Test
    @Transactional
    public void createAttributeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute with an existing ID
        attribute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Attribute in Elasticsearch
        verify(mockAttributeSearchRepository, times(0)).save(attribute);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setUid(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setName(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setValueType(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsMandatoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setIsMandatory(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsUniqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setIsUnique(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsUserAttributeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setIsUserAttribute(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributes() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].isUnique").value(hasItem(DEFAULT_IS_UNIQUE.booleanValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isUserAttribute").value(hasItem(DEFAULT_IS_USER_ATTRIBUTE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAttribute() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.isMandatory").value(DEFAULT_IS_MANDATORY.booleanValue()))
            .andExpect(jsonPath("$.isUnique").value(DEFAULT_IS_UNIQUE.booleanValue()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isUserAttribute").value(DEFAULT_IS_USER_ATTRIBUTE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllAttributesByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uid equals to DEFAULT_UID
        defaultAttributeShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the attributeList where uid equals to UPDATED_UID
        defaultAttributeShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAttributesByUidIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uid in DEFAULT_UID or UPDATED_UID
        defaultAttributeShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the attributeList where uid equals to UPDATED_UID
        defaultAttributeShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllAttributesByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uid is not null
        defaultAttributeShouldBeFound("uid.specified=true");

        // Get all the attributeList where uid is null
        defaultAttributeShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code equals to DEFAULT_CODE
        defaultAttributeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the attributeList where code equals to UPDATED_CODE
        defaultAttributeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAttributeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the attributeList where code equals to UPDATED_CODE
        defaultAttributeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code is not null
        defaultAttributeShouldBeFound("code.specified=true");

        // Get all the attributeList where code is null
        defaultAttributeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name equals to DEFAULT_NAME
        defaultAttributeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttributeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name is not null
        defaultAttributeShouldBeFound("name.specified=true");

        // Get all the attributeList where name is null
        defaultAttributeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType equals to DEFAULT_VALUE_TYPE
        defaultAttributeShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the attributeList where valueType equals to UPDATED_VALUE_TYPE
        defaultAttributeShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultAttributeShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the attributeList where valueType equals to UPDATED_VALUE_TYPE
        defaultAttributeShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType is not null
        defaultAttributeShouldBeFound("valueType.specified=true");

        // Get all the attributeList where valueType is null
        defaultAttributeShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByIsMandatoryIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isMandatory equals to DEFAULT_IS_MANDATORY
        defaultAttributeShouldBeFound("isMandatory.equals=" + DEFAULT_IS_MANDATORY);

        // Get all the attributeList where isMandatory equals to UPDATED_IS_MANDATORY
        defaultAttributeShouldNotBeFound("isMandatory.equals=" + UPDATED_IS_MANDATORY);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsMandatoryIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isMandatory in DEFAULT_IS_MANDATORY or UPDATED_IS_MANDATORY
        defaultAttributeShouldBeFound("isMandatory.in=" + DEFAULT_IS_MANDATORY + "," + UPDATED_IS_MANDATORY);

        // Get all the attributeList where isMandatory equals to UPDATED_IS_MANDATORY
        defaultAttributeShouldNotBeFound("isMandatory.in=" + UPDATED_IS_MANDATORY);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsMandatoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isMandatory is not null
        defaultAttributeShouldBeFound("isMandatory.specified=true");

        // Get all the attributeList where isMandatory is null
        defaultAttributeShouldNotBeFound("isMandatory.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByIsUniqueIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUnique equals to DEFAULT_IS_UNIQUE
        defaultAttributeShouldBeFound("isUnique.equals=" + DEFAULT_IS_UNIQUE);

        // Get all the attributeList where isUnique equals to UPDATED_IS_UNIQUE
        defaultAttributeShouldNotBeFound("isUnique.equals=" + UPDATED_IS_UNIQUE);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsUniqueIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUnique in DEFAULT_IS_UNIQUE or UPDATED_IS_UNIQUE
        defaultAttributeShouldBeFound("isUnique.in=" + DEFAULT_IS_UNIQUE + "," + UPDATED_IS_UNIQUE);

        // Get all the attributeList where isUnique equals to UPDATED_IS_UNIQUE
        defaultAttributeShouldNotBeFound("isUnique.in=" + UPDATED_IS_UNIQUE);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsUniqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUnique is not null
        defaultAttributeShouldBeFound("isUnique.specified=true");

        // Get all the attributeList where isUnique is null
        defaultAttributeShouldNotBeFound("isUnique.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the attributeList where sortOrder equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder is not null
        defaultAttributeShouldBeFound("sortOrder.specified=true");

        // Get all the attributeList where sortOrder is null
        defaultAttributeShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllAttributesByIsUserAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUserAttribute equals to DEFAULT_IS_USER_ATTRIBUTE
        defaultAttributeShouldBeFound("isUserAttribute.equals=" + DEFAULT_IS_USER_ATTRIBUTE);

        // Get all the attributeList where isUserAttribute equals to UPDATED_IS_USER_ATTRIBUTE
        defaultAttributeShouldNotBeFound("isUserAttribute.equals=" + UPDATED_IS_USER_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsUserAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUserAttribute in DEFAULT_IS_USER_ATTRIBUTE or UPDATED_IS_USER_ATTRIBUTE
        defaultAttributeShouldBeFound("isUserAttribute.in=" + DEFAULT_IS_USER_ATTRIBUTE + "," + UPDATED_IS_USER_ATTRIBUTE);

        // Get all the attributeList where isUserAttribute equals to UPDATED_IS_USER_ATTRIBUTE
        defaultAttributeShouldNotBeFound("isUserAttribute.in=" + UPDATED_IS_USER_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByIsUserAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where isUserAttribute is not null
        defaultAttributeShouldBeFound("isUserAttribute.specified=true");

        // Get all the attributeList where isUserAttribute is null
        defaultAttributeShouldNotBeFound("isUserAttribute.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByOptionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        OptionSet optionSet = OptionSetResourceIntTest.createEntity(em);
        em.persist(optionSet);
        em.flush();
        attribute.setOptionSet(optionSet);
        attributeRepository.saveAndFlush(attribute);
        Long optionSetId = optionSet.getId();

        // Get all the attributeList where optionSet equals to optionSetId
        defaultAttributeShouldBeFound("optionSetId.equals=" + optionSetId);

        // Get all the attributeList where optionSet equals to optionSetId + 1
        defaultAttributeShouldNotBeFound("optionSetId.equals=" + (optionSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAttributeShouldBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].isUnique").value(hasItem(DEFAULT_IS_UNIQUE.booleanValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isUserAttribute").value(hasItem(DEFAULT_IS_USER_ATTRIBUTE.booleanValue())));

        // Check, that the count call also returns 1
        restAttributeMockMvc.perform(get("/api/attributes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAttributeShouldNotBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttributeMockMvc.perform(get("/api/attributes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAttributeSearchRepository);

        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findById(attribute.getId()).get();
        // Disconnect from session so that the updates on updatedAttribute are not directly saved in db
        em.detach(updatedAttribute);
        updatedAttribute
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .valueType(UPDATED_VALUE_TYPE)
            .isMandatory(UPDATED_IS_MANDATORY)
            .isUnique(UPDATED_IS_UNIQUE)
            .sortOrder(UPDATED_SORT_ORDER)
            .isUserAttribute(UPDATED_IS_USER_ATTRIBUTE);

        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttribute)))
            .andExpect(status().isOk());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAttribute.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttribute.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testAttribute.isIsMandatory()).isEqualTo(UPDATED_IS_MANDATORY);
        assertThat(testAttribute.isIsUnique()).isEqualTo(UPDATED_IS_UNIQUE);
        assertThat(testAttribute.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAttribute.isIsUserAttribute()).isEqualTo(UPDATED_IS_USER_ATTRIBUTE);

        // Validate the Attribute in Elasticsearch
        verify(mockAttributeSearchRepository, times(1)).save(testAttribute);
    }

    @Test
    @Transactional
    public void updateNonExistingAttribute() throws Exception {
        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Create the Attribute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Attribute in Elasticsearch
        verify(mockAttributeSearchRepository, times(0)).save(attribute);
    }

    @Test
    @Transactional
    public void deleteAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeDelete = attributeRepository.findAll().size();

        // Delete the attribute
        restAttributeMockMvc.perform(delete("/api/attributes/{id}", attribute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Attribute in Elasticsearch
        verify(mockAttributeSearchRepository, times(1)).deleteById(attribute.getId());
    }

    @Test
    @Transactional
    public void searchAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);
        when(mockAttributeSearchRepository.search(queryStringQuery("id:" + attribute.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(attribute), PageRequest.of(0, 1), 1));
        // Search the attribute
        restAttributeMockMvc.perform(get("/api/_search/attributes?query=id:" + attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].isUnique").value(hasItem(DEFAULT_IS_UNIQUE.booleanValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isUserAttribute").value(hasItem(DEFAULT_IS_USER_ATTRIBUTE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        Attribute attribute2 = new Attribute();
        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);
        attribute2.setId(2L);
        assertThat(attribute1).isNotEqualTo(attribute2);
        attribute1.setId(null);
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
