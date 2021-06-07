import Enum.*;
import java.util.ArrayList;
import java.util.List;


//occurence totale de du lieu L dans le graphe
public final class Indice_x_L extends Indice{
int compteur;
Piece piece;

    public Indice_x_L(String desc, Piece piece, int compteur,boolean negation) {
        super.typeIndice = TypeIndice.x_L;
        super.description= desc;
        super.negation=negation;

        this.piece=piece;
        this.compteur=compteur;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcL_" + this.piece.name()).equals("" + compteur));
    }

    public static List<Indice_x_L> all_xL(Solution solution) {
        List<Indice_x_L> list_a_retourner = new ArrayList<>();
        for(Piece piece : ConfigPartie.list_pieces_partie){
            int compt =0 ;
            for(Reservation reservation : solution.solution){
                if (piece == reservation.lieu.piece)compt++;
            }
            list_a_retourner.add(new Indice_x_L("La piece "+piece+" est visitee "+compt+" fois",piece,compt,false));
        }
        return list_a_retourner;
    }

}
