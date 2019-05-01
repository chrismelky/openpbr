package org.openpbr.service;

import org.openpbr.domain.PlanningUnitLevel;
import org.openpbr.repository.PlanningUnitLevelRepository;
import org.openpbr.repository.search.PlanningUnitLevelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PlanningUnitLevel.
 */
@Service
@Transactional
public class PlanningUnitLevelService {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitLevelService.class);

    private final PlanningUnitLevelRepository planningUnitLevelRepository;

    private final PlanningUnitLevelSearchRepository planningUnitLevelSearchRepository;

    public PlanningUnitLevelService(PlanningUnitLevelRepository planningUnitLevelRepository, PlanningUnitLevelSearchRepository planningUnitLevelSearchRepository) {
        this.planningUnitLevelRepository = planningUnitLevelRepository;
        this.planningUnitLevelSearchRepository = planningUnitLevelSearchRepository;
    }

    /**
     * Save a planningUnitLevel.
     *
     * @param planningUnitLevel the entity to save
     * @return the persisted entity
     */
    public PlanningUnitLevel save(PlanningUnitLevel planningUnitLevel) {
        log.debug("Request to save PlanningUnitLevel : {}", planningUnitLevel);
        PlanningUnitLevel result = planningUnitLevelRepository.save(planningUnitLevel);
        planningUnitLevelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the planningUnitLevels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitLevel> findAll(Pageable pageable) {
        log.debug("Request to get all PlanningUnitLevels");
        return planningUnitLevelRepository.findAll(pageable);
    }


    /**
     * Get one planningUnitLevel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PlanningUnitLevel> findOne(Long id) {
        log.debug("Request to get PlanningUnitLevel : {}", id);
        return planningUnitLevelRepository.findById(id);
    }

    /**
     * Delete the planningUnitLevel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanningUnitLevel : {}", id);
        planningUnitLevelRepository.deleteById(id);
        planningUnitLevelSearchRepository.deleteById(id);
    }

    /**
     * Search for the planningUnitLevel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitLevel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanningUnitLevels for query {}", query);
        return planningUnitLevelSearchRepository.search(queryStringQuery(query), pageable);    }
}
