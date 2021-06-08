import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Enum.*;


public class ConfigPartie {
    public static int nombre_lieu=4;
    public static int nombre_connexion=4;
    public static int nombre_perso=4;
    public static int nombre_moment=4;
    public static int nombre_positions_depart=4;
    public static int nombre_indices=15;
    public static boolean optimize_start_indices=false;
    public static boolean autorise_culdesac=false;
    public static List<Piece> list_toutes_pieces = new ArrayList<>(Arrays.asList(Piece.values()));
    public static List<Moment> list_tous_moments = new ArrayList<>(Arrays.asList(Moment.values()));
    public static List<Personne> list_toutes_personnes = new ArrayList<>(Arrays.asList(Personne.values()));

    public static List<Piece> list_pieces_partie=getList_pieces_partie();
    public static List<Moment> list_moments_partie=getList_moments_partie();
    public static List<Personne> list_personnes_partie=getList_personnes_partie();



    public static List<Piece> getList_pieces_partie(){
        List<Piece> list_a_retourner = new ArrayList<>();
        for(int i=0; i<nombre_lieu;i++){
            list_a_retourner.add(list_toutes_pieces.get(i));
        }
        return list_a_retourner;
    }
    private static List<Moment> getList_moments_partie() {
        List<Moment> list_a_retourner = new ArrayList<>();
        for(int i=0; i<nombre_moment;i++){
            list_a_retourner.add(list_tous_moments.get(i));
        }
        return list_a_retourner;
    }
    private static List<Personne> getList_personnes_partie() {
        List<Personne> list_a_retourner = new ArrayList<>();
        for(int i=0; i<nombre_perso;i++){
            list_a_retourner.add(list_toutes_personnes.get(i));
        }
        return list_a_retourner;
    }
}
