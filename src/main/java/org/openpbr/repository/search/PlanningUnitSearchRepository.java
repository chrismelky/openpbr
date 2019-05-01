package org.openpbr.repository.search;

import org.openpbr.domain.PlanningUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PlanningUnit entity.
 */
public interface PlanningUnitSearchRepository extends ElasticsearchRepository<PlanningUnit, Long> {
}
