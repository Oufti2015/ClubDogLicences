package sst.licences.excel;

import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.util.List;
import java.util.stream.Collectors;

public class NewMembersExporter extends ExportToExcel {
    @Override
    public String fileBaseName() {
        return LicencesConstants.NEW_MEMBRES_XLSX;
    }

    @Override
    public List<Membre> membres() {
        return LicencesContainer.me().membres()
                .stream()
                .filter(m -> (m.getLicence() == null || m.getLicence().isEmpty()) && !m.isSentToMyKKusch())
                .collect(Collectors.toList());
    }

    @Override
    public void updateMembre(Membre membre) {
        membre.setSentToMyKKusch(true);
    }

    @Override
    public boolean saveMembres() {
        return true;
    }
}
