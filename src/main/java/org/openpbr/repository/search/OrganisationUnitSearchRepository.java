package org.openpbr.repository.search;

import org.openpbr.domain.OrganisationUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrganisationUnit entity.
 */
public interface OrganisationUnitSearchRepository extends ElasticsearchRepository<OrganisationUnit, Long> {
}
