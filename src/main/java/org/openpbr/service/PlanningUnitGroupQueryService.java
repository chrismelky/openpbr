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

import org.openpbr.domain.PlanningUnitGroup;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.PlanningUnitGroupRepository;
import org.openpbr.repository.search.PlanningUnitGroupSearchRepository;
import org.openpbr.service.dto.PlanningUnitGroupCriteria;

/**
 * Service for executing complex queries for PlanningUnitGroup entities in the database.
 * The main input is a {@link PlanningUnitGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanningUnitGroup} or a {@link Page} of {@link PlanningUnitGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanningUnitGroupQueryService extends QueryService<PlanningUnitGroup> {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitGroupQueryService.class);

    private final PlanningUnitGroupRepository planningUnitGroupRepository;

    private final PlanningUnitGroupSearchRepository planningUnitGroupSearchRepository;

    public PlanningUnitGroupQueryService(PlanningUnitGroupRepository planningUnitGroupRepository, PlanningUnitGroupSearchRepository planningUnitGroupSearchRepository) {
        this.planningUnitGroupRepository = planningUnitGroupRepository;
        this.planningUnitGroupSearchRepository = planningUnitGroupSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanningUnitGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanningUnitGroup> findByCriteria(PlanningUnitGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanningUnitGroup> specification = createSpecification(criteria);
        return planningUnitGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PlanningUnitGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitGroup> findByCriteria(PlanningUnitGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanningUnitGroup> specification = createSpecification(criteria);
        return planningUnitGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanningUnitGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanningUnitGroup> specification = createSpecification(criteria);
        return planningUnitGroupRepository.count(specification);
    }

    /**
     * Function to convert PlanningUnitGroupCriteria to a {@link Specification}
     */
    private Specification<PlanningUnitGroup> createSpecification(PlanningUnitGroupCriteria criteria) {
        Specification<PlanningUnitGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PlanningUnitGroup_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), PlanningUnitGroup_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PlanningUnitGroup_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PlanningUnitGroup_.name));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), PlanningUnitGroup_.sortOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PlanningUnitGroup_.isActive));
            }
            if (criteria.getPlanningUnitsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanningUnitsId(),
                    root -> root.join(PlanningUnitGroup_.planningUnits, JoinType.LEFT).get(PlanningUnit_.id)));
            }
            if (criteria.getPlanningUnitGroupSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanningUnitGroupSetId(),
                    root -> root.join(PlanningUnitGroup_.planningUnitGroupSets, JoinType.LEFT).get(PlanningUnitGroupSet_.id)));
            }
        }
        return specification;
    }
}
