package sst.licences.bank;

import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BankIdentifierGenerator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Map<String, String> addressesMap = new HashMap<>();

    private BankIdentifierGenerator() {

    }

    public static String newId(Membre membre) {
        if (addressesMap.size() == 0) {
            initAddresses();
        }
        String result = addressesMap.get(Membre.addressId(membre));
        if (result == null) {
            String lastId = LicencesContainer.me().getLastBankIdentiferGenerated();
            int lastNumId = 0;
            String nowString = LocalDate.now().format(formatter);
            if (lastId != null && lastId.startsWith(nowString)) {
                lastNumId = Integer.parseInt(lastId.substring(8, 10));
            }

            String id = String.format("%s%02d", nowString, (lastNumId + 1));
            LicencesContainer.me().setLastBankIdentiferGenerated(id);
            long longId = Long.parseLong(id);
            long modulo = longId % 97;
            result = String.format("%s/%s/%s%02d", id.substring(0, 3), id.substring(3, 7), id.substring(7), modulo);
            addressesMap.put(Membre.addressId(membre), result);
        }
        return result;
    }

    private static void initAddresses() {
        for (Membre membre : LicencesContainer.me().membres()) {
            addressesMap.put(Membre.addressId(membre), membre.getTechnicalIdentifer());
        }
    }

}
