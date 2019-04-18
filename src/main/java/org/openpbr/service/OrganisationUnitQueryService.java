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

import org.openpbr.domain.OrganisationUnit;
import org.openpbr.domain.*; // for static metamodels
import org.openpbr.repository.OrganisationUnitRepository;
import org.openpbr.repository.search.OrganisationUnitSearchRepository;
import org.openpbr.service.dto.OrganisationUnitCriteria;

/**
 * Service for executing complex queries for OrganisationUnit entities in the database.
 * The main input is a {@link OrganisationUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganisationUnit} or a {@link Page} of {@link OrganisationUnit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganisationUnitQueryService extends QueryService<OrganisationUnit> {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitQueryService.class);

    private final OrganisationUnitRepository organisationUnitRepository;

    private final OrganisationUnitSearchRepository organisationUnitSearchRepository;

    public OrganisationUnitQueryService(OrganisationUnitRepository organisationUnitRepository, OrganisationUnitSearchRepository organisationUnitSearchRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
        this.organisationUnitSearchRepository = organisationUnitSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrganisationUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationUnit> findByCriteria(OrganisationUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganisationUnit> specification = createSpecification(criteria);
        return organisationUnitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganisationUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganisationUnit> findByCriteria(OrganisationUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganisationUnit> specification = createSpecification(criteria);
        return organisationUnitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganisationUnitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganisationUnit> specification = createSpecification(criteria);
        return organisationUnitRepository.count(specification);
    }

    /**
     * Function to convert OrganisationUnitCriteria to a {@link Specification}
     */
    private Specification<OrganisationUnit> createSpecification(OrganisationUnitCriteria criteria) {
        Specification<OrganisationUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrganisationUnit_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OrganisationUnit_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrganisationUnit_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrganisationUnit_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), OrganisationUnit_.level));
            }
            if (criteria.getOpeningDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpeningDate(), OrganisationUnit_.openingDate));
            }
            if (criteria.getClosedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedDate(), OrganisationUnit_.closedDate));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), OrganisationUnit_.url));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), OrganisationUnit_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), OrganisationUnit_.longitude));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), OrganisationUnit_.address));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), OrganisationUnit_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), OrganisationUnit_.phoneNumber));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), OrganisationUnit_.sortOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), OrganisationUnit_.isActive));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(OrganisationUnit_.parent, JoinType.LEFT).get(OrganisationUnit_.id)));
            }
        }
        return specification;
    }
}
