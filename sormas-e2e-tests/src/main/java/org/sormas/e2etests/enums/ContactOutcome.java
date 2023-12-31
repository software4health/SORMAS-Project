/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.sormas.e2etests.enums;

import java.util.Random;
import lombok.Getter;

@Getter
public enum ContactOutcome {
  SAME_HOUSEHOLD("Live in the same household"),
  FOLLOW_UP("Under follow-up"),
  CORONAVIRUS("COVID-19"),
  UNCONFIRMED("Unconfirmed contact");

  private final String outcome;

  ContactOutcome(String outcomeCase) {
    outcome = outcomeCase;
  }

  public static String getRandomOutcome() {
    Random random = new Random();
    return String.valueOf(ContactOutcome.values()[random.nextInt(values().length)]);
  }
}
