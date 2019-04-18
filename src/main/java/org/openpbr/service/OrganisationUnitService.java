package org.openpbr.service;

import org.openpbr.domain.OrganisationUnit;
import org.openpbr.repository.OrganisationUnitRepository;
import org.openpbr.repository.search.OrganisationUnitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrganisationUnit.
 */
@Service
@Transactional
public class OrganisationUnitService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitService.class);

    private final OrganisationUnitRepository organisationUnitRepository;

    private final OrganisationUnitSearchRepository organisationUnitSearchRepository;

    public OrganisationUnitService(OrganisationUnitRepository organisationUnitRepository, OrganisationUnitSearchRepository organisationUnitSearchRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
        this.organisationUnitSearchRepository = organisationUnitSearchRepository;
    }

    /**
     * Save a organisationUnit.
     *
     * @param organisationUnit the entity to save
     * @return the persisted entity
     */
    public OrganisationUnit save(OrganisationUnit organisationUnit) {
        log.debug("Request to save OrganisationUnit : {}", organisationUnit);
        OrganisationUnit result = organisationUnitRepository.save(organisationUnit);
        organisationUnitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the organisationUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrganisationUnit> findAll(Pageable pageable) {
        log.debug("Request to get all OrganisationUnits");
        return organisationUnitRepository.findAll(pageable);
    }


    /**
     * Get one organisationUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrganisationUnit> findOne(Long id) {
        log.debug("Request to get OrganisationUnit : {}", id);
        return organisationUnitRepository.findById(id);
    }

    /**
     * Delete the organisationUnit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnit : {}", id);
        organisationUnitRepository.deleteById(id);
        organisationUnitSearchRepository.deleteById(id);
    }

    /**
     * Search for the organisationUnit corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrganisationUnit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrganisationUnits for query {}", query);
        return organisationUnitSearchRepository.search(queryStringQuery(query), pageable);    }
}
