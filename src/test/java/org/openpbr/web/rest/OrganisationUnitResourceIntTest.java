package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.OrganisationUnit;
import org.openpbr.domain.OrganisationUnit;
import org.openpbr.repository.OrganisationUnitRepository;
import org.openpbr.repository.search.OrganisationUnitSearchRepository;
import org.openpbr.service.OrganisationUnitService;
import org.openpbr.web.rest.errors.ExceptionTranslator;
import org.openpbr.service.dto.OrganisationUnitCriteria;
import org.openpbr.service.OrganisationUnitQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the OrganisationUnitResource REST controller.
 *
 * @see OrganisationUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class OrganisationUnitResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CLOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMNER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMNER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.OrganisationUnitSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrganisationUnitSearchRepository mockOrganisationUnitSearchRepository;

    @Autowired
    private OrganisationUnitQueryService organisationUnitQueryService;

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

    private MockMvc restOrganisationUnitMockMvc;

    private OrganisationUnit organisationUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganisationUnitResource organisationUnitResource = new OrganisationUnitResource(organisationUnitService, organisationUnitQueryService);
        this.restOrganisationUnitMockMvc = MockMvcBuilders.standaloneSetup(organisationUnitResource)
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
    public static OrganisationUnit createEntity(EntityManager em) {
        OrganisationUnit organisationUnit = new OrganisationUnit()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL)
            .openingDate(DEFAULT_OPENING_DATE)
            .closedDate(DEFAULT_CLOSED_DATE)
            .url(DEFAULT_URL)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMNER)
            .sortOrder(DEFAULT_SORT_ORDER)
            .isActive(DEFAULT_IS_ACTIVE);
        return organisationUnit;
    }

    @Before
    public void initTest() {
        organisationUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganisationUnit() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit
        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isCreated());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnit.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganisationUnit.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOrganisationUnit.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMNER);
        assertThat(testOrganisationUnit.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testOrganisationUnit.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the OrganisationUnit in Elasticsearch
        verify(mockOrganisationUnitSearchRepository, times(1)).save(testOrganisationUnit);
    }

    @Test
    @Transactional
    public void createOrganisationUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit with an existing ID
        organisationUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrganisationUnit in Elasticsearch
        verify(mockOrganisationUnitSearchRepository, times(0)).save(organisationUnit);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setUid(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setName(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setLevel(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnits() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumner").value(hasItem(DEFAULT_PHONE_NUMNER.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/{id}", organisationUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnit.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumner").value(DEFAULT_PHONE_NUMNER.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uid equals to DEFAULT_UID
        defaultOrganisationUnitShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the organisationUnitList where uid equals to UPDATED_UID
        defaultOrganisationUnitShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uid in DEFAULT_UID or UPDATED_UID
        defaultOrganisationUnitShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the organisationUnitList where uid equals to UPDATED_UID
        defaultOrganisationUnitShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uid is not null
        defaultOrganisationUnitShouldBeFound("uid.specified=true");

        // Get all the organisationUnitList where uid is null
        defaultOrganisationUnitShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code equals to DEFAULT_CODE
        defaultOrganisationUnitShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the organisationUnitList where code equals to UPDATED_CODE
        defaultOrganisationUnitShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrganisationUnitShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the organisationUnitList where code equals to UPDATED_CODE
        defaultOrganisationUnitShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code is not null
        defaultOrganisationUnitShouldBeFound("code.specified=true");

        // Get all the organisationUnitList where code is null
        defaultOrganisationUnitShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name equals to DEFAULT_NAME
        defaultOrganisationUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organisationUnitList where name equals to UPDATED_NAME
        defaultOrganisationUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganisationUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organisationUnitList where name equals to UPDATED_NAME
        defaultOrganisationUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name is not null
        defaultOrganisationUnitShouldBeFound("name.specified=true");

        // Get all the organisationUnitList where name is null
        defaultOrganisationUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where level equals to DEFAULT_LEVEL
        defaultOrganisationUnitShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the organisationUnitList where level equals to UPDATED_LEVEL
        defaultOrganisationUnitShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultOrganisationUnitShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the organisationUnitList where level equals to UPDATED_LEVEL
        defaultOrganisationUnitShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where level is not null
        defaultOrganisationUnitShouldBeFound("level.specified=true");

        // Get all the organisationUnitList where level is null
        defaultOrganisationUnitShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where level greater than or equals to DEFAULT_LEVEL
        defaultOrganisationUnitShouldBeFound("level.greaterOrEqualThan=" + DEFAULT_LEVEL);

        // Get all the organisationUnitList where level greater than or equals to UPDATED_LEVEL
        defaultOrganisationUnitShouldNotBeFound("level.greaterOrEqualThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where level less than or equals to DEFAULT_LEVEL
        defaultOrganisationUnitShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the organisationUnitList where level less than or equals to UPDATED_LEVEL
        defaultOrganisationUnitShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.equals=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.equals=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate in DEFAULT_OPENING_DATE or UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.in=" + DEFAULT_OPENING_DATE + "," + UPDATED_OPENING_DATE);

        // Get all the organisationUnitList where openingDate equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.in=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate is not null
        defaultOrganisationUnitShouldBeFound("openingDate.specified=true");

        // Get all the organisationUnitList where openingDate is null
        defaultOrganisationUnitShouldNotBeFound("openingDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate greater than or equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.greaterOrEqualThan=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate greater than or equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.greaterOrEqualThan=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate less than or equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.lessThan=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate less than or equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.lessThan=" + UPDATED_OPENING_DATE);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.equals=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate in DEFAULT_CLOSED_DATE or UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.in=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate is not null
        defaultOrganisationUnitShouldBeFound("closedDate.specified=true");

        // Get all the organisationUnitList where closedDate is null
        defaultOrganisationUnitShouldNotBeFound("closedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate greater than or equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.greaterOrEqualThan=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate greater than or equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.greaterOrEqualThan=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate less than or equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.lessThan=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate less than or equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.lessThan=" + UPDATED_CLOSED_DATE);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url equals to DEFAULT_URL
        defaultOrganisationUnitShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the organisationUnitList where url equals to UPDATED_URL
        defaultOrganisationUnitShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url in DEFAULT_URL or UPDATED_URL
        defaultOrganisationUnitShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the organisationUnitList where url equals to UPDATED_URL
        defaultOrganisationUnitShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url is not null
        defaultOrganisationUnitShouldBeFound("url.specified=true");

        // Get all the organisationUnitList where url is null
        defaultOrganisationUnitShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude equals to DEFAULT_LATITUDE
        defaultOrganisationUnitShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the organisationUnitList where latitude equals to UPDATED_LATITUDE
        defaultOrganisationUnitShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultOrganisationUnitShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the organisationUnitList where latitude equals to UPDATED_LATITUDE
        defaultOrganisationUnitShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude is not null
        defaultOrganisationUnitShouldBeFound("latitude.specified=true");

        // Get all the organisationUnitList where latitude is null
        defaultOrganisationUnitShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude equals to DEFAULT_LONGITUDE
        defaultOrganisationUnitShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the organisationUnitList where longitude equals to UPDATED_LONGITUDE
        defaultOrganisationUnitShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultOrganisationUnitShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the organisationUnitList where longitude equals to UPDATED_LONGITUDE
        defaultOrganisationUnitShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude is not null
        defaultOrganisationUnitShouldBeFound("longitude.specified=true");

        // Get all the organisationUnitList where longitude is null
        defaultOrganisationUnitShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address equals to DEFAULT_ADDRESS
        defaultOrganisationUnitShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the organisationUnitList where address equals to UPDATED_ADDRESS
        defaultOrganisationUnitShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultOrganisationUnitShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the organisationUnitList where address equals to UPDATED_ADDRESS
        defaultOrganisationUnitShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address is not null
        defaultOrganisationUnitShouldBeFound("address.specified=true");

        // Get all the organisationUnitList where address is null
        defaultOrganisationUnitShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email equals to DEFAULT_EMAIL
        defaultOrganisationUnitShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the organisationUnitList where email equals to UPDATED_EMAIL
        defaultOrganisationUnitShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOrganisationUnitShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the organisationUnitList where email equals to UPDATED_EMAIL
        defaultOrganisationUnitShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email is not null
        defaultOrganisationUnitShouldBeFound("email.specified=true");

        // Get all the organisationUnitList where email is null
        defaultOrganisationUnitShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner equals to DEFAULT_PHONE_NUMNER
        defaultOrganisationUnitShouldBeFound("phoneNumner.equals=" + DEFAULT_PHONE_NUMNER);

        // Get all the organisationUnitList where phoneNumner equals to UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.equals=" + UPDATED_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner in DEFAULT_PHONE_NUMNER or UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldBeFound("phoneNumner.in=" + DEFAULT_PHONE_NUMNER + "," + UPDATED_PHONE_NUMNER);

        // Get all the organisationUnitList where phoneNumner equals to UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.in=" + UPDATED_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner is not null
        defaultOrganisationUnitShouldBeFound("phoneNumner.specified=true");

        // Get all the organisationUnitList where phoneNumner is null
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultOrganisationUnitShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the organisationUnitList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrganisationUnitShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultOrganisationUnitShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the organisationUnitList where sortOrder equals to UPDATED_SORT_ORDER
        defaultOrganisationUnitShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where sortOrder is not null
        defaultOrganisationUnitShouldBeFound("sortOrder.specified=true");

        // Get all the organisationUnitList where sortOrder is null
        defaultOrganisationUnitShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultOrganisationUnitShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the organisationUnitList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultOrganisationUnitShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultOrganisationUnitShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the organisationUnitList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultOrganisationUnitShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOrganisationUnitShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the organisationUnitList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrganisationUnitShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOrganisationUnitShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the organisationUnitList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrganisationUnitShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where isActive is not null
        defaultOrganisationUnitShouldBeFound("isActive.specified=true");

        // Get all the organisationUnitList where isActive is null
        defaultOrganisationUnitShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        OrganisationUnit parent = OrganisationUnitResourceIntTest.createEntity(em);
        em.persist(parent);
        em.flush();
        organisationUnit.setParent(parent);
        organisationUnitRepository.saveAndFlush(organisationUnit);
        Long parentId = parent.getId();

        // Get all the organisationUnitList where parent equals to parentId
        defaultOrganisationUnitShouldBeFound("parentId.equals=" + parentId);

        // Get all the organisationUnitList where parent equals to parentId + 1
        defaultOrganisationUnitShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrganisationUnitShouldBeFound(String filter) throws Exception {
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumner").value(hasItem(DEFAULT_PHONE_NUMNER)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrganisationUnitShouldNotBeFound(String filter) throws Exception {
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrganisationUnit() throws Exception {
        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitService.save(organisationUnit);
        // As the test used the treeNodeService layer, reset the Elasticsearch mock repository
        reset(mockOrganisationUnitSearchRepository);

        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Update the organisationUnit
        OrganisationUnit updatedOrganisationUnit = organisationUnitRepository.findById(organisationUnit.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisationUnit are not directly saved in db
        em.detach(updatedOrganisationUnit);
        updatedOrganisationUnit
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .openingDate(UPDATED_OPENING_DATE)
            .closedDate(UPDATED_CLOSED_DATE)
            .url(UPDATED_URL)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMNER)
            .sortOrder(UPDATED_SORT_ORDER)
            .isActive(UPDATED_IS_ACTIVE);

        restOrganisationUnitMockMvc.perform(put("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnit)))
            .andExpect(status().isOk());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnit.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganisationUnit.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOrganisationUnit.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMNER);
        assertThat(testOrganisationUnit.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testOrganisationUnit.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the OrganisationUnit in Elasticsearch
        verify(mockOrganisationUnitSearchRepository, times(1)).save(testOrganisationUnit);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationUnitMockMvc.perform(put("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrganisationUnit in Elasticsearch
        verify(mockOrganisationUnitSearchRepository, times(0)).save(organisationUnit);
    }

    @Test
    @Transactional
    public void deleteOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitService.save(organisationUnit);

        int databaseSizeBeforeDelete = organisationUnitRepository.findAll().size();

        // Delete the organisationUnit
        restOrganisationUnitMockMvc.perform(delete("/api/organisation-units/{id}", organisationUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrganisationUnit in Elasticsearch
        verify(mockOrganisationUnitSearchRepository, times(1)).deleteById(organisationUnit.getId());
    }

    @Test
    @Transactional
    public void searchOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitService.save(organisationUnit);
        when(mockOrganisationUnitSearchRepository.search(queryStringQuery("id:" + organisationUnit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(organisationUnit), PageRequest.of(0, 1), 1));
        // Search the organisationUnit
        restOrganisationUnitMockMvc.perform(get("/api/_search/organisation-units?query=id:" + organisationUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumner").value(hasItem(DEFAULT_PHONE_NUMNER)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationUnit.class);
        OrganisationUnit organisationUnit1 = new OrganisationUnit();
        organisationUnit1.setId(1L);
        OrganisationUnit organisationUnit2 = new OrganisationUnit();
        organisationUnit2.setId(organisationUnit1.getId());
        assertThat(organisationUnit1).isEqualTo(organisationUnit2);
        organisationUnit2.setId(2L);
        assertThat(organisationUnit1).isNotEqualTo(organisationUnit2);
        organisationUnit1.setId(null);
        assertThat(organisationUnit1).isNotEqualTo(organisationUnit2);
    }
}
