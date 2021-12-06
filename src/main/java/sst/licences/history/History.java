package sst.licences.history;

import com.google.common.base.MoreObjects;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Email;
import sst.licences.model.HistoryData;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class History {
    private History() {
    }

    public static boolean historicalDataCutover = false;

    public static void history(Membre membre, String field, String initValue, String changedValue) {
        initValue = MoreObjects.firstNonNull(initValue, "");
        changedValue = MoreObjects.firstNonNull(changedValue, "");
        if (!initValue.equals(changedValue)) {
            HistoryData.ActionType actionType = (LicencesContainer.me().allMembers().contains(membre)) ? HistoryData.ActionType.UPDATE : HistoryData.ActionType.CREATE;
            if (historicalDataCutover) {
                actionType = HistoryData.ActionType.CREATE;
            }

            HistoryData historyData = new HistoryData()
                    .action(actionType)
                    .fieldName(field)
                    .initVal(initValue)
                    .changedVal(changedValue);

            membre.history(historyData);
        }
    }

    public static void history(Membre membre, String field, boolean initValue, boolean changedValue) {
        history(membre, field, yesNo(initValue), yesNo(changedValue));
    }

    public static void history(Membre membre, String field, LocalDate initValue, LocalDate changedValue) {
        String init = initValue != null ? initValue.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) : "";
        String changed = changedValue != null ? changedValue.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) : "";
        history(membre, field, init, changed);
    }

    public static void history(Membre membre, String field, Email initValue, Email changedValue) {
        String init = initValue != null ? initValue.getAdresse() : "";
        String changed = changedValue != null ? changedValue.getAdresse() : "";
        history(membre, field, init, changed);
    }

    public static void affiliation(Membre membre, LocalDate initValue, LocalDate changedValue) {
        String init = initValue != null ? initValue.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) : "";
        String changed = changedValue != null ? changedValue.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) : "";
        if (!init.equals(changed)) {
            HistoryData historyData = new HistoryData()
                    .action(HistoryData.ActionType.AFFILIATION)
                    .fieldName(Membre.FIELD_AFFILIATION)
                    .initVal(init)
                    .changedVal(changed);
            membre.history(historyData);
        }
    }

    public static void inactivation(Membre membre) {
        HistoryData historyData = new HistoryData()
                .action(HistoryData.ActionType.INACTIVATION)
                .fieldName(Membre.FIELD_ACTIVE_FLAG)
                .initVal(yesNo(true))
                .changedVal(yesNo(false));
        membre.history(historyData);
    }

    private static String yesNo(@NotNull @NotEmpty boolean value) {
        return value ? "Yes" : "No";
    }
}
