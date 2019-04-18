package org.openpbr.repository;

import org.openpbr.domain.OrgUnitGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the OrgUnitGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUnitGroupRepository extends JpaRepository<OrgUnitGroup, Long>, JpaSpecificationExecutor<OrgUnitGroup> {

    @Query(value = "select distinct org_unit_group from OrgUnitGroup org_unit_group left join fetch org_unit_group.organisationUnits",
        countQuery = "select count(distinct org_unit_group) from OrgUnitGroup org_unit_group")
    Page<OrgUnitGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct org_unit_group from OrgUnitGroup org_unit_group left join fetch org_unit_group.organisationUnits")
    List<OrgUnitGroup> findAllWithEagerRelationships();

    @Query("select org_unit_group from OrgUnitGroup org_unit_group left join fetch org_unit_group.organisationUnits where org_unit_group.id =:id")
    Optional<OrgUnitGroup> findOneWithEagerRelationships(@Param("id") Long id);

}
