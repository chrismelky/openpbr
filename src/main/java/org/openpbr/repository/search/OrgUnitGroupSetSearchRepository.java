package org.openpbr.repository.search;

import org.openpbr.domain.OrgUnitGroupSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrgUnitGroupSet entity.
 */
public interface OrgUnitGroupSetSearchRepository extends ElasticsearchRepository<OrgUnitGroupSet, Long> {
}
