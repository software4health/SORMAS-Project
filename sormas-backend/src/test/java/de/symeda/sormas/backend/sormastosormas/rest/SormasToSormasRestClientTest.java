package de.symeda.sormas.backend.sormastosormas.rest;

import static de.symeda.sormas.backend.sormastosormas.rest.SormasToSormasRestClient.SORMAS_REST_URL_TEMPLATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import org.junit.jupiter.api.Test;

public class SormasToSormasRestClientTest {

	@Test
	public void testHttpsRestUrl() {
		assertThat(SORMAS_REST_URL_TEMPLATE, startsWith("https://"));
	}

}
