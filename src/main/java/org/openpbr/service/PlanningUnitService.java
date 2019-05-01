package org.openpbr.service;

import org.openpbr.domain.PlanningUnit;
import org.openpbr.repository.PlanningUnitRepository;
import org.openpbr.repository.search.PlanningUnitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PlanningUnit.
 */
@Service
@Transactional
public class PlanningUnitService {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitService.class);

    private final PlanningUnitRepository planningUnitRepository;

    private final PlanningUnitSearchRepository planningUnitSearchRepository;

    public PlanningUnitService(PlanningUnitRepository planningUnitRepository, PlanningUnitSearchRepository planningUnitSearchRepository) {
        this.planningUnitRepository = planningUnitRepository;
        this.planningUnitSearchRepository = planningUnitSearchRepository;
    }

    /**
     * Save a planningUnit.
     *
     * @param planningUnit the entity to save
     * @return the persisted entity
     */
    public PlanningUnit save(PlanningUnit planningUnit) {
        log.debug("Request to save PlanningUnit : {}", planningUnit);
        PlanningUnit result = planningUnitRepository.save(planningUnit);
        planningUnitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the planningUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnit> findAll(Pageable pageable) {
        log.debug("Request to get all PlanningUnits");
        return planningUnitRepository.findAll(pageable);
    }


    /**
     * Get one planningUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PlanningUnit> findOne(Long id) {
        log.debug("Request to get PlanningUnit : {}", id);
        return planningUnitRepository.findById(id);
    }

    /**
     * Delete the planningUnit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanningUnit : {}", id);
        planningUnitRepository.deleteById(id);
        planningUnitSearchRepository.deleteById(id);
    }

    /**
     * Search for the planningUnit corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanningUnits for query {}", query);
        return planningUnitSearchRepository.search(queryStringQuery(query), pageable);    }
}
