package sst.licences.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;

@EqualsAndHashCode
public class Payment {

    @Setter
    @Getter
    private String valueDate;
    @Setter
    @Getter
    private String extractnNumber;
    @Setter
    @Getter
    private String paymentNumber;
    @Setter
    @Getter
    private LocalDate date;
    @Setter
    @Getter
    private String compte;
    @Setter
    @Getter
    private String nom;
    @Setter
    @Getter
    private Double montant;
    @Setter
    @Getter
    private String communications;

    public boolean isComplete() {
        return Strings.isNotEmpty(valueDate)
                && Strings.isNotEmpty(extractnNumber)
                && Strings.isNotEmpty(paymentNumber);
    }

    public String toString() {
        return String.format("%02d/%02d/%4d %13.2f EUROS %50s (%s)",
                date.getDayOfMonth(), date.getMonth().getValue(), date.getYear(),
                montant,
                nom,
                communications);
    }

    public String toFullString() {
        return String.format("%02d/%02d/%4d %13.2f EUROS %50s (%s) - %s",
                date.getDayOfMonth(), date.getMonth().getValue(), date.getYear(),
                montant,
                nom,
                communications,
                compte);
    }
}
