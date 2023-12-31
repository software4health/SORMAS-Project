/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.api.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.infrastructure.continent.ContinentReferenceDto;
import de.symeda.sormas.api.location.LocationDto;

public class LocationHelperTest {

	@Test
	public void testCheckIsEmptyLocation() {

		LocationDto emptyLocation = LocationDto.build();
		assertTrue(LocationHelper.checkIsEmptyLocation(emptyLocation));

		LocationDto nonEmptyLocation = LocationDto.build();
		nonEmptyLocation.setHouseNumber("12");
		assertFalse(LocationHelper.checkIsEmptyLocation(nonEmptyLocation));

		nonEmptyLocation.setHouseNumber("");
		assertTrue(LocationHelper.checkIsEmptyLocation(nonEmptyLocation));

		nonEmptyLocation.setHouseNumber(null);
		nonEmptyLocation.setLatitude(14.2342D);
		assertFalse(LocationHelper.checkIsEmptyLocation(nonEmptyLocation));

		nonEmptyLocation.setLatitude(null);
		nonEmptyLocation.setContinent(new ContinentReferenceDto());
		assertFalse(LocationHelper.checkIsEmptyLocation(nonEmptyLocation));
	}

}
