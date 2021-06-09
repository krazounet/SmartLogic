import Enum.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Le personage P est avec le personnage P dans la piece L
public final class Indice_P_P_L extends Indice{
    List <Personne> list_personne;
    Piece piece;

    public Indice_P_P_L(String desc, boolean negation,List<Personne> lp, Piece piece) {
        super.typeIndice =TypeIndice.P_P_L;
        super.description=desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.LIGNE;

        this.list_personne = lp;
        this.piece = piece;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("Met_" + this.list_personne.get(0).name() + this.list_personne.get(1).name() + this.piece.name()) != null);
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
                    desc=desc+" sont ensemble dans la piece "+piece;
                    list_a_retourner.add(new Indice_P_P_L(desc, false,list_p_temp,piece));
                }

            }
        }



        return list_a_retourner;
    }

    @Override
    public BufferedImage export() {
        /*
        BufferedImage image_indice = new BufferedImage((list_personne.size()+1)*100,100,BufferedImage.TYPE_INT_ARGB);
        BufferedImage image_piece = DrawTools.getImage("image\\"+piece+".png");
        DrawTools.drawImageCenter(image_indice.getGraphics(),image_piece,50,50);
        for (int index_pers=0;index_pers<list_personne.size();index_pers++){
            BufferedImage image_pers = DrawTools.getImage("image\\"+list_personne.get(index_pers)+".png");
            DrawTools.drawImageCenter(image_indice.getGraphics(),image_pers,150+(index_pers*100),50);

        }
        return image_indice;

         */
        BufferedImage image_indice = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        int x_img=25;
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
        }

        return image_indice;
    }

    @Override
    public String getEmplacement() {
        return ""+piece;
    }

    @Override
    public Coordonnee getCoordonnee() {
        int x=50 + (ConfigPartie.list_moments_partie.size()+1)*100;
        int y=50 + (piece.ordinal()+1)*100;
        return new Coordonnee(x,y);
    }
}
