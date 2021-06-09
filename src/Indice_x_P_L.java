import Enum.*;

import java.awt.image.BufferedImage;
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
        super.localisationIndice=LocalisationIndice.LIGNE;

        this.combien = combien;
        this.personne = personne;
        this.piece = piece;
    }

    @Override
    public boolean check(Solution solution)
    {
    	return(solution.stats.get("OcPinL_" + this.personne.name() + this.piece.name()).equals("" + combien));
    }

    @Override
    public BufferedImage export() {
        return null;
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
                list_a_retourner.add(new Indice_x_P_L(compteur,personne,piece,personne+" est "+compteur+ " fois dans la piece "+piece,false));
                //ici on peut faire la négation
                //
            }
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
