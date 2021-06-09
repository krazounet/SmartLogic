import Enum.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Indice d'une Personne dans un Lieu a un Moment
public final class Indice_P_L_M extends Indice {
Personne personne;
Piece piece;
Moment moment;

    public Indice_P_L_M(String desc,Personne p, Piece piece, Moment m,boolean negation) {
        super.typeIndice = TypeIndice.P_L_M;
        super.description= desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.CASE;

        this.piece=piece;
        personne=p;
        moment=m;

    }
    
    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("Res_" + this.personne.name() + this.moment.name() + this.piece.name()) != null);
    }

    public static List<Indice_P_L_M> all_PLM(Solution solution){
    List<Indice_P_L_M> list_a_retourner =new ArrayList<>();
        for (Reservation reservation : solution.solution){
            list_a_retourner.add(new Indice_P_L_M(reservation.personne +" est dans "+reservation.lieu.piece + " au moment "+ reservation.moment,reservation.personne,reservation.lieu.piece, reservation.moment,false));
        }
    return list_a_retourner;
    }

    public static List<Indice_P_L_M> all_PLM_N(Solution solution){
        List<Indice_P_L_M> list_a_retourner =new ArrayList<>();
        for (Reservation reservation : solution.solution){
            for(Piece piece : ConfigPartie.list_pieces_partie){
                if (piece != reservation.lieu.piece){
                    list_a_retourner.add(new Indice_P_L_M(reservation.personne + " n est pas dans " +piece + " au moment "+ reservation.moment,reservation.personne,piece , reservation.moment,true));
                }
            }

        }
        return list_a_retourner;
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(300,100,BufferedImage.TYPE_INT_ARGB);
        BufferedImage img_personne = DrawTools.getImage(SmartLogic.repertoire+"image\\"+personne+".png");
        BufferedImage img_moment = DrawTools.getImage(SmartLogic.repertoire+"image\\"+moment+".png");
        BufferedImage img_lieu = DrawTools.getImage(SmartLogic.repertoire+"image\\"+piece+".png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),img_personne,50,50,0,100);
        DrawTools.drawImageTransformed(image_indice.getGraphics(),img_moment,150,50,0,100);
        DrawTools.drawImageTransformed(image_indice.getGraphics(),img_lieu,250,50,0,100);
        return image_indice;
    }
}
