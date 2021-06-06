import Enum.*;

import java.awt.image.BufferedImage;

public abstract class Indice {
    TypeIndice typeIndice;
    String description;
    boolean negation;

    @Override
    public String toString() {
        return "Indice{" +
                "typeIndice=" + typeIndice +
                ", description='" + description + '\'' +
                ", negation=" + negation +
                '}';
    }

    public BufferedImage export() {
        return null;
    }
}
