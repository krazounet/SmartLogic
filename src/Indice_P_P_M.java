import Enum.*;
import java.util.ArrayList;
import java.util.List;

//Le personage P est avec le perosnnage P ay moment M
public final class Indice_P_P_M extends Indice{
    List <Personne> list_personne;
    Moment moment;

    public Indice_P_P_M(String desc, boolean negation,List<Personne> lp, Moment moment) {
        super.typeIndice =TypeIndice.P_P_M;
        super.description=desc;
        super.negation=negation;
        this.list_personne = lp;

        this.moment = moment;
    }

    public static List<Indice_P_P_M> all_PPM(Solution solution){
        List<Indice_P_P_M> list_a_retourner =new ArrayList<>();
        for(Moment moment : ConfigPartie.list_moments_partie){
            for(Piece piece : ConfigPartie.list_pieces_partie) {
                List<Personne> list_p_temp =new ArrayList<>();

                for (Reservation reservation : solution.solution){
                  if ((moment == reservation.moment) && (piece == reservation.lieu.piece) && (!list_p_temp.contains(reservation.personne))){
                     list_p_temp.add(reservation.personne);
                  }
                }

                if (list_p_temp.size() > 1){
                    String desc="";
                    for(Personne personne : list_p_temp){
                        desc=desc+personne+" ,";
                    }
                    desc=desc+" sont dans la même piece au moment "+moment+".";
                    list_a_retourner.add(new Indice_P_P_M(desc, false,list_p_temp,moment));
                }

            }
        }



        return list_a_retourner;
    }


}
