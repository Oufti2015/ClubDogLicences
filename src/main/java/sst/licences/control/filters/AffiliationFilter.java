package sst.licences.control.filters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class AffiliationFilter {
    public static final AffiliationFilter ALL = new AffiliationFilter("Tous");
    public static final AffiliationFilter CURRENT = new AffiliationFilter(Integer.toString(LocalDate.now().getYear()));
    public static final AffiliationFilter NEXT = new AffiliationFilter(Integer.toString(LocalDate.now().getYear()+1));

    private final String libelle;

    @Override
    public String toString() {
        return libelle;
    }

    public AffiliationFilter(String libelle) {
        this.libelle = libelle;
    }

    public static ObservableList<AffiliationFilter> getList() {
        return FXCollections.observableArrayList(ALL, CURRENT, NEXT);
    }
}
