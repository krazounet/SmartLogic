import Enum.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//nombre de personne dans Lieu au moment M
public final class Indice_x_L_M extends Indice {
    int combien;
    Moment moment;
    Piece piece;
    public Indice_x_L_M(int combien, Moment moment, Piece piece,String desc, boolean negation) {
        super.typeIndice = TypeIndice.x_L_M;
        super.description=desc;
        super.negation=negation;
        super.localisationIndice=LocalisationIndice.CASE;

        this.combien = combien;
        this.moment = moment;
        this.piece = piece;
    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("Grp_" + this.moment.name() + this.piece.name()).equals("" + combien));
    }

    @Override
    public BufferedImage export() {
        BufferedImage image_indice = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        BufferedImage image_px = DrawTools.getImage("image\\PersoX.png");
        DrawTools.drawImageTransformed(image_indice.getGraphics(),image_px,70,50,0,50);
        DrawTools.drawText(image_indice,""+combien,25,50, "Arial", Color.BLACK,40,0);
        return image_indice;
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
                list_a_retourner.add(new Indice_x_L_M(compteur,moment,piece,compteur+" personne(s) est(sont) dans la piece "+piece+" au moment "+moment,false));
                //ici on peut faire la négation
                //
            }
        }
        return list_a_retourner;
    }
    @Override
    public String getEmplacement() {
        return piece+""+moment;
    }

    @Override
    public Coordonnee getCoordonnee() {
        int x=50 + (moment.ordinal()+1)*100;
        int y=50 + (piece.ordinal()+1)*100;
        return new Coordonnee(x,y);
    }

}
