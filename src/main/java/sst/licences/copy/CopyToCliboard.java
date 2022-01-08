package sst.licences.copy;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public abstract class CopyToCliboard {

    private String stringContent = null;

    public void process() {
        StringSelection stringSelection = new StringSelection(stringContent);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    protected void setStringContent(String stringContent) {
        this.stringContent = stringContent;
    }
}
