import Enum.*;

import java.util.ArrayList;
import java.util.List;

//nombre d occurence que Lieu au moment M
public final class Indice_x_L_M extends Indice {
    int combien;
    Moment moment;
    Piece piece;
    public Indice_x_L_M(int combien, Moment moment, Piece piece,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_L_M;
        super.description=desc;
        super.negation=negation;
        this.combien = combien;
        this.moment = moment;
        this.piece = piece;
    }

    public static List<Indice_x_L_M> all_xPL(Solution solution) {
        List<Indice_x_L_M> list_a_retourner = new ArrayList<>();
        for (Moment moment : ConfigPartie.list_moments_partie){
            for (Piece piece : ConfigPartie.list_pieces_partie){
                //
                int compteur = 0;
                for (Reservation reservation : solution.solution){
                    if ((reservation.lieu.piece == piece)&&(reservation.moment==moment)) compteur++;
                }
                list_a_retourner.add(new Indice_x_L_M(compteur,moment,piece,compteur+" personne(s) est(sont) dans la pièce "+piece+" au moment "+moment,false));
                //ici on peut faire la négation
                //
            }
        }
        return list_a_retourner;
    }


}
