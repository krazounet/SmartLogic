import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Enum.*;

public class Lieu {
    Piece piece;
    List<Piece> lien =new ArrayList<>();

    public Lieu(Piece piece, List<Piece> lien) {
        this.piece = piece;
        this.lien = lien;
    }

    @Override
    public String toString() {
        String s="Piece "+piece+" : ";
        for (Piece p : lien){
            s=s+p+" ";

        }
        s=s+"";
        return s;
    }
    public Piece getRandomPieceSuivante(){
        return lien.get(new Random().nextInt(lien.size()));
    }
    public Lieu getRandomLieuSuivant(Plan p){
        Piece piece = getRandomPieceSuivante();
        Lieu lieu = p.getLieuFromPiece(piece);
        return lieu;

    }
}
