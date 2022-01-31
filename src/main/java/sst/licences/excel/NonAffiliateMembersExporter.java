package sst.licences.excel;

import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.util.List;

public class NonAffiliateMembersExporter extends ExportToExcel {
    @Override
    public String fileBaseName() {
        return LicencesConstants.NON_AFFILIATE_MEMBRES_XLSX;
    }

    @Override
    public List<Membre> membres() {
        return LicencesContainer.me().unpaidMembers();
    }

    @Override
    public void updateMembre(Membre membre) {
        // No update needed
    }

    @Override
    public boolean saveMembres() {
        return false;
    }

    @Override
    public String exportName() {
        return "Membres Non Affiliés";
    }
}
