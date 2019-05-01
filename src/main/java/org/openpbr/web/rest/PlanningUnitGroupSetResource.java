package org.openpbr.web.rest;
import org.openpbr.domain.PlanningUnitGroupSet;
import org.openpbr.service.PlanningUnitGroupSetService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.PlanningUnitGroupSetCriteria;
import org.openpbr.service.PlanningUnitGroupSetQueryService;
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
 * REST controller for managing PlanningUnitGroupSet.
 */
@RestController
@RequestMapping("/api")
public class PlanningUnitGroupSetResource {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupSetResource.class);

    private static final String ENTITY_NAME = "planningUnitGroupSet";

    private final PlanningUnitGroupSetService planningUnitGroupSetService;

    private final PlanningUnitGroupSetQueryService planningUnitGroupSetQueryService;

    public PlanningUnitGroupSetResource(PlanningUnitGroupSetService planningUnitGroupSetService, PlanningUnitGroupSetQueryService planningUnitGroupSetQueryService) {
        this.planningUnitGroupSetService = planningUnitGroupSetService;
        this.planningUnitGroupSetQueryService = planningUnitGroupSetQueryService;
    }

    /**
     * POST  /planning-unit-group-sets : Create a new planningUnitGroupSet.
     *
     * @param planningUnitGroupSet the planningUnitGroupSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planningUnitGroupSet, or with status 400 (Bad Request) if the planningUnitGroupSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/planning-unit-group-sets")
    public ResponseEntity<PlanningUnitGroupSet> createPlanningUnitGroupSet(@Valid @RequestBody PlanningUnitGroupSet planningUnitGroupSet) throws URISyntaxException {
        log.debug("REST request to save PlanningUnitGroupSet : {}", planningUnitGroupSet);
        if (planningUnitGroupSet.getId() != null) {
            throw new BadRequestAlertException("A new planningUnitGroupSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanningUnitGroupSet result = planningUnitGroupSetService.save(planningUnitGroupSet);
        return ResponseEntity.created(new URI("/api/planning-unit-group-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planning-unit-group-sets : Updates an existing planningUnitGroupSet.
     *
     * @param planningUnitGroupSet the planningUnitGroupSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planningUnitGroupSet,
     * or with status 400 (Bad Request) if the planningUnitGroupSet is not valid,
     * or with status 500 (Internal Server Error) if the planningUnitGroupSet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/planning-unit-group-sets")
    public ResponseEntity<PlanningUnitGroupSet> updatePlanningUnitGroupSet(@Valid @RequestBody PlanningUnitGroupSet planningUnitGroupSet) throws URISyntaxException {
        log.debug("REST request to update PlanningUnitGroupSet : {}", planningUnitGroupSet);
        if (planningUnitGroupSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanningUnitGroupSet result = planningUnitGroupSetService.save(planningUnitGroupSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planningUnitGroupSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planning-unit-group-sets : get all the planningUnitGroupSets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of planningUnitGroupSets in body
     */
    @GetMapping("/planning-unit-group-sets")
    public ResponseEntity<List<PlanningUnitGroupSet>> getAllPlanningUnitGroupSets(PlanningUnitGroupSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlanningUnitGroupSets by criteria: {}", criteria);
        Page<PlanningUnitGroupSet> page = planningUnitGroupSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planning-unit-group-sets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /planning-unit-group-sets/count : count all the planningUnitGroupSets.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/planning-unit-group-sets/count")
    public ResponseEntity<Long> countPlanningUnitGroupSets(PlanningUnitGroupSetCriteria criteria) {
        log.debug("REST request to count PlanningUnitGroupSets by criteria: {}", criteria);
        return ResponseEntity.ok().body(planningUnitGroupSetQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /planning-unit-group-sets/:id : get the "id" planningUnitGroupSet.
     *
     * @param id the id of the planningUnitGroupSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planningUnitGroupSet, or with status 404 (Not Found)
     */
    @GetMapping("/planning-unit-group-sets/{id}")
    public ResponseEntity<PlanningUnitGroupSet> getPlanningUnitGroupSet(@PathVariable Long id) {
        log.debug("REST request to get PlanningUnitGroupSet : {}", id);
        Optional<PlanningUnitGroupSet> planningUnitGroupSet = planningUnitGroupSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningUnitGroupSet);
    }

    /**
     * DELETE  /planning-unit-group-sets/:id : delete the "id" planningUnitGroupSet.
     *
     * @param id the id of the planningUnitGroupSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/planning-unit-group-sets/{id}")
    public ResponseEntity<Void> deletePlanningUnitGroupSet(@PathVariable Long id) {
        log.debug("REST request to delete PlanningUnitGroupSet : {}", id);
        planningUnitGroupSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/planning-unit-group-sets?query=:query : search for the planningUnitGroupSet corresponding
     * to the query.
     *
     * @param query the query of the planningUnitGroupSet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/planning-unit-group-sets")
    public ResponseEntity<List<PlanningUnitGroupSet>> searchPlanningUnitGroupSets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanningUnitGroupSets for query {}", query);
        Page<PlanningUnitGroupSet> page = planningUnitGroupSetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/planning-unit-group-sets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
