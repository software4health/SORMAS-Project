package de.symeda.sormas.api.utils.fieldvisibility.checkers;

import java.lang.reflect.AccessibleObject;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import de.symeda.sormas.api.feature.FeatureConfigurationDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

public class FeatureTypeFieldVisibilityChecker implements FieldVisibilityCheckers.FieldBasedChecker {

	private final List<FeatureConfigurationDto> featureConfigurations;

	public FeatureTypeFieldVisibilityChecker(List<FeatureConfigurationDto> featureConfigurations) {
		this.featureConfigurations = featureConfigurations;
	}

	@Override
	public boolean isVisible(AccessibleObject field) {
		if (field.isAnnotationPresent(DependingOnFeatureType.class)) {
			DependingOnFeatureType annotation = field.getAnnotation(DependingOnFeatureType.class);
			FeatureType[] featureTypes = annotation.featureType();

			return featureConfigurations.stream().filter(c -> ArrayUtils.contains(featureTypes, c.getFeatureType())).anyMatch(c -> {
				boolean show = !annotation.hide();
				boolean isFeatureConfigured = true;

				DependingOnFeatureType.FeatureProperty[] properties = annotation.properties();
				if (properties.length > 0) {
					// check if all properties are set as required by the annotation
					isFeatureConfigured = Stream.of(properties).allMatch(p -> {
						Object propertyValue = c.getProperties() != null ? c.getProperties().get(p.property()) : null;
						if (propertyValue == null) {
							return false;
						}

						return p.value().equals(propertyValue.toString());
					});
				}

				return !isFeatureConfigured || show;
			});
		}

		return true;
	}
}
