package org.openpbr.repository.search;

import org.openpbr.domain.AttributeValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AttributeValue entity.
 */
public interface AttributeValueSearchRepository extends ElasticsearchRepository<AttributeValue, Long> {
}
