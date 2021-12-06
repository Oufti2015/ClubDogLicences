package sst.licences.model;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class HistoryData {

    public enum ActionType {
        CREATE, UPDATE, AFFILIATION, INACTIVATION
    }

    public HistoryData() {
        this.time = LocalDateTime.now();
    }

    @Getter
    private final LocalDateTime time;
    @Getter
    private ActionType action;
    @Getter
    private String fieldName;
    @Getter
    private String initVal;
    @Getter
    private String changedVal;

    public HistoryData action(ActionType action) {
        this.action = action;
        return this;
    }

    public HistoryData fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public HistoryData initVal(String initVal) {
        this.initVal = initVal;
        return this;
    }

    public HistoryData changedVal(String changedVal) {
        this.changedVal = changedVal;
        return this;
    }
}
