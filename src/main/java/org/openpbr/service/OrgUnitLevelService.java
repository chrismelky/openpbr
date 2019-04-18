package org.openpbr.service;

import org.openpbr.domain.OrgUnitLevel;
import org.openpbr.repository.OrgUnitLevelRepository;
import org.openpbr.repository.search.OrgUnitLevelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrgUnitLevel.
 */
@Service
@Transactional
public class OrgUnitLevelService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitLevelService.class);

    private final OrgUnitLevelRepository orgUnitLevelRepository;

    private final OrgUnitLevelSearchRepository orgUnitLevelSearchRepository;

    public OrgUnitLevelService(OrgUnitLevelRepository orgUnitLevelRepository, OrgUnitLevelSearchRepository orgUnitLevelSearchRepository) {
        this.orgUnitLevelRepository = orgUnitLevelRepository;
        this.orgUnitLevelSearchRepository = orgUnitLevelSearchRepository;
    }

    /**
     * Save a orgUnitLevel.
     *
     * @param orgUnitLevel the entity to save
     * @return the persisted entity
     */
    public OrgUnitLevel save(OrgUnitLevel orgUnitLevel) {
        log.debug("Request to save OrgUnitLevel : {}", orgUnitLevel);
        OrgUnitLevel result = orgUnitLevelRepository.save(orgUnitLevel);
        orgUnitLevelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the orgUnitLevels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitLevel> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnitLevels");
        return orgUnitLevelRepository.findAll(pageable);
    }


    /**
     * Get one orgUnitLevel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrgUnitLevel> findOne(Long id) {
        log.debug("Request to get OrgUnitLevel : {}", id);
        return orgUnitLevelRepository.findById(id);
    }

    /**
     * Delete the orgUnitLevel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUnitLevel : {}", id);
        orgUnitLevelRepository.deleteById(id);
        orgUnitLevelSearchRepository.deleteById(id);
    }

    /**
     * Search for the orgUnitLevel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitLevel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgUnitLevels for query {}", query);
        return orgUnitLevelSearchRepository.search(queryStringQuery(query), pageable);    }
}
