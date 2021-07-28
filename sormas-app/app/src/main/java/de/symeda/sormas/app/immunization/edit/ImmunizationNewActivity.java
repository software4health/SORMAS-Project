/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.app.immunization.edit;

import android.content.Context;
import android.os.AsyncTask;

import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.immunization.Immunization;
import de.symeda.sormas.app.backend.person.Person;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.immunization.ImmunizationPickOrCreateDialog;
import de.symeda.sormas.app.immunization.ImmunizationSection;
import de.symeda.sormas.app.person.SelectOrCreatePersonDialog;
import de.symeda.sormas.app.util.Bundler;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

public class ImmunizationNewActivity extends BaseEditActivity<Immunization> {

	private AsyncTask saveTask;

	public static void startActivity(Context context) {
		BaseEditActivity.startActivity(context, ImmunizationNewActivity.class, buildBundle());
	}

	public static Bundler buildBundle() {
		return BaseEditActivity.buildBundle(null);
	}

	@Override
	protected Immunization queryRootEntity(String recordUuid) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Immunization buildRootEntity() {
		final Person person = DatabaseHelper.getPersonDao().build();
		final Immunization immunization = DatabaseHelper.getImmunizationDao().build(person);

		return immunization;
	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, Immunization activityRootData) {
		BaseEditFragment fragment;
		fragment = ImmunizationNewFragment.newInstance(activityRootData);
		fragment.setLiveValidationDisabled(true);
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_immunization_new;
	}

	@Override
	public void replaceFragment(BaseEditFragment f, boolean allowBackNavigation) {
		super.replaceFragment(f, allowBackNavigation);
		getActiveFragment().setLiveValidationDisabled(true);
	}

	@Override
	public void saveData() {
		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final Immunization immunization = getStoredRootEntity();
		ImmunizationNewFragment fragment = (ImmunizationNewFragment) getActiveFragment();
		fragment.setLiveValidationDisabled(false);

		try {
			FragmentValidator.validate(getContext(), fragment.getContentBinding());
		} catch (ValidationException e) {
			NotificationHelper.showNotification(this, ERROR, e.getMessage());
			return;
		}

		SelectOrCreatePersonDialog.selectOrCreatePerson(immunization.getPerson(), person -> {
			if (person != null) {
				immunization.setPerson(person);
				pickOrCreateImmunizationAndSave(immunization, fragment);
			}
		});
	}

	private void pickOrCreateImmunizationAndSave(Immunization immunization, ImmunizationNewFragment fragment) {
		ImmunizationPickOrCreateDialog.pickOrCreateImmunization(immunization, pickedImmunization -> {
			if (pickedImmunization.getUuid().equals(immunization.getUuid())) {

				if (saveTask != null) {
					NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
					return; // don't save multiple times
				}
				saveTask = new SavingAsyncTask(getRootView(), immunization) {

					@Override
					protected void onPreExecute() {
						showPreloader();
					}

					@Override
					protected void doInBackground(TaskResultHolder resultHolder) throws Exception {
						DatabaseHelper.getPersonDao().saveAndSnapshot(immunization.getPerson());
						DatabaseHelper.getImmunizationDao().saveAndSnapshot(immunization);
					}

					@Override
					protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
						hidePreloader();
						if (taskResult.getResultStatus().isSuccess()) {
							finish();
							ImmunizationEditActivity.startActivity(getContext(), immunization.getUuid(), ImmunizationSection.IMMUNIZATION_INFO);
						}

						// do after clearing, because we want to show a success notification that would otherwise be hidden immediately
						super.onPostExecute(taskResult);

						saveTask = null;
					}
				}.executeOnThreadPool();
			} else {
				finish();
				ImmunizationEditActivity.startActivity(getContext(), pickedImmunization.getUuid(), ImmunizationSection.IMMUNIZATION_INFO);
			}
		});
	}

	@Override
	public Enum getPageStatus() {
		return null;
	}

}
