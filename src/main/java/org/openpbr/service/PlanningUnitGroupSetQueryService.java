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

import org.openpbr.domain.PlanningUnitGroupSet;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.PlanningUnitGroupSetRepository;
import org.openpbr.repository.search.PlanningUnitGroupSetSearchRepository;
import org.openpbr.service.dto.PlanningUnitGroupSetCriteria;

/**
 * Service for executing complex queries for PlanningUnitGroupSet entities in the database.
 * The main input is a {@link PlanningUnitGroupSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanningUnitGroupSet} or a {@link Page} of {@link PlanningUnitGroupSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanningUnitGroupSetQueryService extends QueryService<PlanningUnitGroupSet> {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupSetQueryService.class);

    private final PlanningUnitGroupSetRepository planningUnitGroupSetRepository;

    private final PlanningUnitGroupSetSearchRepository planningUnitGroupSetSearchRepository;

    public PlanningUnitGroupSetQueryService(PlanningUnitGroupSetRepository planningUnitGroupSetRepository, PlanningUnitGroupSetSearchRepository planningUnitGroupSetSearchRepository) {
        this.planningUnitGroupSetRepository = planningUnitGroupSetRepository;
        this.planningUnitGroupSetSearchRepository = planningUnitGroupSetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanningUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanningUnitGroupSet> findByCriteria(PlanningUnitGroupSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanningUnitGroupSet> specification = createSpecification(criteria);
        return planningUnitGroupSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PlanningUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroupSet> findByCriteria(PlanningUnitGroupSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanningUnitGroupSet> specification = createSpecification(criteria);
        return planningUnitGroupSetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanningUnitGroupSetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanningUnitGroupSet> specification = createSpecification(criteria);
        return planningUnitGroupSetRepository.count(specification);
    }

    /**
     * Function to convert PlanningUnitGroupSetCriteria to a {@link Specification}
     */
    private Specification<PlanningUnitGroupSet> createSpecification(PlanningUnitGroupSetCriteria criteria) {
        Specification<PlanningUnitGroupSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PlanningUnitGroupSet_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), PlanningUnitGroupSet_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PlanningUnitGroupSet_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PlanningUnitGroupSet_.name));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), PlanningUnitGroupSet_.sortOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PlanningUnitGroupSet_.isActive));
            }
            if (criteria.getPlanningUnitGroupsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanningUnitGroupsId(),
                    root -> root.join(PlanningUnitGroupSet_.planningUnitGroups, JoinType.LEFT).get(PlanningUnitGroup_.id)));
            }
        }
        return specification;
    }
}
