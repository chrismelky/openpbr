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

import org.openpbr.domain.OrgUnitLevel;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.OrgUnitLevelRepository;
import org.openpbr.repository.search.OrgUnitLevelSearchRepository;
import org.openpbr.service.dto.OrgUnitLevelCriteria;

/**
 * Service for executing complex queries for OrgUnitLevel entities in the database.
 * The main input is a {@link OrgUnitLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUnitLevel} or a {@link Page} of {@link OrgUnitLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUnitLevelQueryService extends QueryService<OrgUnitLevel> {

    private final Logger log = LoggerFactory.getLogger(OrgUnitLevelQueryService.class);

    private final OrgUnitLevelRepository orgUnitLevelRepository;

    private final OrgUnitLevelSearchRepository orgUnitLevelSearchRepository;

    public OrgUnitLevelQueryService(OrgUnitLevelRepository orgUnitLevelRepository, OrgUnitLevelSearchRepository orgUnitLevelSearchRepository) {
        this.orgUnitLevelRepository = orgUnitLevelRepository;
        this.orgUnitLevelSearchRepository = orgUnitLevelSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrgUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUnitLevel> findByCriteria(OrgUnitLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUnitLevel> specification = createSpecification(criteria);
        return orgUnitLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrgUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitLevel> findByCriteria(OrgUnitLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUnitLevel> specification = createSpecification(criteria);
        return orgUnitLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrgUnitLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrgUnitLevel> specification = createSpecification(criteria);
        return orgUnitLevelRepository.count(specification);
    }

    /**
     * Function to convert OrgUnitLevelCriteria to a {@link Specification}
     */
    private Specification<OrgUnitLevel> createSpecification(OrgUnitLevelCriteria criteria) {
        Specification<OrgUnitLevel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrgUnitLevel_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OrgUnitLevel_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrgUnitLevel_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgUnitLevel_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), OrgUnitLevel_.level));
            }
        }
        return specification;
    }
}
