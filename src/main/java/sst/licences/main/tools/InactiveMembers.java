package sst.licences.main.tools;

import sst.licences.container.LicencesContainer;
import sst.licences.container.MemberEligibility;

public class InactiveMembers {
    public static void main(String[] args) {
        LicencesContainer.load();

        LicencesContainer.me().membres()
                .stream()
                .filter(m -> !MemberEligibility.isCurrentYearAffiliated(m))
                .forEach(System.out::println);
    }
}
