package sst.licences.main;

import sst.licences.model.Comite;

public class CreateComiteFile {
    public static void main(String[] args) {
        Comite comite = new Comite();
        comite.addMembre(new Comite.MembreDuComite("Ledur", "Corine"));
        comite.addMembre(new Comite.MembreDuComite("Tonglet", "Marine"));
        comite.addMembre(new Comite.MembreDuComite("Tonglet", "Caroline"));
        comite.addMembre(new Comite.MembreDuComite("Moreno", "Nicolas"));
        comite.addMembre(new Comite.MembreDuComite("Stiennon", "Stéphane"));
        comite.addMembre(new Comite.MembreDuComite("Masse", "Charlotte"));
        comite.addMembre(new Comite.MembreDuComite("Willems", "Laura"));
        comite.addMembre(new Comite.MembreDuComite("Steurs", "Tifany"));

        comite.save();
    }
}
