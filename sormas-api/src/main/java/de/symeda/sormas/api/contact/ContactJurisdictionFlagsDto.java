/*
 *  SORMAS® - Surveillance Outbreak Response Management & Analysis System
 *  Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.symeda.sormas.api.contact;

import java.io.Serializable;

public class ContactJurisdictionFlagsDto implements Serializable {

	private Boolean isInJurisdiction;
	private Boolean isCaseInJurisdiction;

	public ContactJurisdictionFlagsDto(Object[] flags) {
		this((boolean) flags[1], (boolean) flags[2]);
	}

	public ContactJurisdictionFlagsDto(Boolean isInJurisdiction, Boolean isCaseInJurisdiction) {
		this.isInJurisdiction = isInJurisdiction;
		this.isCaseInJurisdiction = isCaseInJurisdiction;
	}

	public Boolean getInJurisdiction() {
		return isInJurisdiction;
	}

	public Boolean getCaseInJurisdiction() {
		return isCaseInJurisdiction;
	}
}
