import Enum.*;
import java.util.ArrayList;
import java.util.List;


//nombre de Pieces différentes à un Moment
public final class Indice_x_Ldiff_M extends Indice{
    int combien; //nbre de piece différentes
    Moment moment;

    public Indice_x_Ldiff_M(int combien, Moment moment,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_L_M;
        super.description=desc;
        super.negation=negation;
        this.combien = combien;
        this.moment = moment;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcLM_" + this.moment.name()).equals("" + combien));
    }

    public static List<Indice_x_Ldiff_M> all_xLdiffM(Solution solution) {
        List<Indice_x_Ldiff_M> list_a_retourner = new ArrayList<>();
        for (Moment moment : ConfigPartie.list_moments_partie){

                //
                List<Piece> list_piece_tmp=new ArrayList<>();
                for (Reservation reservation : solution.solution){
                	if(reservation.moment != moment)
                		continue;
                    if (! list_piece_tmp.contains(reservation.lieu.piece)  ) list_piece_tmp.add(reservation.lieu.piece);
                }
                list_a_retourner.add(new Indice_x_Ldiff_M(list_piece_tmp.size(),moment,"A "+moment+", les persos sont repartis dans "+list_piece_tmp.size()+" pieces differentes",false));
                //ici on peut faire la négation
                //

        }
        return list_a_retourner;
    }

}
