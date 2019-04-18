package org.openpbr.service;

import org.openpbr.domain.OrgUnitGroupSet;
import org.openpbr.repository.OrgUnitGroupSetRepository;
import org.openpbr.repository.search.OrgUnitGroupSetSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrgUnitGroupSet.
 */
@Service
@Transactional
public class OrgUnitGroupSetService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupSetService.class);

    private final OrgUnitGroupSetRepository orgUnitGroupSetRepository;

    private final OrgUnitGroupSetSearchRepository orgUnitGroupSetSearchRepository;

    public OrgUnitGroupSetService(OrgUnitGroupSetRepository orgUnitGroupSetRepository, OrgUnitGroupSetSearchRepository orgUnitGroupSetSearchRepository) {
        this.orgUnitGroupSetRepository = orgUnitGroupSetRepository;
        this.orgUnitGroupSetSearchRepository = orgUnitGroupSetSearchRepository;
    }

    /**
     * Save a orgUnitGroupSet.
     *
     * @param orgUnitGroupSet the entity to save
     * @return the persisted entity
     */
    public OrgUnitGroupSet save(OrgUnitGroupSet orgUnitGroupSet) {
        log.debug("Request to save OrgUnitGroupSet : {}", orgUnitGroupSet);
        OrgUnitGroupSet result = orgUnitGroupSetRepository.save(orgUnitGroupSet);
        orgUnitGroupSetSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the orgUnitGroupSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroupSet> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnitGroupSets");
        return orgUnitGroupSetRepository.findAll(pageable);
    }

    /**
     * Get all the OrgUnitGroupSet with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<OrgUnitGroupSet> findAllWithEagerRelationships(Pageable pageable) {
        return orgUnitGroupSetRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one orgUnitGroupSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrgUnitGroupSet> findOne(Long id) {
        log.debug("Request to get OrgUnitGroupSet : {}", id);
        return orgUnitGroupSetRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orgUnitGroupSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUnitGroupSet : {}", id);
        orgUnitGroupSetRepository.deleteById(id);
        orgUnitGroupSetSearchRepository.deleteById(id);
    }

    /**
     * Search for the orgUnitGroupSet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroupSet> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgUnitGroupSets for query {}", query);
        return orgUnitGroupSetSearchRepository.search(queryStringQuery(query), pageable);    }
}
