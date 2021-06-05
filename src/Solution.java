import java.util.ArrayList;
import java.util.List;
import Enum.*;

public class Solution {

    List<Reservation> solution = new ArrayList<>();

    public Solution(Plan plan) {
        for (Personne personne : ConfigPartie.list_personnes_partie) {
            Lieu lieu=null;
            for (Moment present : ConfigPartie.list_tous_moments) {

                if (present == Moment.M1) lieu = plan.getRandomLieu();
                else {
                    lieu = lieu.getRandomLieuSuivant(plan);
                       }
                solution.add(new Reservation(lieu,personne,present));
            }
        }
    }

    @Override
    public String toString() {
        String s="";
        for (Reservation reservation : solution){
            s=s+reservation.toString();
        }
        return s;
    }
}
