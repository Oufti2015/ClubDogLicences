package sst.licences.control.filters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum ComiteFilter {
    COMITE("Comité"),
    MEMBER("Membres"),
    ALL("Tous");

    ComiteFilter(String libelle) {
        this.libelle = libelle;
    }

    private final String libelle;

    @Override
    public String toString() {
        return libelle;
    }
    public static ObservableList<ComiteFilter> getList() {
        return FXCollections.observableArrayList(ALL, COMITE, MEMBER);
    }
}
