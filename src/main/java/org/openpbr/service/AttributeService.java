package org.openpbr.service;

import org.openpbr.domain.Attribute;
import org.openpbr.repository.AttributeRepository;
import org.openpbr.repository.search.AttributeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Attribute.
 */
@Service
@Transactional
public class AttributeService {

    private final Logger log = LoggerFactory.getLogger(AttributeService.class);

    private final AttributeRepository attributeRepository;

    private final AttributeSearchRepository attributeSearchRepository;

    public AttributeService(AttributeRepository attributeRepository, AttributeSearchRepository attributeSearchRepository) {
        this.attributeRepository = attributeRepository;
        this.attributeSearchRepository = attributeSearchRepository;
    }

    /**
     * Save a attribute.
     *
     * @param attribute the entity to save
     * @return the persisted entity
     */
    public Attribute save(Attribute attribute) {
        log.debug("Request to save Attribute : {}", attribute);
        Attribute result = attributeRepository.save(attribute);
        attributeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the attributes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findAll(Pageable pageable) {
        log.debug("Request to get all Attributes");
        return attributeRepository.findAll(pageable);
    }


    /**
     * Get one attribute by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Attribute> findOne(Long id) {
        log.debug("Request to get Attribute : {}", id);
        return attributeRepository.findById(id);
    }

    /**
     * Delete the attribute by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Attribute : {}", id);
        attributeRepository.deleteById(id);
        attributeSearchRepository.deleteById(id);
    }

    /**
     * Search for the attribute corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Attribute> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Attributes for query {}", query);
        return attributeSearchRepository.search(queryStringQuery(query), pageable);    }
}
