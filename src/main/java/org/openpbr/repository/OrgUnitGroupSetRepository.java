package org.openpbr.repository;

import org.openpbr.domain.OrgUnitGroupSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the OrgUnitGroupSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUnitGroupSetRepository extends JpaRepository<OrgUnitGroupSet, Long>, JpaSpecificationExecutor<OrgUnitGroupSet> {

    @Query(value = "select distinct org_unit_group_set from OrgUnitGroupSet org_unit_group_set left join fetch org_unit_group_set.orgUnitGroups left join fetch org_unit_group_set.attributeValues",
        countQuery = "select count(distinct org_unit_group_set) from OrgUnitGroupSet org_unit_group_set")
    Page<OrgUnitGroupSet> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct org_unit_group_set from OrgUnitGroupSet org_unit_group_set left join fetch org_unit_group_set.orgUnitGroups left join fetch org_unit_group_set.attributeValues")
    List<OrgUnitGroupSet> findAllWithEagerRelationships();

    @Query("select org_unit_group_set from OrgUnitGroupSet org_unit_group_set left join fetch org_unit_group_set.orgUnitGroups left join fetch org_unit_group_set.attributeValues where org_unit_group_set.id =:id")
    Optional<OrgUnitGroupSet> findOneWithEagerRelationships(@Param("id") Long id);

}
