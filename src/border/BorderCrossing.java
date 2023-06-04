package border;

import java.util.List;

public class BorderCrossing {
    private List<PoliceTerminal> policeList;
    private List<CustomsTerminal> customsList;

    public BorderCrossing(List<PoliceTerminal> police,List<CustomsTerminal> customs)
    {
        this.policeList=police;
        this.customsList=customs;
    }
}
