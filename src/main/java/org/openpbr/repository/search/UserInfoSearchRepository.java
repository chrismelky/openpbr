package org.openpbr.repository.search;

import org.openpbr.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserInfo entity.
 */
public interface UserInfoSearchRepository extends ElasticsearchRepository<UserInfo, Long> {
}
