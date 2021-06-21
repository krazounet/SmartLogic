import Enum.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


//occurence totale du lieu L dans le graphe
public final class Indice_x_L extends Indice{
int compteur;
Piece piece;

    public Indice_x_L(String desc, Piece piece, int compteur,boolean negation) {
        super.typeIndice = TypeIndice.x_L;
        super.description= desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.LIGNE;

        this.piece=piece;
        this.compteur=compteur;

    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcL_" + this.piece.name()).equals("" + compteur));
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        
        BufferedImage image_px = DrawTools.getImage("image\\PersoX.png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),image_px,25,50,0,40);
        DrawTools.drawText(image_indice,"x"+compteur,65,50, "Arial", Color.BLACK,40,0);
        return image_indice;

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
