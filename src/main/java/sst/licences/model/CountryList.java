package sst.licences.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class CountryList {
    public static Country belgium = new Country("B", "Belgique");
    private static Country france = new Country("FR", "France");
    private static Country luxembourg = new Country("LUX", "Luxembourg");

    public static ObservableList<Country> getCountryList() {
        return FXCollections.observableArrayList(belgium, france, luxembourg);
    }

    public static Optional<Country> country(String code) {
        return getCountryList().stream().filter(c->c.getCode().equals(code)).findFirst();
    }
}
