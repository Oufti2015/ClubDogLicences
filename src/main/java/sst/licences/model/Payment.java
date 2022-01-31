package sst.licences.model;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
        return !Strings.isNullOrEmpty(valueDate)
                && !Strings.isNullOrEmpty(extractnNumber)
                && !Strings.isNullOrEmpty(paymentNumber);
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
