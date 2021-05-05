package fr.ihm.accidents;

import java.util.ArrayList;

public class AccidentModel {

    private final ArrayList<Accident> accidents;

    public AccidentModel() {
        this.accidents = new ArrayList<>();
    }

    public void addAccident(Accident accident) {
        this.accidents.add(accident);
    }

    public ArrayList<Accident> getAccidents() {
        return accidents;
    }
}
