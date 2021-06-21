import Enum.*;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//Le personage P est avec le personnage P au moment M
public final class Indice_P_P_M extends Indice{
    List <Personne> list_personne;
    Moment moment;

    public Indice_P_P_M(String desc, boolean negation,List<Personne> lp, Moment moment) {
        super.typeIndice =TypeIndice.P_P_M;
        super.description=desc;
        super.negation=negation;
        super.localisationIndice = LocalisationIndice.COLONNE;

        this.list_personne = lp;
        this.moment = moment;
    }

    @Override
    public boolean check(Solution solution)
    {
    	boolean valueToReturn = true;
    	for(int idPA = 0; idPA < this.list_personne.size() - 1; idPA++)
    	{
    		Personne personne1 = this.list_personne.get(idPA);
        	for(int idPB = idPA + 1; idPB < this.list_personne.size(); idPB++)
        	{
        		Personne personne2 = this.list_personne.get(idPB);
        		if(solution.stats.get("Met_" + personne1.name() + personne2.name() + this.moment.name()) == null)
        			valueToReturn = false;
        	}
    	}
    	return(valueToReturn);
//    	return(solution.stats.get("Met_" + this.list_personne.get(0).name() + this.list_personne.get(1).name() + this.moment.name()) != null);
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        
        DrawTools.drawImageTransformed(image_indice, DrawTools.getImage("image/LieuV.png"), 50, 50, 0, 100);
        
		List<List<Point>> listListPosCharacter = Arrays.asList(
                Collections.singletonList(new Point(2, 2)),
				Arrays.asList(new Point(1, 2), new Point(3, 2)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 3), new Point(3, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 2), new Point(1, 3), new Point(3, 3)),
				Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 2), new Point(3, 2), new Point(1, 3), new Point(3, 3))
				);
		
        for (int index_pers=0;index_pers<list_personne.size();index_pers++){
            BufferedImage image_pers = DrawTools.getImage("image\\"+list_personne.get(index_pers)+".png");
            DrawTools.drawImageTransformed(image_indice,image_pers,25 * listListPosCharacter.get(list_personne.size() - 1).get(index_pers).x,25 * listListPosCharacter.get(list_personne.size() - 1).get(index_pers).y,0,40);
        }

/*        int x_img=25;
        int y_img=25;
        for (int index_pers=0;index_pers<list_personne.size();index_pers++){
            BufferedImage image_pers = DrawTools.getImage("image\\"+list_personne.get(index_pers)+".png");
            DrawTools.drawImageTransformed(image_indice.getGraphics(),image_pers,x_img,y_img,0,50);
            if (index_pers == 0) {x_img=x_img+50;}
            if (index_pers == 1) {x_img=25;y_img=75;}
            if (index_pers == 2) {x_img=x_img+50;}
            ////BUG si 5 PERSONNES ///
        }
        BufferedImage image_lien = DrawTools.getImage("image\\lien.png");
        if (list_personne.size()<3){
            DrawTools.drawImageTransformed(image_indice.getGraphics(),image_lien,50,75,0,50);
        }else{
            DrawTools.drawImageTransformed(image_indice.getGraphics(),image_lien,50,50,0,50);
        }*/
        
        return image_indice;
    }

    public static List<Indice_P_P_M> all_PPM(Solution solution){
        List<Indice_P_P_M> list_a_retourner =new ArrayList<>();
        for(Moment moment : ConfigPartie.list_moments_partie){
            for(Piece piece : ConfigPartie.list_pieces_partie) {
                List<Personne> list_p_temp =new ArrayList<>();

                for (Reservation reservation : solution.solution){
                  if ((moment == reservation.moment) && (piece == reservation.lieu.piece)){
                     list_p_temp.add(reservation.personne);
                  }
                }

                if (list_p_temp.size() > 1){
                    String desc="";
                    for(Personne personne : list_p_temp){
                        desc=desc+personne+" ,";
                    }
                    desc=desc+" sont dans la meme piece au moment "+moment;
                    list_a_retourner.add(new Indice_P_P_M(desc, false,list_p_temp,moment));
                }

            }
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
