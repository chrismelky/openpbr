package org.openpbr.service;

import org.openpbr.domain.OptionValue;
import org.openpbr.repository.OptionValueRepository;
import org.openpbr.repository.search.OptionValueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OptionValue.
 */
@Service
@Transactional
public class OptionValueService {

    private final Logger log = LoggerFactory.getLogger(OptionValueService.class);

    private final OptionValueRepository optionValueRepository;

    private final OptionValueSearchRepository optionValueSearchRepository;

    public OptionValueService(OptionValueRepository optionValueRepository, OptionValueSearchRepository optionValueSearchRepository) {
        this.optionValueRepository = optionValueRepository;
        this.optionValueSearchRepository = optionValueSearchRepository;
    }

    /**
     * Save a optionValue.
     *
     * @param optionValue the entity to save
     * @return the persisted entity
     */
    public OptionValue save(OptionValue optionValue) {
        log.debug("Request to save OptionValue : {}", optionValue);
        OptionValue result = optionValueRepository.save(optionValue);
        optionValueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the optionValues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OptionValue> findAll(Pageable pageable) {
        log.debug("Request to get all OptionValues");
        return optionValueRepository.findAll(pageable);
    }


    /**
     * Get one optionValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OptionValue> findOne(Long id) {
        log.debug("Request to get OptionValue : {}", id);
        return optionValueRepository.findById(id);
    }

    /**
     * Delete the optionValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OptionValue : {}", id);
        optionValueRepository.deleteById(id);
        optionValueSearchRepository.deleteById(id);
    }

    /**
     * Search for the optionValue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OptionValue> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OptionValues for query {}", query);
        return optionValueSearchRepository.search(queryStringQuery(query), pageable);    }
}
