/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.outbreak;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.PojoUrlParamConverter;

public class OutbreakCriteria implements Serializable {

	private static final long serialVersionUID = 326691431810294295L;

	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private Disease disease;
	private Boolean active;
	private Date changeDateAfter;
	
	public String toUrlParams() {
		return PojoUrlParamConverter.toUrlParams(this);
	}
	
	public static OutbreakCriteria fromUrlParams(String urlParams) {
		return PojoUrlParamConverter.fromUrlParams(new OutbreakCriteria(), urlParams);
	}
	
	public RegionReferenceDto getRegion() {
		return region;
	}
	public OutbreakCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}
	
	public DistrictReferenceDto getDistrict() {
		return district;
	}
	public OutbreakCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}
	
	public Disease getDisease() {
		return disease;
	}
	public OutbreakCriteria disease(Disease disease) {
		this.disease = disease;
		return this;
	}
	
	public Boolean getActive() {
		return active;
	}
	public OutbreakCriteria active(Boolean active) {
		this.active = active;
		return this;
	}
	
	public Date getChangeDateAfter() {
		return changeDateAfter;
	}
	public OutbreakCriteria changeDateAfter(Date changeDateAfter) {
		this.changeDateAfter = changeDateAfter;
		return this;
	}
}
