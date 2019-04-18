package org.openpbr.repository;

import org.openpbr.domain.OrganisationUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrganisationUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long>, JpaSpecificationExecutor<OrganisationUnit> {

}
