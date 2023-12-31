/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.campaign.data;

import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.uuid.AbstractUuidDto;

public class CampaignFormDataIndexDto extends AbstractUuidDto implements Cloneable {

	public static final String I18N_PREFIX = "CampaignFormData";
	public static final String CAMPAIGN = "campaign";
	public static final String FORM = "form";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String FORM_DATE = "formDate";

	private static final long serialVersionUID = -6672198324526771162L;

	private String campaign;
	private String form;
	private List<CampaignFormDataEntry> formValues;
	private String region;
	private String district;
	private String community;
	private Date formDate;

	public CampaignFormDataIndexDto(
		String uuid,
		String campaign,
		String form,
		Object formValues,
		String region,
		String district,
		String community,
		Date formDate) {
		super(uuid);
		this.campaign = campaign;
		this.form = form;
		this.formValues = (List<CampaignFormDataEntry>) formValues;
		this.region = region;
		this.district = district;
		this.community = community;
		this.formDate = formDate;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public List<CampaignFormDataEntry> getFormValues() {
		return formValues;
	}

	public void setFormValues(List<CampaignFormDataEntry> formValues) {
		this.formValues = formValues;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public Date getFormDate() {
		return formDate;
	}

	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}
}
