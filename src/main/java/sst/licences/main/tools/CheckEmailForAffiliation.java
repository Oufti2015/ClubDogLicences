package sst.licences.main.tools;

import com.google.common.base.MoreObjects;
import sst.licences.container.LicencesContainer;
import sst.licences.container.MemberEligibility;
import sst.licences.model.Membre;

import java.util.List;

public class CheckEmailForAffiliation {
    public static void main(String[] args) {
        LicencesContainer.load();

        List<Membre> membres = MemberEligibility.eligibleMembresForAffiliationEmail();
        for (Membre membre : membres) {
            System.out.printf("%-30s %4d %s%n", membre.fullName(), membre.daysFromLastAffiliationEmail(), MoreObjects.firstNonNull(membre.getAffiliation(), "null"));
        }
    }
}
