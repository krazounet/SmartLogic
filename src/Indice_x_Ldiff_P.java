import Enum.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//nombre de lieu différent pour un Perso
public final class Indice_x_Ldiff_P extends Indice{
    int combien; //nbre de piece différentes
    Personne personne;
    public Indice_x_Ldiff_P(int combien, Personne personne,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_Ldiff_P;
        super.description=desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.HORS_TABLEAU;

        this.combien = combien;
        this.personne = personne;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcLP_" + this.personne.name()).equals("" + combien));
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(200,50,BufferedImage.TYPE_INT_ARGB);
        
        BufferedImage image_px = DrawTools.getImage("image\\"+personne+".png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),image_px,25,25,0,50);
        DrawTools.drawText(image_indice,":"+combien+"x",80,25, "Arial", Color.BLACK,40,0);
        BufferedImage image_lx = DrawTools.getImage("image\\LieuX.png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),image_lx,135,25,0,50);

        return image_indice;
    }

    public static List<Indice_x_Ldiff_P> all_xLdiffP(Solution solution) {
        List<Indice_x_Ldiff_P> list_a_retourner = new ArrayList<>();
        for (Personne personne : ConfigPartie.list_personnes_partie){

            //
            List<Piece> list_piece_tmp=new ArrayList<>();
            for (Reservation reservation : solution.solution){
            	//if(reservation.personne != personne)
            	//	continue;
                if ((reservation.personne == personne)&&(! list_piece_tmp.contains(reservation.lieu.piece)  )) list_piece_tmp.add(reservation.lieu.piece);
            }
            list_a_retourner.add(new Indice_x_Ldiff_P(list_piece_tmp.size(),personne,"La personne "+personne+" a visite "+list_piece_tmp.size()+" pieces differentes",false));
            //ici on peut faire la négation
            //

        }
        return list_a_retourner;
    }
    @Override
    public String getEmplacement() {
        return null;
    }

    @Override
    public Coordonnee getCoordonnee() {
        return null;
    }

}
