package org.openpbr.web.rest;
import org.openpbr.domain.PlanningUnitLevel;
import org.openpbr.service.PlanningUnitLevelService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.PlanningUnitLevelCriteria;
import org.openpbr.service.PlanningUnitLevelQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PlanningUnitLevel.
 */
@RestController
@RequestMapping("/api")
public class PlanningUnitLevelResource {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitLevelResource.class);

    private static final String ENTITY_NAME = "planningUnitLevel";

    private final PlanningUnitLevelService planningUnitLevelService;

    private final PlanningUnitLevelQueryService planningUnitLevelQueryService;

    public PlanningUnitLevelResource(PlanningUnitLevelService planningUnitLevelService, PlanningUnitLevelQueryService planningUnitLevelQueryService) {
        this.planningUnitLevelService = planningUnitLevelService;
        this.planningUnitLevelQueryService = planningUnitLevelQueryService;
    }

    /**
     * POST  /planning-unit-levels : Create a new planningUnitLevel.
     *
     * @param planningUnitLevel the planningUnitLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planningUnitLevel, or with status 400 (Bad Request) if the planningUnitLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/planning-unit-levels")
    public ResponseEntity<PlanningUnitLevel> createPlanningUnitLevel(@Valid @RequestBody PlanningUnitLevel planningUnitLevel) throws URISyntaxException {
        log.debug("REST request to save PlanningUnitLevel : {}", planningUnitLevel);
        if (planningUnitLevel.getId() != null) {
            throw new BadRequestAlertException("A new planningUnitLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanningUnitLevel result = planningUnitLevelService.save(planningUnitLevel);
        return ResponseEntity.created(new URI("/api/planning-unit-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planning-unit-levels : Updates an existing planningUnitLevel.
     *
     * @param planningUnitLevel the planningUnitLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planningUnitLevel,
     * or with status 400 (Bad Request) if the planningUnitLevel is not valid,
     * or with status 500 (Internal Server Error) if the planningUnitLevel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/planning-unit-levels")
    public ResponseEntity<PlanningUnitLevel> updatePlanningUnitLevel(@Valid @RequestBody PlanningUnitLevel planningUnitLevel) throws URISyntaxException {
        log.debug("REST request to update PlanningUnitLevel : {}", planningUnitLevel);
        if (planningUnitLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanningUnitLevel result = planningUnitLevelService.save(planningUnitLevel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planningUnitLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planning-unit-levels : get all the planningUnitLevels.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of planningUnitLevels in body
     */
    @GetMapping("/planning-unit-levels")
    public ResponseEntity<List<PlanningUnitLevel>> getAllPlanningUnitLevels(PlanningUnitLevelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlanningUnitLevels by criteria: {}", criteria);
        Page<PlanningUnitLevel> page = planningUnitLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planning-unit-levels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /planning-unit-levels/count : count all the planningUnitLevels.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/planning-unit-levels/count")
    public ResponseEntity<Long> countPlanningUnitLevels(PlanningUnitLevelCriteria criteria) {
        log.debug("REST request to count PlanningUnitLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(planningUnitLevelQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /planning-unit-levels/:id : get the "id" planningUnitLevel.
     *
     * @param id the id of the planningUnitLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planningUnitLevel, or with status 404 (Not Found)
     */
    @GetMapping("/planning-unit-levels/{id}")
    public ResponseEntity<PlanningUnitLevel> getPlanningUnitLevel(@PathVariable Long id) {
        log.debug("REST request to get PlanningUnitLevel : {}", id);
        Optional<PlanningUnitLevel> planningUnitLevel = planningUnitLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningUnitLevel);
    }

    /**
     * DELETE  /planning-unit-levels/:id : delete the "id" planningUnitLevel.
     *
     * @param id the id of the planningUnitLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/planning-unit-levels/{id}")
    public ResponseEntity<Void> deletePlanningUnitLevel(@PathVariable Long id) {
        log.debug("REST request to delete PlanningUnitLevel : {}", id);
        planningUnitLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/planning-unit-levels?query=:query : search for the planningUnitLevel corresponding
     * to the query.
     *
     * @param query the query of the planningUnitLevel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/planning-unit-levels")
    public ResponseEntity<List<PlanningUnitLevel>> searchPlanningUnitLevels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanningUnitLevels for query {}", query);
        Page<PlanningUnitLevel> page = planningUnitLevelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/planning-unit-levels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
