package sst.licences.report;

import sst.licences.model.Membre;

import java.util.List;

public interface Report {
    Report input(List<Membre> input);
    Report format();
    String output();
}
