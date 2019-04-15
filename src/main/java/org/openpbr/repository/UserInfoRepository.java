package org.openpbr.repository;

import org.openpbr.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query(value = "select distinct user_info from UserInfo user_info left join fetch user_info.attributeValues",
        countQuery = "select count(distinct user_info) from UserInfo user_info")
    Page<UserInfo> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct user_info from UserInfo user_info left join fetch user_info.attributeValues")
    List<UserInfo> findAllWithEagerRelationships();

    @Query("select user_info from UserInfo user_info left join fetch user_info.attributeValues where user_info.id =:id")
    Optional<UserInfo> findOneWithEagerRelationships(@Param("id") Long id);

}
