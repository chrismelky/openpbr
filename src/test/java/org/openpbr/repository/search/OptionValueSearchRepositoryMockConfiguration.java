package org.openpbr.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OptionValueSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OptionValueSearchRepositoryMockConfiguration {

    @MockBean
    private OptionValueSearchRepository mockOptionValueSearchRepository;

}
