package org.openpbr.web.rest;
import org.openpbr.domain.AttributeValue;
import org.openpbr.repository.AttributeValueRepository;
import org.openpbr.repository.search.AttributeValueSearchRepository;
import org.openpbr.web.rest.errors.BadRequestAlertException;
import org.openpbr.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AttributeValue.
 */
@RestController
@RequestMapping("/api")
public class AttributeValueResource {

    private final Logger log = LoggerFactory.getLogger(AttributeValueResource.class);

    private static final String ENTITY_NAME = "attributeValue";

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueSearchRepository attributeValueSearchRepository;

    public AttributeValueResource(AttributeValueRepository attributeValueRepository, AttributeValueSearchRepository attributeValueSearchRepository) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeValueSearchRepository = attributeValueSearchRepository;
    }

    /**
     * POST  /attribute-values : Create a new attributeValue.
     *
     * @param attributeValue the attributeValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributeValue, or with status 400 (Bad Request) if the attributeValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attribute-values")
    public ResponseEntity<AttributeValue> createAttributeValue(@Valid @RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to save AttributeValue : {}", attributeValue);
        if (attributeValue.getId() != null) {
            throw new BadRequestAlertException("A new attributeValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeValue result = attributeValueRepository.save(attributeValue);
        attributeValueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attribute-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attribute-values : Updates an existing attributeValue.
     *
     * @param attributeValue the attributeValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributeValue,
     * or with status 400 (Bad Request) if the attributeValue is not valid,
     * or with status 500 (Internal Server Error) if the attributeValue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attribute-values")
    public ResponseEntity<AttributeValue> updateAttributeValue(@Valid @RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to update AttributeValue : {}", attributeValue);
        if (attributeValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttributeValue result = attributeValueRepository.save(attributeValue);
        attributeValueSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributeValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attribute-values : get all the attributeValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributeValues in body
     */
    @GetMapping("/attribute-values")
    public List<AttributeValue> getAllAttributeValues() {
        log.debug("REST request to get all AttributeValues");
        return attributeValueRepository.findAll();
    }

    /**
     * GET  /attribute-values/:id : get the "id" attributeValue.
     *
     * @param id the id of the attributeValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributeValue, or with status 404 (Not Found)
     */
    @GetMapping("/attribute-values/{id}")
    public ResponseEntity<AttributeValue> getAttributeValue(@PathVariable Long id) {
        log.debug("REST request to get AttributeValue : {}", id);
        Optional<AttributeValue> attributeValue = attributeValueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributeValue);
    }

    /**
     * DELETE  /attribute-values/:id : delete the "id" attributeValue.
     *
     * @param id the id of the attributeValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attribute-values/{id}")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable Long id) {
        log.debug("REST request to delete AttributeValue : {}", id);
        attributeValueRepository.deleteById(id);
        attributeValueSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attribute-values?query=:query : search for the attributeValue corresponding
     * to the query.
     *
     * @param query the query of the attributeValue search
     * @return the result of the search
     */
    @GetMapping("/_search/attribute-values")
    public List<AttributeValue> searchAttributeValues(@RequestParam String query) {
        log.debug("REST request to search AttributeValues for query {}", query);
        return StreamSupport
            .stream(attributeValueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
