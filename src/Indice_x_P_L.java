import Enum.*;

import java.util.ArrayList;
import java.util.List;

//nombre d occurence que P soit dans L
public final class Indice_x_P_L extends Indice {
    int combien;
    Personne personne;
    Piece piece;

    public Indice_x_P_L(int combien, Personne personne, Piece piece,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_P_L;
        super.description=desc;
        super.negation=negation;
        this.combien = combien;
        this.personne = personne;
        this.piece = piece;
    }

    public static List<Indice_x_P_L> all_xPL(Solution solution) {
        List<Indice_x_P_L> list_a_retourner = new ArrayList<>();
        for (Personne personne : ConfigPartie.list_personnes_partie){
            for (Piece piece : ConfigPartie.list_pieces_partie){
                //
                int compteur = 0;
                for (Reservation reservation : solution.solution){
                 if ((reservation.lieu.piece == piece)&&(reservation.personne==personne)) compteur++;
                }
                list_a_retourner.add(new Indice_x_P_L(compteur,personne,piece,personne+" est "+compteur+ " fois dans la pièce "+piece,false));
                //ici on peut faire la négation
                //
            }
        }
        return list_a_retourner;
    }

}
