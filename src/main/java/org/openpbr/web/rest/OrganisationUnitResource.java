package org.openpbr.web.rest;
import org.openpbr.domain.OrganisationUnit;
import org.openpbr.service.OrganisationUnitService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.OrganisationUnitCriteria;
import org.openpbr.service.OrganisationUnitQueryService;
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
 * REST controller for managing OrganisationUnit.
 */
@RestController
@RequestMapping("/api")
public class OrganisationUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitResource.class);

    private static final String ENTITY_NAME = "organisationUnit";

    private final OrganisationUnitService organisationUnitService;

    private final OrganisationUnitQueryService organisationUnitQueryService;

    public OrganisationUnitResource(OrganisationUnitService organisationUnitService, OrganisationUnitQueryService organisationUnitQueryService) {
        this.organisationUnitService = organisationUnitService;
        this.organisationUnitQueryService = organisationUnitQueryService;
    }

    /**
     * POST  /organisation-units : Create a new organisationUnit.
     *
     * @param organisationUnit the organisationUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organisationUnit, or with status 400 (Bad Request) if the organisationUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organisation-units")
    public ResponseEntity<OrganisationUnit> createOrganisationUnit(@Valid @RequestBody OrganisationUnit organisationUnit) throws URISyntaxException {
        log.debug("REST request to save OrganisationUnit : {}", organisationUnit);
        if (organisationUnit.getId() != null) {
            throw new BadRequestAlertException("A new organisationUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity.created(new URI("/api/organisation-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organisation-units : Updates an existing organisationUnit.
     *
     * @param organisationUnit the organisationUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organisationUnit,
     * or with status 400 (Bad Request) if the organisationUnit is not valid,
     * or with status 500 (Internal Server Error) if the organisationUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organisation-units")
    public ResponseEntity<OrganisationUnit> updateOrganisationUnit(@Valid @RequestBody OrganisationUnit organisationUnit) throws URISyntaxException {
        log.debug("REST request to update OrganisationUnit : {}", organisationUnit);
        if (organisationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organisationUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organisation-units : get all the organisationUnits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of organisationUnits in body
     */
    @GetMapping("/organisation-units")
    public ResponseEntity<List<OrganisationUnit>> getAllOrganisationUnits(OrganisationUnitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganisationUnits by criteria: {}", criteria);
        Page<OrganisationUnit> page = organisationUnitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organisation-units");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /organisation-units/count : count all the organisationUnits.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/organisation-units/count")
    public ResponseEntity<Long> countOrganisationUnits(OrganisationUnitCriteria criteria) {
        log.debug("REST request to count OrganisationUnits by criteria: {}", criteria);
        return ResponseEntity.ok().body(organisationUnitQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /organisation-units/:id : get the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organisationUnit, or with status 404 (Not Found)
     */
    @GetMapping("/organisation-units/{id}")
    public ResponseEntity<OrganisationUnit> getOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to get OrganisationUnit : {}", id);
        Optional<OrganisationUnit> organisationUnit = organisationUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organisationUnit);
    }

    /**
     * DELETE  /organisation-units/:id : delete the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organisation-units/{id}")
    public ResponseEntity<Void> deleteOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to delete OrganisationUnit : {}", id);
        organisationUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/organisation-units?query=:query : search for the organisationUnit corresponding
     * to the query.
     *
     * @param query the query of the organisationUnit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/organisation-units")
    public ResponseEntity<List<OrganisationUnit>> searchOrganisationUnits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrganisationUnits for query {}", query);
        Page<OrganisationUnit> page = organisationUnitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/organisation-units");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
