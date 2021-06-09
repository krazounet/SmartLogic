import Enum.Moment;
import Enum.Personne;
import Enum.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConfigPartie {
    //config de ce que cherche l'algo
    public static int nombre_lieu=5;
    public static int nombre_connexion=6;
    public static int nombre_perso=4;
    public static int nombre_moment=4;
    public static int nombre_positions_depart=4;
    public static int nombre_indices=15;
    public static boolean culdesac=false;

    //config d'activation ou pas de certains alo
    public static boolean find_optimisation=false;
    public static boolean optimize_start_indices=false;

    public static List<Piece> list_toutes_pieces = new ArrayList<>(Arrays.asList(Piece.values()));
    public static List<Moment> list_tous_moments = new ArrayList<>(Arrays.asList(Moment.values()));
    public static List<Personne> list_toutes_personnes = new ArrayList<>(Arrays.asList(Personne.values()));

    public static List<Piece> list_pieces_partie=getList_pieces_partie();
    public static List<Moment> list_moments_partie=getList_moments_partie();
    public static List<Personne> list_personnes_partie=getList_personnes_partie();



    public static String nom_export = getNomExport();

    private static String getNomExport() {
        String nom= nombre_perso+"P"+nombre_moment+"M"+nombre_lieu+"L"+nombre_connexion+"C ";
        if (culdesac) nom=nom+"CDS ";
        nom=nom+" - ID"+nombre_positions_depart+" IT"+nombre_indices+ " - ";
        nom=nom+ System.currentTimeMillis();
        return nom;
    }



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
