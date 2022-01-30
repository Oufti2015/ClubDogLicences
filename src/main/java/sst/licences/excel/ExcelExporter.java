package sst.licences.excel;

import sst.licences.model.Membre;

import java.io.IOException;
import java.util.List;

public interface ExcelExporter {

    void export() throws IOException;

    String fileBaseName();

    List<Membre> membres();

    void updateMembre(Membre membre);

    boolean saveMembres();

    String exportName();
}
