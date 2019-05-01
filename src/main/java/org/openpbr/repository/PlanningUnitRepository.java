package org.openpbr.repository;

import org.openpbr.domain.PlanningUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlanningUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningUnitRepository extends JpaRepository<PlanningUnit, Long>, JpaSpecificationExecutor<PlanningUnit> {

}
