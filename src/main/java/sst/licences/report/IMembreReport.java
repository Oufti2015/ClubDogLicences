package sst.licences.report;

import sst.licences.model.Membre;

import java.util.List;

public interface IMembreReport {
    IMembreReport input(List<Membre> input);

    IMembreReport format();

    String output();
}
