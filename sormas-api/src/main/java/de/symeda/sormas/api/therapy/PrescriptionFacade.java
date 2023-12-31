package de.symeda.sormas.api.therapy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.caze.CaseCriteria;

@Remote
public interface PrescriptionFacade {

	List<PrescriptionIndexDto> getIndexList(PrescriptionCriteria criteria);

	PrescriptionDto getPrescriptionByUuid(String uuid);

	PrescriptionDto savePrescription(@Valid PrescriptionDto prescription);

	void deletePrescription(String prescriptionUuid);

	List<PrescriptionDto> getAllActivePrescriptionsAfter(Date date);

	List<PrescriptionDto> getAllActivePrescriptionsAfter(Date date, Integer batchSize, String lastSynchronizedUuid);

	List<PrescriptionDto> getByUuids(List<String> uuids);

	List<String> getAllActiveUuids();

	List<PrescriptionExportDto> getExportList(CaseCriteria caseCriteria, Collection<String> selectedRows, int first, int max);
}
