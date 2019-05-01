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

import org.openpbr.domain.PlanningUnit;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.PlanningUnitRepository;
import org.openpbr.repository.search.PlanningUnitSearchRepository;
import org.openpbr.service.dto.PlanningUnitCriteria;

/**
 * Service for executing complex queries for PlanningUnit entities in the database.
 * The main input is a {@link PlanningUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanningUnit} or a {@link Page} of {@link PlanningUnit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanningUnitQueryService extends QueryService<PlanningUnit> {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitQueryService.class);

    private final PlanningUnitRepository planningUnitRepository;

    private final PlanningUnitSearchRepository planningUnitSearchRepository;

    public PlanningUnitQueryService(PlanningUnitRepository planningUnitRepository, PlanningUnitSearchRepository planningUnitSearchRepository) {
        this.planningUnitRepository = planningUnitRepository;
        this.planningUnitSearchRepository = planningUnitSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanningUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanningUnit> findByCriteria(PlanningUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanningUnit> specification = createSpecification(criteria);
        return planningUnitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PlanningUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnit> findByCriteria(PlanningUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanningUnit> specification = createSpecification(criteria);
        return planningUnitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanningUnitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanningUnit> specification = createSpecification(criteria);
        return planningUnitRepository.count(specification);
    }

    /**
     * Function to convert PlanningUnitCriteria to a {@link Specification}
     */
    private Specification<PlanningUnit> createSpecification(PlanningUnitCriteria criteria) {
        Specification<PlanningUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PlanningUnit_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), PlanningUnit_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PlanningUnit_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PlanningUnit_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), PlanningUnit_.level));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), PlanningUnit_.sortOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PlanningUnit_.isActive));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(PlanningUnit_.parent, JoinType.LEFT).get(PlanningUnit_.id)));
            }
            if (criteria.getPlanningUnitGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanningUnitGroupId(),
                    root -> root.join(PlanningUnit_.planningUnitGroups, JoinType.LEFT).get(PlanningUnitGroup_.id)));
            }
        }
        return specification;
    }
}
