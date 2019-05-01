package org.openpbr.web.rest;
import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.service.PlanningUnitGroupService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.PlanningUnitGroupCriteria;
import org.openpbr.service.PlanningUnitGroupQueryService;
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
 * REST controller for managing PlanningUnitGroup.
 */
@RestController
@RequestMapping("/api")
public class PlanningUnitGroupResource {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupResource.class);

    private static final String ENTITY_NAME = "planningUnitGroup";

    private final PlanningUnitGroupService planningUnitGroupService;

    private final PlanningUnitGroupQueryService planningUnitGroupQueryService;

    public PlanningUnitGroupResource(PlanningUnitGroupService planningUnitGroupService, PlanningUnitGroupQueryService planningUnitGroupQueryService) {
        this.planningUnitGroupService = planningUnitGroupService;
        this.planningUnitGroupQueryService = planningUnitGroupQueryService;
    }

    /**
     * POST  /planning-unit-groups : Create a new planningUnitGroup.
     *
     * @param planningUnitGroup the planningUnitGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planningUnitGroup, or with status 400 (Bad Request) if the planningUnitGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/planning-unit-groups")
    public ResponseEntity<PlanningUnitGroup> createPlanningUnitGroup(@Valid @RequestBody PlanningUnitGroup planningUnitGroup) throws URISyntaxException {
        log.debug("REST request to save PlanningUnitGroup : {}", planningUnitGroup);
        if (planningUnitGroup.getId() != null) {
            throw new BadRequestAlertException("A new planningUnitGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanningUnitGroup result = planningUnitGroupService.save(planningUnitGroup);
        return ResponseEntity.created(new URI("/api/planning-unit-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planning-unit-groups : Updates an existing planningUnitGroup.
     *
     * @param planningUnitGroup the planningUnitGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planningUnitGroup,
     * or with status 400 (Bad Request) if the planningUnitGroup is not valid,
     * or with status 500 (Internal Server Error) if the planningUnitGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/planning-unit-groups")
    public ResponseEntity<PlanningUnitGroup> updatePlanningUnitGroup(@Valid @RequestBody PlanningUnitGroup planningUnitGroup) throws URISyntaxException {
        log.debug("REST request to update PlanningUnitGroup : {}", planningUnitGroup);
        if (planningUnitGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanningUnitGroup result = planningUnitGroupService.save(planningUnitGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planningUnitGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planning-unit-groups : get all the planningUnitGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of planningUnitGroups in body
     */
    @GetMapping("/planning-unit-groups")
    public ResponseEntity<List<PlanningUnitGroup>> getAllPlanningUnitGroups(PlanningUnitGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PlanningUnitGroups by criteria: {}", criteria);
        Page<PlanningUnitGroup> page = planningUnitGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planning-unit-groups");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /planning-unit-groups/count : count all the planningUnitGroups.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/planning-unit-groups/count")
    public ResponseEntity<Long> countPlanningUnitGroups(PlanningUnitGroupCriteria criteria) {
        log.debug("REST request to count PlanningUnitGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(planningUnitGroupQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /planning-unit-groups/:id : get the "id" planningUnitGroup.
     *
     * @param id the id of the planningUnitGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planningUnitGroup, or with status 404 (Not Found)
     */
    @GetMapping("/planning-unit-groups/{id}")
    public ResponseEntity<PlanningUnitGroup> getPlanningUnitGroup(@PathVariable Long id) {
        log.debug("REST request to get PlanningUnitGroup : {}", id);
        Optional<PlanningUnitGroup> planningUnitGroup = planningUnitGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningUnitGroup);
    }

    /**
     * DELETE  /planning-unit-groups/:id : delete the "id" planningUnitGroup.
     *
     * @param id the id of the planningUnitGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/planning-unit-groups/{id}")
    public ResponseEntity<Void> deletePlanningUnitGroup(@PathVariable Long id) {
        log.debug("REST request to delete PlanningUnitGroup : {}", id);
        planningUnitGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/planning-unit-groups?query=:query : search for the planningUnitGroup corresponding
     * to the query.
     *
     * @param query the query of the planningUnitGroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/planning-unit-groups")
    public ResponseEntity<List<PlanningUnitGroup>> searchPlanningUnitGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanningUnitGroups for query {}", query);
        Page<PlanningUnitGroup> page = planningUnitGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/planning-unit-groups");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
