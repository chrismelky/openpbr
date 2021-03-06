package org.openpbr.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrgUnitLevelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrgUnitLevelSearchRepositoryMockConfiguration {

    @MockBean
    private OrgUnitLevelSearchRepository mockOrgUnitLevelSearchRepository;

}
