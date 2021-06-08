import Enum.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        List<Conexion> list_a_retourner =new ArrayList<>();
    	boolean listOK = false;
    	do
    	{
	        List<Conexion> list_temp_connexions_possibles =new ArrayList<>(getList_conexions_possibles());
	        Collections.shuffle(list_temp_connexions_possibles);
	        list_a_retourner.clear();
	        for (int i=0;i<ConfigPartie.nombre_connexion;i++){
	            list_a_retourner.add(list_temp_connexions_possibles.get(i));
	        }
	        
	        // Test des culs de sac
	        if(ConfigPartie.culdesac)
	        	listOK = true;
	        else
	        {
				HashMap<Piece,Integer> nbOccurenceLieu = new HashMap<>();
				for(Piece piece :ConfigPartie.list_pieces_partie)//
					nbOccurenceLieu.put(piece,0);
				for(Conexion conexionToTest : list_a_retourner)
				{
					nbOccurenceLieu.put(conexionToTest.Piece1,nbOccurenceLieu.get(conexionToTest.Piece1)+1 );
					nbOccurenceLieu.put(conexionToTest.Piece2,nbOccurenceLieu.get(conexionToTest.Piece2)+1 );
				}
				if(!nbOccurenceLieu.values().contains(1))listOK = true;

	        }
	        
	        // Test pour voir si des routes se croisent
	        if(listOK)
	        {
		        for(int idCnx1 = 0; idCnx1 < list_a_retourner.size() - 1; idCnx1++)
		        {
		        	Conexion cnx1 = list_a_retourner.get(idCnx1);
		        	for(int idCnx2 = idCnx1 + 1; idCnx2 < list_a_retourner.size(); idCnx2++)
		        	{
			        	Conexion cnx2 = list_a_retourner.get(idCnx2);
	
			        	List<Piece> listA = new ArrayList<Piece>();
						List<Piece> listB = new ArrayList<Piece>();
						boolean inListA = true;
						for(Piece pieceToTest : ConfigPartie.list_pieces_partie)
						{
							if(pieceToTest == cnx1.Piece1 || pieceToTest == cnx1.Piece2)
								inListA = !inListA;
							if(inListA)
								listA.add(pieceToTest);
							else
								listB.add(pieceToTest);
						}
						listA.remove(cnx1.Piece1);
						listA.remove(cnx1.Piece2);
						listB.remove(cnx1.Piece1);
						listB.remove(cnx1.Piece2);
						if(listA.contains(cnx2.Piece1) && listB.contains(cnx2.Piece2) || listB.contains(cnx2.Piece1) && listA.contains(cnx2.Piece2))
							listOK = false;
		        	}
		        }
	        }
    	} while(!listOK);
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
