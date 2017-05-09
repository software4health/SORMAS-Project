package de.symeda.sormas.app.backend.visit;

import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.visit.VisitDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.person.Person;
import de.symeda.sormas.app.backend.person.PersonDtoHelper;
import de.symeda.sormas.app.backend.symptoms.Symptoms;
import de.symeda.sormas.app.backend.symptoms.SymptomsDtoHelper;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.user.UserDtoHelper;

public class VisitDtoHelper extends AdoDtoHelper<Visit, VisitDto> {

    private SymptomsDtoHelper symptomsDtoHelper;

    public VisitDtoHelper() {
        symptomsDtoHelper = new SymptomsDtoHelper();
    }

    @Override
    public Visit create() {
        return new Visit();
    }

    @Override
    public VisitDto createDto() {
        return new VisitDto();
    }

    @Override
    public void fillInnerFromDto(Visit target, VisitDto source) {

        target.setDisease(source.getDisease());

        target.setPerson(DatabaseHelper.getPersonDao().getByReferenceDto(source.getPerson()));

        target.setSymptoms(symptomsDtoHelper.fillOrCreateFromDto(target.getSymptoms(), source.getSymptoms()));
        target.setVisitDateTime(source.getVisitDateTime());
        target.setVisitRemarks(source.getVisitRemarks());
        target.setVisitStatus(source.getVisitStatus());
        target.setVisitUser(DatabaseHelper.getUserDao().getByReferenceDto(source.getVisitUser()));
    }

    @Override
    public void fillInnerFromAdo(VisitDto dto, Visit ado) {

        dto.setDisease(ado.getDisease());

        if (ado.getPerson() != null) {
            Person person = DatabaseHelper.getPersonDao().queryForId(ado.getPerson().getId());
            dto.setPerson(PersonDtoHelper.toReferenceDto(person));
        } else {
            dto.setPerson(null);
        }

        if (ado.getSymptoms() != null) {
            Symptoms symptoms = DatabaseHelper.getSymptomsDao().queryForId(ado.getSymptoms().getId());
            SymptomsDto symptomsDto = symptomsDtoHelper.adoToDto(symptoms);
            dto.setSymptoms(symptomsDto);
        } else {
            dto.setSymptoms(null);
        }

        dto.setVisitDateTime(ado.getVisitDateTime());
        dto.setVisitRemarks(ado.getVisitRemarks());
        dto.setVisitStatus(ado.getVisitStatus());

        if (ado.getVisitUser() != null) {
            User user = DatabaseHelper.getUserDao().queryForId(ado.getVisitUser().getId());
            dto.setVisitUser(UserDtoHelper.toReferenceDto(user));
        } else {
            dto.setVisitUser(null);
        }
    }
}
