package sst.licences.excel;

import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.util.List;
import java.util.stream.Collectors;

public class AllMembersExporter extends ExportToExcel {
    @Override
    public String fileBaseName() {
        return LicencesConstants.ALL_MEMBRES_XLSX;
    }

    @Override
    public List<Membre> membres() {
        return LicencesContainer.me().membres()
                .stream()
                .filter(m -> (m.getAffiliation() != null))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMembre(Membre membre) {
    }

    @Override
    public boolean saveMembres() {
        return false;
    }
}
