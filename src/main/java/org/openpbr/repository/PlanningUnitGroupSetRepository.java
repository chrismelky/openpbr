package org.openpbr.repository;

import org.openpbr.domain.PlanningUnitGroupSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PlanningUnitGroupSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningUnitGroupSetRepository extends JpaRepository<PlanningUnitGroupSet, Long>, JpaSpecificationExecutor<PlanningUnitGroupSet> {

    @Query(value = "select distinct planning_unit_group_set from PlanningUnitGroupSet planning_unit_group_set left join fetch planning_unit_group_set.planningUnitGroups",
        countQuery = "select count(distinct planning_unit_group_set) from PlanningUnitGroupSet planning_unit_group_set")
    Page<PlanningUnitGroupSet> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct planning_unit_group_set from PlanningUnitGroupSet planning_unit_group_set left join fetch planning_unit_group_set.planningUnitGroups")
    List<PlanningUnitGroupSet> findAllWithEagerRelationships();

    @Query("select planning_unit_group_set from PlanningUnitGroupSet planning_unit_group_set left join fetch planning_unit_group_set.planningUnitGroups where planning_unit_group_set.id =:id")
    Optional<PlanningUnitGroupSet> findOneWithEagerRelationships(@Param("id") Long id);

}
