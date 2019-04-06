package org.openpbr.web.rest;
import org.openpbr.domain.OptionValue;
import org.openpbr.service.OptionValueService;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import org.openpbr.web.rest.util.PaginationUtil;
import org.openpbr.service.dto.OptionValueCriteria;
import org.openpbr.service.OptionValueQueryService;
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
 * REST controller for managing OptionValue.
 */
@RestController
@RequestMapping("/api")
public class OptionValueResource {

    private final Logger log = LoggerFactory.getLogger(OptionValueResource.class);

    private static final String ENTITY_NAME = "optionValue";

    private final OptionValueService optionValueService;

    private final OptionValueQueryService optionValueQueryService;

    public OptionValueResource(OptionValueService optionValueService, OptionValueQueryService optionValueQueryService) {
        this.optionValueService = optionValueService;
        this.optionValueQueryService = optionValueQueryService;
    }

    /**
     * POST  /option-values : Create a new optionValue.
     *
     * @param optionValue the optionValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new optionValue, or with status 400 (Bad Request) if the optionValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/option-values")
    public ResponseEntity<OptionValue> createOptionValue(@Valid @RequestBody OptionValue optionValue) throws URISyntaxException {
        log.debug("REST request to save OptionValue : {}", optionValue);
        if (optionValue.getId() != null) {
            throw new BadRequestAlertException("A new optionValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OptionValue result = optionValueService.save(optionValue);
        return ResponseEntity.created(new URI("/api/option-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /option-values : Updates an existing optionValue.
     *
     * @param optionValue the optionValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated optionValue,
     * or with status 400 (Bad Request) if the optionValue is not valid,
     * or with status 500 (Internal Server Error) if the optionValue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/option-values")
    public ResponseEntity<OptionValue> updateOptionValue(@Valid @RequestBody OptionValue optionValue) throws URISyntaxException {
        log.debug("REST request to update OptionValue : {}", optionValue);
        if (optionValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OptionValue result = optionValueService.save(optionValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, optionValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /option-values : get all the optionValues.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of optionValues in body
     */
    @GetMapping("/option-values")
    public ResponseEntity<List<OptionValue>> getAllOptionValues(OptionValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OptionValues by criteria: {}", criteria);
        Page<OptionValue> page = optionValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/option-values");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /option-values/count : count all the optionValues.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/option-values/count")
    public ResponseEntity<Long> countOptionValues(OptionValueCriteria criteria) {
        log.debug("REST request to count OptionValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(optionValueQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /option-values/:id : get the "id" optionValue.
     *
     * @param id the id of the optionValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the optionValue, or with status 404 (Not Found)
     */
    @GetMapping("/option-values/{id}")
    public ResponseEntity<OptionValue> getOptionValue(@PathVariable Long id) {
        log.debug("REST request to get OptionValue : {}", id);
        Optional<OptionValue> optionValue = optionValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionValue);
    }

    /**
     * DELETE  /option-values/:id : delete the "id" optionValue.
     *
     * @param id the id of the optionValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/option-values/{id}")
    public ResponseEntity<Void> deleteOptionValue(@PathVariable Long id) {
        log.debug("REST request to delete OptionValue : {}", id);
        optionValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/option-values?query=:query : search for the optionValue corresponding
     * to the query.
     *
     * @param query the query of the optionValue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/option-values")
    public ResponseEntity<List<OptionValue>> searchOptionValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OptionValues for query {}", query);
        Page<OptionValue> page = optionValueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/option-values");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
