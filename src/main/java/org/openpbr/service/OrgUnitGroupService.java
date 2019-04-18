package org.openpbr.service;

import org.openpbr.domain.OrgUnitGroup;
import org.openpbr.repository.OrgUnitGroupRepository;
import org.openpbr.repository.search.OrgUnitGroupSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrgUnitGroup.
 */
@Service
@Transactional
public class OrgUnitGroupService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupService.class);

    private final OrgUnitGroupRepository orgUnitGroupRepository;

    private final OrgUnitGroupSearchRepository orgUnitGroupSearchRepository;

    public OrgUnitGroupService(OrgUnitGroupRepository orgUnitGroupRepository, OrgUnitGroupSearchRepository orgUnitGroupSearchRepository) {
        this.orgUnitGroupRepository = orgUnitGroupRepository;
        this.orgUnitGroupSearchRepository = orgUnitGroupSearchRepository;
    }

    /**
     * Save a orgUnitGroup.
     *
     * @param orgUnitGroup the entity to save
     * @return the persisted entity
     */
    public OrgUnitGroup save(OrgUnitGroup orgUnitGroup) {
        log.debug("Request to save OrgUnitGroup : {}", orgUnitGroup);
        OrgUnitGroup result = orgUnitGroupRepository.save(orgUnitGroup);
        orgUnitGroupSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the orgUnitGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroup> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnitGroups");
        return orgUnitGroupRepository.findAll(pageable);
    }

    /**
     * Get all the OrgUnitGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<OrgUnitGroup> findAllWithEagerRelationships(Pageable pageable) {
        return orgUnitGroupRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one orgUnitGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrgUnitGroup> findOne(Long id) {
        log.debug("Request to get OrgUnitGroup : {}", id);
        return orgUnitGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orgUnitGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUnitGroup : {}", id);
        orgUnitGroupRepository.deleteById(id);
        orgUnitGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the orgUnitGroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroup> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgUnitGroups for query {}", query);
        return orgUnitGroupSearchRepository.search(queryStringQuery(query), pageable);    }
}
