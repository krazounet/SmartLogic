import Enum.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conexion {
    Piece Piece1;
    Piece Piece2;

    public Conexion(Piece piece1,Piece piece2) {
        this.Piece1 = piece1;
        this.Piece2 = piece2;
    }
    public static List<Conexion> getList_conexions_possibles(){
        List<Conexion> list_a_retourner = new ArrayList<>();
        for(int i=0; i<ConfigPartie.list_pieces_partie.size()-1;i++){
            for(int j=i+1; j<ConfigPartie.list_pieces_partie.size();j++){
                Conexion conexion = new Conexion(ConfigPartie.list_pieces_partie.get(i),ConfigPartie.list_pieces_partie.get(j));
                list_a_retourner.add(conexion);
            }
        }
        return list_a_retourner;
    }

    public static List<Conexion> getList_conexions_retenues(){
        List<Conexion> list_temp_connexions_possibles =new ArrayList<>(getList_conexions_possibles());
        Collections.shuffle(list_temp_connexions_possibles);
        List<Conexion> list_a_retourner =new ArrayList<>();
        for (int i=0;i<ConfigPartie.nombre_connexion;i++){
            list_a_retourner.add(list_temp_connexions_possibles.get(i));
        }
        return list_a_retourner;
    }

public boolean contains (Piece piece_a_tester){
        if (piece_a_tester == Piece1) return true;
    return piece_a_tester == Piece2;
}
    public Piece autre (Piece piece_a_tester){
        if (piece_a_tester == Piece1) return Piece2;
        if (piece_a_tester == Piece2) return Piece1;
        return null;
    }

    @Override
    public String toString() {
        return "Conexion{" +
                "Piece1=" + Piece1 +
                ", Piece2=" + Piece2 +
                '}';
    }
}
