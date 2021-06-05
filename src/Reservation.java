import Enum.*;

public class Reservation {
    Lieu lieu;
    Personne personne;
    Moment moment;

    public Reservation(Lieu lieu, Personne personne, Moment moment) {
        this.lieu = lieu;
        this.personne = personne;
        this.moment = moment;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "lieu=" + lieu +
                ", personne=" + personne +
                ", moment=" + moment +
                "}\n";
    }
}
