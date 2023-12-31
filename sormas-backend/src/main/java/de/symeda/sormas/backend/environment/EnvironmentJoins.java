package de.symeda.sormas.backend.environment;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import de.symeda.sormas.backend.common.QueryJoins;
import de.symeda.sormas.backend.environment.environmentsample.EnvironmentSample;
import de.symeda.sormas.backend.environment.environmentsample.EnvironmentSampleJoins;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.location.LocationJoins;
import de.symeda.sormas.backend.user.User;

public class EnvironmentJoins extends QueryJoins<Environment> {

	private Join<Environment, Location> location;
	private Join<Environment, User> reportingUser;
	private Join<Environment, User> responsibleUser;
	private From<?, EnvironmentSample> environmentSamples;
	private LocationJoins locationJoins;
	private EnvironmentSampleJoins environmentSampleJoins;

	public EnvironmentJoins(From<?, Environment> root) {
		super(root);
	}

	public Join<Environment, Location> getLocation() {
		return getOrCreate(location, Environment.LOCATION, JoinType.LEFT, this::setLocation);
	}

	public void setLocation(Join<Environment, Location> location) {
		this.location = location;
	}

	public LocationJoins getLocationJoins() {
		return getOrCreate(locationJoins, () -> new LocationJoins(getLocation()), this::setLocationJoins);
	}

	public void setLocationJoins(LocationJoins locationJoins) {
		this.locationJoins = locationJoins;
	}

	public Join<Location, Country> getCountry() {
		return getLocationJoins().getCountry();
	}

	public Join<Location, Region> getRegion() {
		return getLocationJoins().getRegion();
	}

	public Join<Location, District> getDistrict() {
		return getLocationJoins().getDistrict();
	}

	public Join<Location, Community> getCommunity() {
		return getLocationJoins().getCommunity();
	}

	public Join<Environment, User> getReportingUser() {
		return getOrCreate(reportingUser, Environment.REPORTING_USER, JoinType.LEFT, this::setReportingUser);
	}

	public void setReportingUser(Join<Environment, User> reportingUser) {
		this.reportingUser = reportingUser;
	}

	public Join<Environment, User> getResponsibleUser() {
		return getOrCreate(responsibleUser, Environment.RESPONSIBLE_USER, JoinType.LEFT, this::setResponsibleUser);
	}

	private void setResponsibleUser(Join<Environment, User> responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public EnvironmentSampleJoins getEnvironmentSampleJoins() {
		return getOrCreate(environmentSampleJoins, () -> new EnvironmentSampleJoins(getEnvironmentSamples()), this::setEnvironmentSampleJoins);
	}

	private void setEnvironmentSampleJoins(EnvironmentSampleJoins environmentSampleJoins) {
		this.environmentSampleJoins = environmentSampleJoins;
	}

	public From<?, EnvironmentSample> getEnvironmentSamples() {
		if (environmentSamples == null) {
			setEnvironmentSamples(getRoot().join(Environment.ENVIRONMENT_SAMPLES, JoinType.LEFT));
		}
		return environmentSamples;
	}

	private void setEnvironmentSamples(From<?, EnvironmentSample> environmentSamples) {
		this.environmentSamples = environmentSamples;
	}
}
