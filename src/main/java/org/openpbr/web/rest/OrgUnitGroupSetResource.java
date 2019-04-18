package org.openpbr.web.rest;
import org.openpbr.domain.OrgUnitGroupSet;
import org.openpbr.service.OrgUnitGroupSetService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.OrgUnitGroupSetCriteria;
import org.openpbr.service.OrgUnitGroupSetQueryService;
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
 * REST controller for managing OrgUnitGroupSet.
 */
@RestController
@RequestMapping("/api")
public class OrgUnitGroupSetResource {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupSetResource.class);

    private static final String ENTITY_NAME = "orgUnitGroupSet";

    private final OrgUnitGroupSetService orgUnitGroupSetService;

    private final OrgUnitGroupSetQueryService orgUnitGroupSetQueryService;

    public OrgUnitGroupSetResource(OrgUnitGroupSetService orgUnitGroupSetService, OrgUnitGroupSetQueryService orgUnitGroupSetQueryService) {
        this.orgUnitGroupSetService = orgUnitGroupSetService;
        this.orgUnitGroupSetQueryService = orgUnitGroupSetQueryService;
    }

    /**
     * POST  /org-unit-group-sets : Create a new orgUnitGroupSet.
     *
     * @param orgUnitGroupSet the orgUnitGroupSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orgUnitGroupSet, or with status 400 (Bad Request) if the orgUnitGroupSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/org-unit-group-sets")
    public ResponseEntity<OrgUnitGroupSet> createOrgUnitGroupSet(@Valid @RequestBody OrgUnitGroupSet orgUnitGroupSet) throws URISyntaxException {
        log.debug("REST request to save OrgUnitGroupSet : {}", orgUnitGroupSet);
        if (orgUnitGroupSet.getId() != null) {
            throw new BadRequestAlertException("A new orgUnitGroupSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUnitGroupSet result = orgUnitGroupSetService.save(orgUnitGroupSet);
        return ResponseEntity.created(new URI("/api/org-unit-group-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /org-unit-group-sets : Updates an existing orgUnitGroupSet.
     *
     * @param orgUnitGroupSet the orgUnitGroupSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orgUnitGroupSet,
     * or with status 400 (Bad Request) if the orgUnitGroupSet is not valid,
     * or with status 500 (Internal Server Error) if the orgUnitGroupSet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/org-unit-group-sets")
    public ResponseEntity<OrgUnitGroupSet> updateOrgUnitGroupSet(@Valid @RequestBody OrgUnitGroupSet orgUnitGroupSet) throws URISyntaxException {
        log.debug("REST request to update OrgUnitGroupSet : {}", orgUnitGroupSet);
        if (orgUnitGroupSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrgUnitGroupSet result = orgUnitGroupSetService.save(orgUnitGroupSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orgUnitGroupSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /org-unit-group-sets : get all the orgUnitGroupSets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of orgUnitGroupSets in body
     */
    @GetMapping("/org-unit-group-sets")
    public ResponseEntity<List<OrgUnitGroupSet>> getAllOrgUnitGroupSets(OrgUnitGroupSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrgUnitGroupSets by criteria: {}", criteria);
        Page<OrgUnitGroupSet> page = orgUnitGroupSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/org-unit-group-sets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /org-unit-group-sets/count : count all the orgUnitGroupSets.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/org-unit-group-sets/count")
    public ResponseEntity<Long> countOrgUnitGroupSets(OrgUnitGroupSetCriteria criteria) {
        log.debug("REST request to count OrgUnitGroupSets by criteria: {}", criteria);
        return ResponseEntity.ok().body(orgUnitGroupSetQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /org-unit-group-sets/:id : get the "id" orgUnitGroupSet.
     *
     * @param id the id of the orgUnitGroupSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orgUnitGroupSet, or with status 404 (Not Found)
     */
    @GetMapping("/org-unit-group-sets/{id}")
    public ResponseEntity<OrgUnitGroupSet> getOrgUnitGroupSet(@PathVariable Long id) {
        log.debug("REST request to get OrgUnitGroupSet : {}", id);
        Optional<OrgUnitGroupSet> orgUnitGroupSet = orgUnitGroupSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUnitGroupSet);
    }

    /**
     * DELETE  /org-unit-group-sets/:id : delete the "id" orgUnitGroupSet.
     *
     * @param id the id of the orgUnitGroupSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/org-unit-group-sets/{id}")
    public ResponseEntity<Void> deleteOrgUnitGroupSet(@PathVariable Long id) {
        log.debug("REST request to delete OrgUnitGroupSet : {}", id);
        orgUnitGroupSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/org-unit-group-sets?query=:query : search for the orgUnitGroupSet corresponding
     * to the query.
     *
     * @param query the query of the orgUnitGroupSet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/org-unit-group-sets")
    public ResponseEntity<List<OrgUnitGroupSet>> searchOrgUnitGroupSets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrgUnitGroupSets for query {}", query);
        Page<OrgUnitGroupSet> page = orgUnitGroupSetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/org-unit-group-sets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
