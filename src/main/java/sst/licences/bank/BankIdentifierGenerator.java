package sst.licences.bank;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class BankIdentifierGenerator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Map<String, String> addressesMap = new HashMap<>();

    private BankIdentifierGenerator() {
    }

    public static void reset(Membre membre) {
        if (addressesMap.size() == 0) {
            initAddresses();
        }
        addressesMap.remove(Membre.addressId(membre));
    }

    public static String newId(Membre membre) {
        if (addressesMap.size() == 0) {
            initAddresses();
        }
        String result = addressesMap.get(Membre.addressId(membre));
        if (result == null) {
            String lastId = LicencesContainer.me().getConfig().getLastBankIdentifierGenerated();
            int lastNumId = 0;
            String nowString = LocalDate.now().format(formatter);
            if (lastId != null && lastId.startsWith(nowString)) {
                lastNumId = Integer.parseInt(lastId.substring(8, 10));
            }

            String id = String.format("%s%02d", nowString, (lastNumId + 1));
            LicencesContainer.me().getConfig().setLastBankIdentifierGenerated(id);
            long longId = Long.parseLong(id);
            long modulo = longId % 97;
            result = String.format("%s/%s/%s%02d", id.substring(0, 3), id.substring(3, 7), id.substring(7), modulo);
            addressesMap.put(Membre.addressId(membre), result);
        }
        return result;
    }

    private static void initAddresses() {
        for (Membre membre : LicencesContainer.me().membres()) {
            addressesMap.put(Membre.addressId(membre), membre.getTechnicalIdentifier());
        }
    }

    public static String techId(Membre membre) {
        if (addressesMap.size() == 0) {
            initAddresses();
        }
        return addressesMap.get(Membre.addressId(membre));
    }

    public static boolean correctTechId(String techId) {
        return addressesMap.values().stream().filter(v -> v.equals(techId)).count() <= 1;
    }

    public static void checkTechnicalId() {
        boolean errorFound = false;
        for (Membre m : LicencesContainer.me().membres()) {
            if (m.isComite()) {
                if (m.getTechnicalIdentifier() != null) {
                    log.warn(m.fullName() + " " + m.getTechnicalIdentifier() + " est dans le comité...");
                    BankIdentifierGenerator.reset(m);
                    m.setTechnicalIdentifier(null, true);
                    errorFound = true;
                }
            } else {
                if (!BankIdentifierGenerator.correctTechId(m.getTechnicalIdentifier())) {
                    log.warn(m.fullName() + " " + m.getTechnicalIdentifier());
                    BankIdentifierGenerator.reset(m);
                    m.setTechnicalIdentifier(BankIdentifierGenerator.newId(m), true);
                    errorFound = true;
                }
            }
        }
        if (errorFound) {
            LicencesContainer.me().save();
        }
    }
}
