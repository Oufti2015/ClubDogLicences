package sst.licences.main.tools;

import com.google.common.base.Strings;
import sst.licences.container.LicencesContainer;
import sst.licences.container.MemberEligibility;

public class BlankEmailMembers {
    public static void main(String[] args) {
        LicencesContainer.load();

        System.out.println("Start.");

        LicencesContainer.me().membres()
                .stream()
                .filter(MemberEligibility::isCurrentYearAffiliated)
                .filter(m -> m.getEmail() == null || Strings.isNullOrEmpty(m.getEmail().getAdresse()))
                .forEach(System.out::println);

        System.out.println("End.");
    }
}
