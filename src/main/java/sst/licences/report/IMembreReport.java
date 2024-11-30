package sst.licences.report;

import sst.licences.model.Membre;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IMembreReport {
    IMembreReport input(List<Membre> input);

    IMembreReport format() throws IOException;

    String output();
}
