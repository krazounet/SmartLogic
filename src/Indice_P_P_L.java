import Enum.*;
import java.util.ArrayList;
import java.util.List;

//Le personage P est avec le personnage P au moment M
public final class Indice_P_P_L extends Indice{
    List <Personne> list_personne;
    Piece piece;

    public Indice_P_P_L(String desc, boolean negation,List<Personne> lp, Piece piece) {
        super.typeIndice =TypeIndice.P_P_M;
        super.description=desc;
        super.negation=negation;
        this.list_personne = lp;

        this.piece = piece;
    }

    public static List<Indice_P_P_L> all_PPL(Solution solution){
        List<Indice_P_P_L> list_a_retourner =new ArrayList<>();
        for(Moment moment : ConfigPartie.list_moments_partie){
            for(Piece piece : ConfigPartie.list_pieces_partie) {
                List<Personne> list_p_temp =new ArrayList<>();

                for (Reservation reservation : solution.solution){
                    if ((moment == reservation.moment) && (piece == reservation.lieu.piece) && (!list_p_temp.contains(reservation.personne))){
                        list_p_temp.add(reservation.personne);
                    }
                }

                if (list_p_temp.size() > 1){
                    String desc="A un moment, ";
                    for(Personne personne : list_p_temp){
                        desc=desc+personne+" ,";
                    }
                    desc=desc+" sont ensemble dans la piece "+piece+".";
                    list_a_retourner.add(new Indice_P_P_L(desc, false,list_p_temp,piece));
                }

            }
        }



        return list_a_retourner;
    }


}
