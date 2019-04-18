package org.openpbr.repository;

import org.openpbr.domain.OrgUnitLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrgUnitLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUnitLevelRepository extends JpaRepository<OrgUnitLevel, Long>, JpaSpecificationExecutor<OrgUnitLevel> {

}
