package org.openpbr.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.openpbr.domain.OptionSet;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.OptionSetRepository;
import org.openpbr.repository.search.OptionSetSearchRepository;
import org.openpbr.service.dto.OptionSetCriteria;

/**
 * Service for executing complex queries for OptionSet entities in the database.
 * The main input is a {@link OptionSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OptionSet} or a {@link Page} of {@link OptionSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionSetQueryService extends QueryService<OptionSet> {

    private final Logger log = LoggerFactory.getLogger(OptionSetQueryService.class);

    private final OptionSetRepository optionSetRepository;

    private final OptionSetSearchRepository optionSetSearchRepository;

    public OptionSetQueryService(OptionSetRepository optionSetRepository, OptionSetSearchRepository optionSetSearchRepository) {
        this.optionSetRepository = optionSetRepository;
        this.optionSetSearchRepository = optionSetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OptionSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OptionSet> findByCriteria(OptionSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OptionSet> specification = createSpecification(criteria);
        return optionSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OptionSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionSet> findByCriteria(OptionSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OptionSet> specification = createSpecification(criteria);
        return optionSetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionSetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OptionSet> specification = createSpecification(criteria);
        return optionSetRepository.count(specification);
    }

    /**
     * Function to convert OptionSetCriteria to a {@link Specification}
     */
    private Specification<OptionSet> createSpecification(OptionSetCriteria criteria) {
        Specification<OptionSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OptionSet_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OptionSet_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OptionSet_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OptionSet_.name));
            }
            if (criteria.getValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getValueType(), OptionSet_.valueType));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), OptionSet_.sortOrder));
            }
            if (criteria.getOptionValuesId() != null) {
                specification = specification.and(buildSpecification(criteria.getOptionValuesId(),
                    root -> root.join(OptionSet_.optionValues, JoinType.LEFT).get(OptionValue_.id)));
            }
        }
        return specification;
    }
}
