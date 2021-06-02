import java.util.ArrayList;
import java.util.List;
import Enum.*;

public class Lieu {
    Piece piece;
    List<Piece> lien =new ArrayList<>();

    public Lieu(Piece piece, List<Piece> lien) {
        this.piece = piece;
        this.lien = lien;
    }
}
