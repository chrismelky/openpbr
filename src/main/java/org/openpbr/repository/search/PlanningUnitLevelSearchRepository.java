package org.openpbr.repository.search;

import org.openpbr.domain.PlanningUnitLevel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PlanningUnitLevel entity.
 */
public interface PlanningUnitLevelSearchRepository extends ElasticsearchRepository<PlanningUnitLevel, Long> {
}
