package org.openpbr.repository;

import org.openpbr.domain.OptionSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OptionSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionSetRepository extends JpaRepository<OptionSet, Long>, JpaSpecificationExecutor<OptionSet> {

}
