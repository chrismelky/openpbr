package org.openpbr.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrgUnitGroupSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrgUnitGroupSearchRepositoryMockConfiguration {

    @MockBean
    private OrgUnitGroupSearchRepository mockOrgUnitGroupSearchRepository;

}
