import Enum.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//nombre de Pieces différentes à un Moment
public final class Indice_x_Ldiff_M extends Indice{
    int combien; //nbre de piece différentes
    Moment moment;

    public Indice_x_Ldiff_M(int combien, Moment moment,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_Ldiff_M;
        super.description=desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.COLONNE;

        this.combien = combien;
        this.moment = moment;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcLM_" + this.moment.name()).equals("" + combien));
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        
/*		List<List<Point>> listListPosCharacter = Arrays.asList(
                Collections.singletonList(new Point(2, 2)),
				Arrays.asList(new Point(1, 2), new Point(3, 2)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 3), new Point(3, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 2), new Point(1, 3), new Point(3, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 2), new Point(3, 2), new Point(1, 3), new Point(3, 3))
				);
		
        for (int index_pers=0;index_pers<combien;index_pers++){
            BufferedImage image_pers = DrawTools.getImage("image/LieuX.png");
            DrawTools.drawImageTransformed(image_indice,image_pers,25 * listListPosCharacter.get(combien - 1).get(index_pers).x,25 * listListPosCharacter.get(combien - 1).get(index_pers).y,0,40);
        }*/

        BufferedImage image_px = DrawTools.getImage("image\\LieuX.png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),image_px,25,50,0,40);
        DrawTools.drawText(image_indice,"x"+combien,65,50, "Arial", Color.BLACK,40,0);
        return image_indice;
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
    @Override
    public String getEmplacement() {
        return ""+moment;
    }

    @Override
    public Coordonnee getCoordonnee() {
        int x=50 + (moment.ordinal()+1)*100;
        int y=50 + (ConfigPartie.list_pieces_partie.size()+1)*100;
        return new Coordonnee(x,y);
    }
}
