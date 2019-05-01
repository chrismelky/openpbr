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

import org.openpbr.domain.PlanningUnitLevel;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.PlanningUnitLevelRepository;
import org.openpbr.repository.search.PlanningUnitLevelSearchRepository;
import org.openpbr.service.dto.PlanningUnitLevelCriteria;

/**
 * Service for executing complex queries for PlanningUnitLevel entities in the database.
 * The main input is a {@link PlanningUnitLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanningUnitLevel} or a {@link Page} of {@link PlanningUnitLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanningUnitLevelQueryService extends QueryService<PlanningUnitLevel> {

    private final Logger log = LoggerFactory.getLogger(PlanningUnitLevelQueryService.class);

    private final PlanningUnitLevelRepository planningUnitLevelRepository;

    private final PlanningUnitLevelSearchRepository planningUnitLevelSearchRepository;

    public PlanningUnitLevelQueryService(PlanningUnitLevelRepository planningUnitLevelRepository, PlanningUnitLevelSearchRepository planningUnitLevelSearchRepository) {
        this.planningUnitLevelRepository = planningUnitLevelRepository;
        this.planningUnitLevelSearchRepository = planningUnitLevelSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanningUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanningUnitLevel> findByCriteria(PlanningUnitLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanningUnitLevel> specification = createSpecification(criteria);
        return planningUnitLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PlanningUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanningUnitLevel> findByCriteria(PlanningUnitLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanningUnitLevel> specification = createSpecification(criteria);
        return planningUnitLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanningUnitLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanningUnitLevel> specification = createSpecification(criteria);
        return planningUnitLevelRepository.count(specification);
    }

    /**
     * Function to convert PlanningUnitLevelCriteria to a {@link Specification}
     */
    private Specification<PlanningUnitLevel> createSpecification(PlanningUnitLevelCriteria criteria) {
        Specification<PlanningUnitLevel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PlanningUnitLevel_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), PlanningUnitLevel_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PlanningUnitLevel_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PlanningUnitLevel_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), PlanningUnitLevel_.level));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PlanningUnitLevel_.isActive));
            }
        }
        return specification;
    }
}
