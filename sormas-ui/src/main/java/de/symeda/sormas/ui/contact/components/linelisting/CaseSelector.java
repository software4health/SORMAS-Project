package de.symeda.sormas.ui.contact.components.linelisting;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.contact.AdoptAddressLayout;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class CaseSelector extends CustomField<CaseReferenceDto> {

	private final CaseReferenceDto initialCaseValue;

	private final Label caseInfoLabel;
	private CaseReferenceDto selectedCaseValue;
	private AdoptAddressLayout adoptAddressLayout;

	public CaseSelector(String caseInfoText) {
		caseInfoLabel = new Label(caseInfoText, ContentMode.HTML);
		initialCaseValue = null;
	}

	public CaseSelector(CaseReferenceDto selectedCaseValue) {
		caseInfoLabel = new Label("", ContentMode.HTML);
		initialCaseValue = selectedCaseValue;
	}

	@Override
	protected Component initContent() {
		HorizontalLayout layout = new HorizontalLayout();
		VerticalLayout mainLayout = new VerticalLayout(layout);
		mainLayout.setSpacing(false);

		if (initialCaseValue != null) {
			setValue(initialCaseValue);
			displayLabelForCase(initialCaseValue);
			layout.addComponent(caseInfoLabel);
			adoptAddressLayout = new AdoptAddressLayout(initialCaseValue);
			adoptAddressLayout.setAdoptAddress(true);
			mainLayout.addComponent(adoptAddressLayout);
		} else {
			Button chooseCaseButton = ButtonHelper.createButton(Captions.contactChooseCase, null, ValoTheme.BUTTON_PRIMARY, CssStyles.VSPACE_2);
			Button removeCaseButton = ButtonHelper.createButton(Captions.contactRemoveCase, null, ValoTheme.BUTTON_LINK);

			CssStyles.style(caseInfoLabel, CssStyles.VSPACE_TOP_4);
			layout.addComponent(caseInfoLabel);

			chooseCaseButton.addClickListener(e -> ControllerProvider.getContactController().openSelectCaseForContactWindow(null, selectedCase -> {
				if (selectedCase != null) {
					CaseReferenceDto selectedCaseReference = selectedCase.toReference();
					setValue(selectedCaseReference);
					displayLabelForCase(selectedCaseReference);
					removeCaseButton.setVisible(true);
					chooseCaseButton.setCaption(I18nProperties.getCaption(Captions.contactChangeCase));
					if (adoptAddressLayout == null) {
						adoptAddressLayout = new AdoptAddressLayout(selectedCase.toReference());
						mainLayout.addComponent(adoptAddressLayout);
					} else {
						adoptAddressLayout.setCaseReference(selectedCase.toReference());
					}
					adoptAddressLayout.setVisible(true);
					adoptAddressLayout.setAdoptAddress(true);
				} else {
					if (adoptAddressLayout != null) {
						adoptAddressLayout.setVisible(false);
						adoptAddressLayout.setAdoptAddress(false);
					}
				}
			}));
			layout.addComponent(chooseCaseButton);

			removeCaseButton.addClickListener(e -> {
				setValue(null);
				caseInfoLabel.setValue(I18nProperties.getString(Strings.infoNoSourceCaseSelected));
				caseInfoLabel.addStyleName(CssStyles.VSPACE_TOP_4);
				removeCaseButton.setVisible(false);
				chooseCaseButton.setCaption(I18nProperties.getCaption(Captions.contactChooseCase));
				if (adoptAddressLayout != null) {
					adoptAddressLayout.setVisible(false);
					adoptAddressLayout.setAdoptAddress(false);
				}
			});
			layout.addComponent(removeCaseButton);
			removeCaseButton.setVisible(false);
		}

		return mainLayout;
	}

	@Override
	protected void doSetValue(CaseReferenceDto caseReferenceDto) {
		this.selectedCaseValue = caseReferenceDto;

	}

	@Override
	public CaseReferenceDto getValue() {
		return selectedCaseValue;
	}

	private void displayLabelForCase(CaseReferenceDto selectedCase) {
		caseInfoLabel.setValue(
			String.format(
				I18nProperties.getString(Strings.infoContactCreationSourceCase),
				selectedCase.getFirstName() + " " + selectedCase.getLastName() + " " + "(" + DataHelper.getShortUuid(selectedCase.getUuid()) + ")"));
		caseInfoLabel.removeStyleName(CssStyles.VSPACE_TOP_4);
	}

	public AdoptAddressLayout getAdoptAddressLayout() {
		return adoptAddressLayout;
	}
}
