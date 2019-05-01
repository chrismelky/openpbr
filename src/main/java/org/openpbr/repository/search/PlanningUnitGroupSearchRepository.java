package org.openpbr.repository.search;

import org.openpbr.domain.PlanningUnitGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PlanningUnitGroup entity.
 */
public interface PlanningUnitGroupSearchRepository extends ElasticsearchRepository<PlanningUnitGroup, Long> {
}
