package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AttachTechId {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void main(String[] args) {
        LicencesContainer.load();
        LocalDate start = LocalDate.of(1982, Month.DECEMBER, 31);
        Map<String, String> addressMap = new HashMap<>();
        for (Membre m : LicencesContainer.me().membres()) {
            if (!m.isComite()) {
                String addressId = Membre.addressId(m);
                String bankId = addressMap.get(addressId);
                if (bankId == null) {
                    String id = start.format(formatter) + "01";
                    LicencesContainer.me().setLastBankIdentiferGenerated(id);
                    long longId = Long.parseLong(id);
                    long modulo = longId % 97;
                    bankId = String.format("%s/%s/%s%02d", id.substring(0, 3), id.substring(3, 7), id.substring(7), modulo);
                    addressMap.put(addressId, bankId);
                    start = start.plusDays(1);
                }
                log.info("%s %s %s%n", m.getPrenom(), m.getNom(), bankId);
                m.setTechnicalIdentifer(bankId);
            }
        }

        LicencesContainer.me().save();
    }
}
