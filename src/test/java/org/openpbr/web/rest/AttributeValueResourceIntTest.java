package org.openpbr.web.rest;

import org.openpbr.OpenpbrApp;

import org.openpbr.domain.AttributeValue;
import org.openpbr.domain.Attribute;
import org.openpbr.repository.AttributeValueRepository;
import org.openpbr.repository.search.AttributeValueSearchRepository;
import org.openpbr.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Test class for the AttributeValueResource REST controller.
 *
 * @see AttributeValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenpbrApp.class)
public class AttributeValueResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    /**
     * This repository is mocked in the org.openpbr.repository.search test package.
     *
     * @see org.openpbr.repository.search.AttributeValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private AttributeValueSearchRepository mockAttributeValueSearchRepository;

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

    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeValueResource attributeValueResource = new AttributeValueResource(attributeValueRepository, mockAttributeValueSearchRepository);
        this.restAttributeValueMockMvc = MockMvcBuilders.standaloneSetup(attributeValueResource)
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
    public static AttributeValue createEntity(EntityManager em) {
        AttributeValue attributeValue = new AttributeValue()
            .value(DEFAULT_VALUE);
        // Add required entity
        Attribute attribute = AttributeResourceIntTest.createEntity(em);
        em.persist(attribute);
        em.flush();
        attributeValue.setAttribute(attribute);
        return attributeValue;
    }

    @Before
    public void initTest() {
        attributeValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttributeValue() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // Create the AttributeValue
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isCreated());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).save(testAttributeValue);
    }

    @Test
    @Transactional
    public void createAttributeValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // Create the AttributeValue with an existing ID
        attributeValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(0)).save(attributeValue);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeValueRepository.findAll().size();
        // set the field null
        attributeValue.setValue(null);

        // Create the AttributeValue, which fails.

        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributeValues() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc.perform(get("/api/attribute-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).get();
        // Disconnect from session so that the updates on updatedAttributeValue are not directly saved in db
        em.detach(updatedAttributeValue);
        updatedAttributeValue
            .value(UPDATED_VALUE);

        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttributeValue)))
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).save(testAttributeValue);
    }

    @Test
    @Transactional
    public void updateNonExistingAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Create the AttributeValue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(0)).save(attributeValue);
    }

    @Test
    @Transactional
    public void deleteAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeDelete = attributeValueRepository.findAll().size();

        // Delete the attributeValue
        restAttributeValueMockMvc.perform(delete("/api/attribute-values/{id}", attributeValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).deleteById(attributeValue.getId());
    }

    @Test
    @Transactional
    public void searchAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);
        when(mockAttributeValueSearchRepository.search(queryStringQuery("id:" + attributeValue.getId())))
            .thenReturn(Collections.singletonList(attributeValue));
        // Search the attributeValue
        restAttributeValueMockMvc.perform(get("/api/_search/attribute-values?query=id:" + attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValue.class);
        AttributeValue attributeValue1 = new AttributeValue();
        attributeValue1.setId(1L);
        AttributeValue attributeValue2 = new AttributeValue();
        attributeValue2.setId(attributeValue1.getId());
        assertThat(attributeValue1).isEqualTo(attributeValue2);
        attributeValue2.setId(2L);
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
        attributeValue1.setId(null);
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
    }
}
