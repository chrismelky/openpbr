package org.openpbr.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PlanningUnitGroupSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlanningUnitGroupSearchRepositoryMockConfiguration {

    @MockBean
    private PlanningUnitGroupSearchRepository mockPlanningUnitGroupSearchRepository;

}
