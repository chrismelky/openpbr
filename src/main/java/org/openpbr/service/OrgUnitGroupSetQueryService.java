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

import org.openpbr.domain.OrgUnitGroupSet;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.OrgUnitGroupSetRepository;
import org.openpbr.repository.search.OrgUnitGroupSetSearchRepository;
import org.openpbr.service.dto.OrgUnitGroupSetCriteria;

/**
 * Service for executing complex queries for OrgUnitGroupSet entities in the database.
 * The main input is a {@link OrgUnitGroupSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUnitGroupSet} or a {@link Page} of {@link OrgUnitGroupSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUnitGroupSetQueryService extends QueryService<OrgUnitGroupSet> {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupSetQueryService.class);

    private final OrgUnitGroupSetRepository orgUnitGroupSetRepository;

    private final OrgUnitGroupSetSearchRepository orgUnitGroupSetSearchRepository;

    public OrgUnitGroupSetQueryService(OrgUnitGroupSetRepository orgUnitGroupSetRepository, OrgUnitGroupSetSearchRepository orgUnitGroupSetSearchRepository) {
        this.orgUnitGroupSetRepository = orgUnitGroupSetRepository;
        this.orgUnitGroupSetSearchRepository = orgUnitGroupSetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrgUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUnitGroupSet> findByCriteria(OrgUnitGroupSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUnitGroupSet> specification = createSpecification(criteria);
        return orgUnitGroupSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrgUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroupSet> findByCriteria(OrgUnitGroupSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUnitGroupSet> specification = createSpecification(criteria);
        return orgUnitGroupSetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrgUnitGroupSetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrgUnitGroupSet> specification = createSpecification(criteria);
        return orgUnitGroupSetRepository.count(specification);
    }

    /**
     * Function to convert OrgUnitGroupSetCriteria to a {@link Specification}
     */
    private Specification<OrgUnitGroupSet> createSpecification(OrgUnitGroupSetCriteria criteria) {
        Specification<OrgUnitGroupSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrgUnitGroupSet_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OrgUnitGroupSet_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrgUnitGroupSet_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgUnitGroupSet_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OrgUnitGroupSet_.description));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), OrgUnitGroupSet_.sortOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), OrgUnitGroupSet_.isActive));
            }
            if (criteria.getOrgUnitGroupsId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrgUnitGroupsId(),
                    root -> root.join(OrgUnitGroupSet_.orgUnitGroups, JoinType.LEFT).get(OrgUnitGroup_.id)));
            }
            if (criteria.getAttributeValuesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttributeValuesId(),
                    root -> root.join(OrgUnitGroupSet_.attributeValues, JoinType.LEFT).get(AttributeValue_.id)));
            }
        }
        return specification;
    }
}
