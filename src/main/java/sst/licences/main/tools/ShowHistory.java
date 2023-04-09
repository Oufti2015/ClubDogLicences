package sst.licences.main.tools;

import com.google.common.base.MoreObjects;
import sst.licences.container.LicencesContainer;
import sst.licences.model.HistoryData;
import sst.licences.model.Membre;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class ShowHistory {
    public static void main(String[] args) {
        if (args.length == 1) {
            LicencesContainer.load();
            String techId = args[0];
            System.out.println("Searching for " + techId);
            List<Membre> collect = LicencesContainer.me().allMembers()
                    .stream()
                    .filter(m -> m.getTechnicalIdentifier() != null && m.getTechnicalIdentifier().equals(techId))
                    .collect(Collectors.toList());
            for (Membre membre : collect) {
                System.out.println("Member : " + membre);
                System.out.println("-------- ");
                List<HistoryData> history = membre.getHistory().stream().sorted((o1, o2) -> o2.getTime().compareTo(o1.getTime())).collect(Collectors.toList());
                for (HistoryData data : history) {
                    System.out.printf("%25s %-20s %-20s %-30s %-30s%n",
                            data.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                            data.getAction(),
                            MoreObjects.firstNonNull(data.getFieldName(), "---"),
                            MoreObjects.firstNonNull(data.getInitVal(), "---"),
                            MoreObjects.firstNonNull(data.getChangedVal(), "---"));
                }
            }
        }
    }
}
