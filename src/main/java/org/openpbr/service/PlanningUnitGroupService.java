package org.openpbr.service;

import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.repository.PlanningUnitGroupRepository;
import org.openpbr.repository.search.PlanningUnitGroupSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PlanningUnitGroup.
 */
@Service
@Transactional
public class PlanningUnitGroupService {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupService.class);

    private final PlanningUnitGroupRepository planningUnitGroupRepository;

    private final PlanningUnitGroupSearchRepository planningUnitGroupSearchRepository;

    public PlanningUnitGroupService(PlanningUnitGroupRepository planningUnitGroupRepository, PlanningUnitGroupSearchRepository planningUnitGroupSearchRepository) {
        this.planningUnitGroupRepository = planningUnitGroupRepository;
        this.planningUnitGroupSearchRepository = planningUnitGroupSearchRepository;
    }

    /**
     * Save a planningUnitGroup.
     *
     * @param planningUnitGroup the entity to save
     * @return the persisted entity
     */
    public PlanningUnitGroup save(PlanningUnitGroup planningUnitGroup) {
        log.debug("Request to save PlanningUnitGroup : {}", planningUnitGroup);
        PlanningUnitGroup result = planningUnitGroupRepository.save(planningUnitGroup);
        planningUnitGroupSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the planningUnitGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroup> findAll(Pageable pageable) {
        log.debug("Request to get all PlanningUnitGroups");
        return planningUnitGroupRepository.findAll(pageable);
    }

    /**
     * Get all the PlanningUnitGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<PlanningUnitGroup> findAllWithEagerRelationships(Pageable pageable) {
        return planningUnitGroupRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one planningUnitGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PlanningUnitGroup> findOne(Long id) {
        log.debug("Request to get PlanningUnitGroup : {}", id);
        return planningUnitGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the planningUnitGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanningUnitGroup : {}", id);
        planningUnitGroupRepository.deleteById(id);
        planningUnitGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the planningUnitGroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroup> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanningUnitGroups for query {}", query);
        return planningUnitGroupSearchRepository.search(queryStringQuery(query), pageable);    }
}
