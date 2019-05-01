package org.openpbr.web.rest;
import org.openpbr.domain.PlanningUnit;
import org.openpbr.service.PlanningUnitService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.PlanningUnitCriteria;
import org.openpbr.service.PlanningUnitQueryService;
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
 * REST controller for managing PlanningUnit.
 */
@RestController
@RequestMapping("/api")
public class PlanningUnitResource {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitResource.class);

    private static final String ENTITY_NAME = "planningUnit";

    private final PlanningUnitService planningUnitService;

    private final PlanningUnitQueryService planningUnitQueryService;

    public PlanningUnitResource(PlanningUnitService planningUnitService, PlanningUnitQueryService planningUnitQueryService) {
        this.planningUnitService = planningUnitService;
        this.planningUnitQueryService = planningUnitQueryService;
    }

    /**
     * POST  /planning-units : Create a new planningUnit.
     *
     * @param planningUnit the planningUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planningUnit, or with status 400 (Bad Request) if the planningUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/planning-units")
    public ResponseEntity<PlanningUnit> createPlanningUnit(@Valid @RequestBody PlanningUnit planningUnit) throws URISyntaxException {
        log.debug("REST request to save PlanningUnit : {}", planningUnit);
        if (planningUnit.getId() != null) {
            throw new BadRequestAlertException("A new planningUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanningUnit result = planningUnitService.save(planningUnit);
        return ResponseEntity.created(new URI("/api/planning-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planning-units : Updates an existing planningUnit.
     *
     * @param planningUnit the planningUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planningUnit,
     * or with status 400 (Bad Request) if the planningUnit is not valid,
     * or with status 500 (Internal Server Error) if the planningUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/planning-units")
    public ResponseEntity<PlanningUnit> updatePlanningUnit(@Valid @RequestBody PlanningUnit planningUnit) throws URISyntaxException {
        log.debug("REST request to update PlanningUnit : {}", planningUnit);
        if (planningUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanningUnit result = planningUnitService.save(planningUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planningUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planning-units : get all the planningUnits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of planningUnits in body
     */
    @GetMapping("/planning-units")
    public ResponseEntity<List<PlanningUnit>> getAllPlanningUnits(PlanningUnitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlanningUnits by criteria: {}", criteria);
        Page<PlanningUnit> page = planningUnitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planning-units");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /planning-units/count : count all the planningUnits.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/planning-units/count")
    public ResponseEntity<Long> countPlanningUnits(PlanningUnitCriteria criteria) {
        log.debug("REST request to count PlanningUnits by criteria: {}", criteria);
        return ResponseEntity.ok().body(planningUnitQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /planning-units/:id : get the "id" planningUnit.
     *
     * @param id the id of the planningUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planningUnit, or with status 404 (Not Found)
     */
    @GetMapping("/planning-units/{id}")
    public ResponseEntity<PlanningUnit> getPlanningUnit(@PathVariable Long id) {
        log.debug("REST request to get PlanningUnit : {}", id);
        Optional<PlanningUnit> planningUnit = planningUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningUnit);
    }

    /**
     * DELETE  /planning-units/:id : delete the "id" planningUnit.
     *
     * @param id the id of the planningUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/planning-units/{id}")
    public ResponseEntity<Void> deletePlanningUnit(@PathVariable Long id) {
        log.debug("REST request to delete PlanningUnit : {}", id);
        planningUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/planning-units?query=:query : search for the planningUnit corresponding
     * to the query.
     *
     * @param query the query of the planningUnit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/planning-units")
    public ResponseEntity<List<PlanningUnit>> searchPlanningUnits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanningUnits for query {}", query);
        Page<PlanningUnit> page = planningUnitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/planning-units");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
