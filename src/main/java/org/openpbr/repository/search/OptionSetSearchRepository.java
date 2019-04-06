package org.openpbr.repository.search;

import org.openpbr.domain.OptionSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OptionSet entity.
 */
public interface OptionSetSearchRepository extends ElasticsearchRepository<OptionSet, Long> {
}
