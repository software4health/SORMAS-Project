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

package de.symeda.sormas.api.docgeneneration;

import de.symeda.sormas.api.audit.AuditIncludeProperty;
import de.symeda.sormas.api.audit.AuditedClass;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AuditedClass
public class DocumentTemplateEntities implements Serializable {

	private static final long serialVersionUID = 4264467192868446193L;

	private final Map<RootEntityType, Object> entities = new HashMap<>();

	public void addEntity(RootEntityType rootEntityType, Object entity) {
		entities.put(rootEntityType, entity);
	}

	public Object getEntity(RootEntityType rootEntityType) {
		return entities.get(rootEntityType);
	}

	@AuditIncludeProperty
	public Map<RootEntityType, Object> getEntities() {
		return entities;
	}
}
