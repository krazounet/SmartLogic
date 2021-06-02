import Enum.Piece;

import java.util.ArrayList;
import java.util.List;

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


     //   List<Conexion> list_con = Conexion.getList_conexions_retenues();


    }
}
