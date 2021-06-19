import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Enum.*;

public class Solution {

    List<Reservation> solution = new ArrayList<>();
    HashMap<String, String> stats = new HashMap<>();

    public Solution(Plan plan) {
        for (Personne personne : ConfigPartie.list_personnes_partie) {
            Lieu lieu=null;
            for (Moment present : ConfigPartie.list_moments_partie) {

                if (present == Moment.M1) lieu = plan.getRandomLieu();
                else {
					assert lieu != null;
					lieu = lieu.getRandomLieuSuivant(plan);
                       }
                solution.add(new Reservation(lieu,personne,present));
            }
        }
        
        createHashMaps();
    }
    
    public Solution(List<Reservation> solutionI)
    {
    	solution = solutionI;
        createHashMaps();
    }
    
    public void createHashMaps()
    {
        // Cr�ation des HashMaps des r�servations
        for(Reservation reservation : solution)
        {
            // Liste des r�servations : "Res_PPMML" = "Yes"
        	stats.put(new StringBuilder("Res_").append(reservation.personne.name()).append(reservation.moment.name()).append(reservation.lieu.piece.name()).toString(), "Yes");
        }
        
        // Cr�ation des HashMaps qui concernent deux r�servations
        for(int r1 = 0; r1 < solution.size(); r1++)
        {
        	Reservation resa1 = solution.get(r1);
			for (Reservation resa2 : solution) {
				if (resa1 == resa2)
					continue;
				if (resa1.moment == resa2.moment && resa1.lieu == resa2.lieu) {
					// Deux personnes dans le m�me lieu connu � un moment inconnu : "Met_P1P2L" = "MM"
					stats.put(new StringBuilder("Met_").append(resa1.personne.name()).append(resa2.personne.name()).append(resa1.lieu.piece.name()).toString(), resa1.moment.name());
					// Deux personnes dans le m�me lieu inconnu � un moment connu : "Met_P1P2MM" = "L"
					stats.put(new StringBuilder("Met_").append(resa1.personne.name()).append(resa2.personne.name()).append(resa1.moment.name()).toString(), resa1.lieu.piece.name());
				}
			}
        }
        
        // Cr�ation des HashMaps qui concernent un lieu connu et un moment connu
        for(Moment present : ConfigPartie.list_moments_partie)
        {
        	for(Piece piece : ConfigPartie.list_pieces_partie)
        	{
        		int compteur = 0;
        		for(Reservation reservation : solution)
        			if(reservation.moment == present && reservation.lieu.piece == piece)
        				compteur++;
        		// N personnes dans le m�me lieu connu � un moment connu : "Grp_MML" = "N"
            	stats.put(new StringBuilder("Grp_").append(present.name()).append(piece.name()).toString(), ""+compteur);
        	}
        }
        
        // Cr�ation des HashMaps qui indiquent le nombre d'occurence des lieux
    	for(Piece piece : ConfigPartie.list_pieces_partie)
    	{
    		int compteur = 0;
    		for(Reservation reservation : solution)
    			if(reservation.lieu.piece == piece)
    				compteur++;
    		// Occurence d'un lieu dans le graphe : "OcL_L" = "N"
        	stats.put(new StringBuilder("OcL_").append(piece.name()).toString(), "" + compteur);
    	}
    	
    	// Cr�ation des HashMaps qui indiquent le nombre de lieux diff�rents � un moment donn�
        for(Moment present : ConfigPartie.list_moments_partie)
        {
        	List<Piece> listDiffPieces = new ArrayList<>();
    		for(Reservation reservation : solution)
    		{
    			if(reservation.moment == present)
    			{
    				if(!listDiffPieces.contains(reservation.lieu.piece))
    					listDiffPieces.add(reservation.lieu.piece);
    			}
    		}
    		// Nombre de lieu � un moment : "OcLM_MM" = "N"
        	stats.put(new StringBuilder("OcLM_").append(present.name()).toString(), "" + listDiffPieces.size());
        }

        // Cr�ation des HashMaps qui indiquent le nombre de lieux diff�rents visit�s par un personnage donn�
        for(Personne personne : ConfigPartie.list_personnes_partie)
        {
        	List<Piece> listDiffPieces = new ArrayList<>();
    		for(Reservation reservation : solution)
    		{
    			if(reservation.personne == personne)
    			{
    				if(!listDiffPieces.contains(reservation.lieu.piece))
    					listDiffPieces.add(reservation.lieu.piece);
    			}
    		}
    		// Nombre de lieu visit� par une personne : "OcLP_PP" = "N"
        	stats.put(new StringBuilder("OcLP_").append(personne.name()).toString(), "" + listDiffPieces.size());
        }

        // Cr�ation des HashMaps qui indiquent le nombre de fois qu'une personne � visit� un lieu donn�
        for(Personne personne : ConfigPartie.list_personnes_partie)
        {
        	for(Piece piece : ConfigPartie.list_pieces_partie)
        	{
    			int compteur = 0;
        		for(Reservation reservation : solution)
        		{
        			if(reservation.personne == personne && reservation.lieu.piece == piece)
        				compteur++;
        		}
        		// Nombre de fois qu'une personne donn� � visit� un lieu donn� : "OcPinL_PPL" = "N"
            	stats.put(new StringBuilder("OcPinL_").append(personne.name()).append(piece.name()).toString(), "" + compteur);
        	}
        }


    }
    
    public static List<Solution> getAllValidSolutionFromListIndices(Plan plan, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres)
    {
    	List<Solution> listSolutions = new ArrayList<>();
    	
    	List<List<List<Reservation>>> allPossibleMoves = new ArrayList<>();
    	
    	for(Personne personne : ConfigPartie.list_personnes_partie)
    	{
        	List<List<Reservation>> allPossibleMoveIndiv = getAllPossibleMoveForAPerson(plan, personne, liste_indices_depart, liste_indices_autres);
        	
        	for(int indexMoveToTest = 0; indexMoveToTest < allPossibleMoveIndiv.size(); indexMoveToTest++)
        	{
        		Solution solutionToTest = new Solution(allPossibleMoveIndiv.get(indexMoveToTest));
        		
        		boolean solutionValid = true;
        		for(Indice_P_L_M indiceToTest : liste_indices_depart)
        		{
        			if(indiceToTest.personne == personne)
        			{
            			if(!indiceToTest.check(solutionToTest))
        					solutionValid = false;
        			}
        		}
        		
        		if(!solutionValid)
        		{
        			allPossibleMoveIndiv.remove(indexMoveToTest);
        			indexMoveToTest--;
        		}
        	}
        	
        	allPossibleMoves.add(allPossibleMoveIndiv);
    	}
    	
    	List<Integer> listIndex = new ArrayList<>();
    	for(int i = 0; i < ConfigPartie.list_personnes_partie.size(); i++)
    		listIndex.add(0);

    	addAllSolutions(listSolutions, allPossibleMoves, listIndex, liste_indices_autres);
    	
    	return(listSolutions);
    }
    
    public static void addAllSolutions(List<Solution> listSolutions, List<List<List<Reservation>>> allPossibleMoves, List<Integer> listIndex, List<Indice> liste_indices_autres)
    {
    	List<Reservation> listToAdd = new ArrayList<>();
    	
    	for(int idPerso = 0; idPerso < listIndex.size(); idPerso++)
    	{
    		listToAdd.addAll(allPossibleMoves.get(idPerso).get(listIndex.get(idPerso)));
    	}

    	Solution solutionToAdd = new Solution(listToAdd);
		boolean allIndicesOK = true;
		for(Indice indiceToTest : liste_indices_autres)
		{
			if(!indiceToTest.check(solutionToAdd))
			{
				allIndicesOK = false;
				break;
			}
		}
		
		if(allIndicesOK)
	    	listSolutions.add(solutionToAdd);
		
    	
    	int posToIncrement = listIndex.size() - 1;
    	
    	do
    	{
    		int valueToIncrement = listIndex.get(posToIncrement);
    		valueToIncrement++;
    		if(valueToIncrement >= allPossibleMoves.get(posToIncrement).size())
    		{
    			listIndex.set(posToIncrement, 0);
    			posToIncrement--;
    		}
    		else
    		{
    			listIndex.set(posToIncrement, valueToIncrement);

    	    	listToAdd = new ArrayList<>();
    	    	
    	    	for(int idPerso = 0; idPerso < listIndex.size(); idPerso++)
    	    	{
    	    		listToAdd.addAll(allPossibleMoves.get(idPerso).get(listIndex.get(idPerso)));
    	    	}

    	    	solutionToAdd = new Solution(listToAdd);
    			allIndicesOK = true;
    			for(Indice indiceToTest : liste_indices_autres)
    			{
    				if(!indiceToTest.check(solutionToAdd))
    				{
    					allIndicesOK = false;
    					break;
    				}
    			}
    			
    			if(allIndicesOK)
    			{
    		    	listSolutions.add(solutionToAdd);
    		    	if(listSolutions.size() > 1)
    		    		return;
    			}

    	    	posToIncrement = listIndex.size() - 1;
    		}
    	} while(posToIncrement != -1);
    }

    public static List<List<Reservation>> getAllPossibleMoveForAPerson(Plan plan, Personne personne, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres)
    {
    	List<List<Reservation>> allPossibleMove = new ArrayList<>();

    	getAllPossibleMoveForAPersonRecursive(allPossibleMove, new ArrayList<>(), plan, personne, liste_indices_depart, liste_indices_autres, false);
    	
        return(allPossibleMove);
    }
    
    public static void getAllPossibleMoveForAPersonRecursive(List<List<Reservation>> allPossibleMove, List<Reservation> actualMove, Plan plan, Personne personne, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres, boolean dontUseOptimisation)
    {
    	if(actualMove.size() >= ConfigPartie.list_moments_partie.size())
    	{
			List<Reservation> moveToAdd = new ArrayList<>(actualMove);
    		allPossibleMove.add(moveToAdd);
    		return;
    	}

    	Moment present = ConfigPartie.list_moments_partie.get(actualMove.size());
    	for(Lieu lieu : plan.list_lieu)
    	{
    		if(actualMove.size() == 0)
    		{
    			actualMove.add(new Reservation(lieu, personne, present));

    			if(dontUseOptimisation || isMoveValid(actualMove, liste_indices_depart, liste_indices_autres))
    				getAllPossibleMoveForAPersonRecursive(allPossibleMove, actualMove, plan, personne, liste_indices_depart, liste_indices_autres, dontUseOptimisation);
    			
    			actualMove.remove(actualMove.size() - 1);
    		}
    		else
    		{
    			if(lieu.lien.contains(actualMove.get(actualMove.size() - 1).lieu.piece))
    			{
        			actualMove.add(new Reservation(lieu, personne, present));

        			if(dontUseOptimisation || isMoveValid(actualMove, liste_indices_depart, liste_indices_autres))
        				getAllPossibleMoveForAPersonRecursive(allPossibleMove, actualMove, plan, personne, liste_indices_depart, liste_indices_autres, dontUseOptimisation);
        			
        			actualMove.remove(actualMove.size() - 1);
    			}
    		}
    	}
    }
    
    public static boolean isMoveValid(List<Reservation> actualMove, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres)
    {
    	for(Indice_P_L_M indiceToTest : liste_indices_depart)
    	{
    		for(Reservation reservationToTest : actualMove)
    		{
    			if(indiceToTest.personne == reservationToTest.personne && indiceToTest.moment == reservationToTest.moment && indiceToTest.piece != reservationToTest.lieu.piece)
    				return(false);
    		}
    	}
    	
    	for(Indice indiceToTest : liste_indices_autres)
    	{
			switch(indiceToTest.typeIndice)
			{
			case P_L_M:
	    		for(Reservation reservationToTest : actualMove)
	    		{
        			if(((Indice_P_L_M)indiceToTest).personne == reservationToTest.personne && ((Indice_P_L_M)indiceToTest).moment == reservationToTest.moment && ((Indice_P_L_M)indiceToTest).piece != reservationToTest.lieu.piece)
        				return(false);
	    		}
				break;
			case x_P_L:
				int compteur = 0;
	    		for(Reservation reservationToTest : actualMove)
	    		{
	    			if(((Indice_x_P_L)indiceToTest).personne == reservationToTest.personne && ((Indice_x_P_L)indiceToTest).piece == reservationToTest.lieu.piece)
	    				compteur++;
	    		}
	    		if(compteur > ((Indice_x_P_L)indiceToTest).combien)
	    			return(false);
				break;
			case x_L_M:
	    		for(Reservation reservationToTest : actualMove)
	    		{
	    			if(((Indice_x_L_M)indiceToTest).moment == reservationToTest.moment && ((Indice_x_L_M)indiceToTest).piece == reservationToTest.lieu.piece && ((Indice_x_L_M)indiceToTest).combien == 0)
        				return(false);
	    		}
				break;
			case x_Ldiff_P:
				List<Piece> listPieceDiff = new ArrayList<Piece>();
	    		for(Reservation reservationToTest : actualMove)
	    		{
	    			if(((Indice_x_Ldiff_P)indiceToTest).personne == reservationToTest.personne)
	    				if(!listPieceDiff.contains(reservationToTest.lieu.piece))
	    					listPieceDiff.add(reservationToTest.lieu.piece);
	    		}
	    		if(listPieceDiff.size() > ((Indice_x_Ldiff_P)indiceToTest).combien)
	    			return(false);
				break;
			case x_Ldiff_M:
				break;
			case x_L:
				break;
			case P_P_M:
				break;
			case P_P_L:
				break;
			}
    	}
    	return(true);
    }
    
    @Override
    public String toString() {
        StringBuilder s= new StringBuilder();
        for (Reservation reservation : solution){
            s.append(reservation.toString());
        }
        return s.toString();
    }


    public void export(){


    }
}
