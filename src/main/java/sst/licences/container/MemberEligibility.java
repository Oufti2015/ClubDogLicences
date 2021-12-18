package sst.licences.container;

import lombok.extern.log4j.Log4j2;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class MemberEligibility {

    private MemberEligibility() {
    }

    public static List<Membre> eligibleMembres() {
        LocalDate dateStart = dateStart();
        LocalDate dateEnd = dateEnd();
        List<Membre> collect = LicencesContainer.me().membres()
                .stream()
                .filter(m -> !m.isComite() && (m.getAffiliation() == null ||
                        (m.getAffiliation().isAfter(dateStart) && m.getAffiliation().isBefore(dateEnd))))
                .collect(Collectors.toList());
        log.debug("" + collect.size() + " membres éligibles pour email ! ");
        return collect;
    }

    private static LocalDate dateStart() {
        return LocalDate.of(refYear() - 1, Month.SEPTEMBER, 1);
    }

    private static LocalDate dateEnd() {
        return LocalDate.of(refYear(), Month.SEPTEMBER, 1);
    }

    private static int refYear() {
        LocalDate now = LocalDate.now();
        int result = now.getYear();
        if (now.isBefore(LocalDate.of(now.getYear(), Month.SEPTEMBER, 1))) {
            result -= 1;
        }
        return result;
    }

    public static List<Membre> eligibleMembres(int year) {
        LocalDate dateStart = LocalDate.of(year - 1, Month.SEPTEMBER, 1);
        return LicencesContainer.me().membres()
                .stream()
                .filter(m -> m.isComite() || (m.getAffiliation() != null && m.getAffiliation().isAfter(dateStart)))
                .collect(Collectors.toList());
    }

    public static List<Membre> unpaid(int year) {
        LocalDate dateStart = LocalDate.of(year - 1, Month.SEPTEMBER, 1);
        return LicencesContainer.me().membres()
                .stream()
                .filter(m -> !m.isComite() && (m.getAffiliation() == null || m.getAffiliation().isBefore(dateStart)))
                .collect(Collectors.toList());
    }

    public static boolean isAffiliated(Membre membre, int year) {
        LocalDate dateStart = LocalDate.of(year - 1, Month.SEPTEMBER, 1);
        return membre.isComite() || (membre.getAffiliation() != null && membre.getAffiliation().isAfter(dateStart));
    }

    public static boolean isCurrentYearAffiliated(Membre membre) {
        return isAffiliated(membre, LocalDate.now().getYear());
    }

    public static boolean isNextYearAffiliated(Membre membre) {
        return isAffiliated(membre, LocalDate.now().getYear() + 1);
    }
}
