import Enum.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plan {

    List<Lieu> list_lieu = new ArrayList<>();

    public Plan() {
        //la taille du plan dépend de config.nombre_lieu
        //le nombre de connexion de ConfigPartie.nombre_connexion
        //ConfigPartie.nombre_connexion
        //ConfigPartie.list_pieces_partie
        for (Piece piece : ConfigPartie.list_pieces_partie){
            List<Piece> list_pieces_connectes=new ArrayList<>();
            for (Conexion con : Conexion.getList_conexions_retenues()){
                if(con.contains(piece)){
                    list_pieces_connectes.add(con.autre(piece));
                }
            }
            list_lieu.add(new Lieu(piece,list_pieces_connectes));
        }

    }

    public Lieu getRandomLieu(){
        return list_lieu.get(new Random().nextInt(list_lieu.size()));
    }

    public Lieu getLieuFromPiece(Piece piece){
        for (Lieu lieu : list_lieu){
            if (lieu.piece == piece) return lieu;

        }
        return null;
    }

    @Override
    public String toString() {
        String s="";
        for (Lieu l : list_lieu){
            s=s+l.toString();
        }
        return s;
    }
}
