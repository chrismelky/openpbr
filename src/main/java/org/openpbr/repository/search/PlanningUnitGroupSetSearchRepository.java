package org.openpbr.repository.search;

import org.openpbr.domain.PlanningUnitGroupSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PlanningUnitGroupSet entity.
 */
public interface PlanningUnitGroupSetSearchRepository extends ElasticsearchRepository<PlanningUnitGroupSet, Long> {
}
