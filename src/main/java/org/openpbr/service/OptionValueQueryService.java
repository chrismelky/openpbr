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

import org.openpbr.domain.OptionValue;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.OptionValueRepository;
import org.openpbr.repository.search.OptionValueSearchRepository;
import org.openpbr.service.dto.OptionValueCriteria;

/**
 * Service for executing complex queries for OptionValue entities in the database.
 * The main input is a {@link OptionValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OptionValue} or a {@link Page} of {@link OptionValue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionValueQueryService extends QueryService<OptionValue> {

    private final Logger log = LoggerFactory.getLogger(OptionValueQueryService.class);

    private final OptionValueRepository optionValueRepository;

    private final OptionValueSearchRepository optionValueSearchRepository;

    public OptionValueQueryService(OptionValueRepository optionValueRepository, OptionValueSearchRepository optionValueSearchRepository) {
        this.optionValueRepository = optionValueRepository;
        this.optionValueSearchRepository = optionValueSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OptionValue} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OptionValue> findByCriteria(OptionValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OptionValue> specification = createSpecification(criteria);
        return optionValueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OptionValue} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionValue> findByCriteria(OptionValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OptionValue> specification = createSpecification(criteria);
        return optionValueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OptionValue> specification = createSpecification(criteria);
        return optionValueRepository.count(specification);
    }

    /**
     * Function to convert OptionValueCriteria to a {@link Specification}
     */
    private Specification<OptionValue> createSpecification(OptionValueCriteria criteria) {
        Specification<OptionValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OptionValue_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OptionValue_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OptionValue_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OptionValue_.name));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), OptionValue_.sortOrder));
            }
            if (criteria.getOptionSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getOptionSetId(),
                    root -> root.join(OptionValue_.optionSet, JoinType.LEFT).get(OptionSet_.id)));
            }
        }
        return specification;
    }
}
