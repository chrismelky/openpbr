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

import org.openpbr.domain.Attribute;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.AttributeRepository;
import org.openpbr.repository.search.AttributeSearchRepository;
import org.openpbr.service.dto.AttributeCriteria;

/**
 * Service for executing complex queries for Attribute entities in the database.
 * The main input is a {@link AttributeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Attribute} or a {@link Page} of {@link Attribute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeQueryService extends QueryService<Attribute> {

    private final Logger log = LoggerFactory.getLogger(AttributeQueryService.class);

    private final AttributeRepository attributeRepository;

    private final AttributeSearchRepository attributeSearchRepository;

    public AttributeQueryService(AttributeRepository attributeRepository, AttributeSearchRepository attributeSearchRepository) {
        this.attributeRepository = attributeRepository;
        this.attributeSearchRepository = attributeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Attribute> findByCriteria(AttributeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findByCriteria(AttributeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttributeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.count(specification);
    }

    /**
     * Function to convert AttributeCriteria to a {@link Specification}
     */
    private Specification<Attribute> createSpecification(AttributeCriteria criteria) {
        Specification<Attribute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Attribute_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), Attribute_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Attribute_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Attribute_.name));
            }
            if (criteria.getValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getValueType(), Attribute_.valueType));
            }
            if (criteria.getIsMandatory() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMandatory(), Attribute_.isMandatory));
            }
            if (criteria.getIsUnique() != null) {
                specification = specification.and(buildSpecification(criteria.getIsUnique(), Attribute_.isUnique));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), Attribute_.sortOrder));
            }
            if (criteria.getOptionSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getOptionSetId(),
                    root -> root.join(Attribute_.optionSet, JoinType.LEFT).get(OptionSet_.id)));
            }
        }
        return specification;
    }
}
