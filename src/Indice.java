import Enum.*;

import java.awt.image.BufferedImage;

public abstract class Indice {
    TypeIndice typeIndice;
    String description;
    boolean negation;
    boolean usefull=true;
    LocalisationIndice localisationIndice;

    public abstract boolean check(Solution solution);

    @Override
    public String toString() {
        return "Indice{" +
                "typeIndice=" + typeIndice +
                ", description='" + description + '\'' +
                ", negation=" + negation +
                '}';
    }

    public abstract BufferedImage export() ;
    public abstract String getEmplacement();
    public abstract Coordonnee getCoordonnee() ;

}
