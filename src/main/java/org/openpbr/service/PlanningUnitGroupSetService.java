package org.openpbr.service;

import org.openpbr.domain.PlanningUnitGroupSet;
import org.openpbr.repository.PlanningUnitGroupSetRepository;
import org.openpbr.repository.search.PlanningUnitGroupSetSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PlanningUnitGroupSet.
 */
@Service
@Transactional
public class PlanningUnitGroupSetService {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupSetService.class);

    private final PlanningUnitGroupSetRepository planningUnitGroupSetRepository;

    private final PlanningUnitGroupSetSearchRepository planningUnitGroupSetSearchRepository;

    public PlanningUnitGroupSetService(PlanningUnitGroupSetRepository planningUnitGroupSetRepository, PlanningUnitGroupSetSearchRepository planningUnitGroupSetSearchRepository) {
        this.planningUnitGroupSetRepository = planningUnitGroupSetRepository;
        this.planningUnitGroupSetSearchRepository = planningUnitGroupSetSearchRepository;
    }

    /**
     * Save a planningUnitGroupSet.
     *
     * @param planningUnitGroupSet the entity to save
     * @return the persisted entity
     */
    public PlanningUnitGroupSet save(PlanningUnitGroupSet planningUnitGroupSet) {
        log.debug("Request to save PlanningUnitGroupSet : {}", planningUnitGroupSet);
        PlanningUnitGroupSet result = planningUnitGroupSetRepository.save(planningUnitGroupSet);
        planningUnitGroupSetSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the planningUnitGroupSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroupSet> findAll(Pageable pageable) {
        log.debug("Request to get all PlanningUnitGroupSets");
        return planningUnitGroupSetRepository.findAll(pageable);
    }

    /**
     * Get all the PlanningUnitGroupSet with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<PlanningUnitGroupSet> findAllWithEagerRelationships(Pageable pageable) {
        return planningUnitGroupSetRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one planningUnitGroupSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PlanningUnitGroupSet> findOne(Long id) {
        log.debug("Request to get PlanningUnitGroupSet : {}", id);
        return planningUnitGroupSetRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the planningUnitGroupSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanningUnitGroupSet : {}", id);
        planningUnitGroupSetRepository.deleteById(id);
        planningUnitGroupSetSearchRepository.deleteById(id);
    }

    /**
     * Search for the planningUnitGroupSet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroupSet> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanningUnitGroupSets for query {}", query);
        return planningUnitGroupSetSearchRepository.search(queryStringQuery(query), pageable);    }
}
