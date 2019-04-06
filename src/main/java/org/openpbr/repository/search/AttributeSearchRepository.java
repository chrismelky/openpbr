package org.openpbr.repository.search;

import org.openpbr.domain.Attribute;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Attribute entity.
 */
public interface AttributeSearchRepository extends ElasticsearchRepository<Attribute, Long> {
}
