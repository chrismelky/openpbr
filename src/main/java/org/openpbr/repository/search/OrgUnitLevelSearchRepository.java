package org.openpbr.repository.search;

import org.openpbr.domain.OrgUnitLevel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrgUnitLevel entity.
 */
public interface OrgUnitLevelSearchRepository extends ElasticsearchRepository<OrgUnitLevel, Long> {
}
