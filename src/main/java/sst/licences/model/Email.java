package sst.licences.model;

import lombok.Data;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

@Data
public class Email {
    @NotNull
    @NotEmpty
    private String adresse;
    @NotNull
    @NotEmpty
    private Boolean ok;

    public Email(String adresse) {
        this.adresse = adresse;
        this.ok = true;
    }
}
