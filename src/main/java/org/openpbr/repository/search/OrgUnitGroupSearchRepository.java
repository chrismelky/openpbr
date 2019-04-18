package org.openpbr.repository.search;

import org.openpbr.domain.OrgUnitGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrgUnitGroup entity.
 */
public interface OrgUnitGroupSearchRepository extends ElasticsearchRepository<OrgUnitGroup, Long> {
}
