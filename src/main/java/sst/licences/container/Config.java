package sst.licences.container;

import lombok.Data;

@Data
public class Config {
    private String lastBankIdentifierGenerated;

    public void init(Config readValue) {
        this.setLastBankIdentifierGenerated(readValue.getLastBankIdentifierGenerated());
    }
}
