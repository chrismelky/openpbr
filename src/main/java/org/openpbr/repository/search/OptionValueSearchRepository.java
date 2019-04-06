package org.openpbr.repository.search;

import org.openpbr.domain.OptionValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OptionValue entity.
 */
public interface OptionValueSearchRepository extends ElasticsearchRepository<OptionValue, Long> {
}
