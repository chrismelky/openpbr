package org.openpbr.repository;

import org.openpbr.domain.PlanningUnitGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PlanningUnitGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningUnitGroupRepository extends JpaRepository<PlanningUnitGroup, Long>, JpaSpecificationExecutor<PlanningUnitGroup> {

    @Query(value = "select distinct planning_unit_group from PlanningUnitGroup planning_unit_group left join fetch planning_unit_group.planningUnits",
        countQuery = "select count(distinct planning_unit_group) from PlanningUnitGroup planning_unit_group")
    Page<PlanningUnitGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct planning_unit_group from PlanningUnitGroup planning_unit_group left join fetch planning_unit_group.planningUnits")
    List<PlanningUnitGroup> findAllWithEagerRelationships();

    @Query("select planning_unit_group from PlanningUnitGroup planning_unit_group left join fetch planning_unit_group.planningUnits where planning_unit_group.id =:id")
    Optional<PlanningUnitGroup> findOneWithEagerRelationships(@Param("id") Long id);

}
