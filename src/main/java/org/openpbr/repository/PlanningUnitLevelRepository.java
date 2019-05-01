package org.openpbr.repository;

import org.openpbr.domain.PlanningUnitLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlanningUnitLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningUnitLevelRepository extends JpaRepository<PlanningUnitLevel, Long>, JpaSpecificationExecutor<PlanningUnitLevel> {

}
