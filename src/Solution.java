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
        	stats.put("Res_" + reservation.personne.name() + reservation.moment.name() + reservation.lieu.piece.name(), "Yes");
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
					stats.put("Met_" + resa1.personne.name() + resa2.personne.name() + resa1.lieu.piece.name(), resa1.moment.name());
					// Deux personnes dans le m�me lieu inconnu � un moment connu : "Met_P1P2MM" = "L"
					stats.put("Met_" + resa1.personne.name() + resa2.personne.name() + resa1.moment.name(), resa1.lieu.piece.name());
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
            	stats.put("Grp_" + present.name() + piece.name(), "" + compteur);
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
        	stats.put("OcL_" + piece.name(), "" + compteur);
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
        	stats.put("OcLM_" + present.name(), "" + listDiffPieces.size());
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
        	stats.put("OcLP_" + personne.name(), "" + listDiffPieces.size());
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
            	stats.put("OcPinL_" + personne.name() + piece.name(), "" + compteur);
        	}
        }


    }
    
    public static List<Solution> getAllValidSolutionFromListIndicesDepart(Plan plan, List<Indice_P_L_M> liste_indices_depart)
    {
    	List<Solution> listSolutions = new ArrayList<>();
    	
    	List<List<List<Reservation>>> allPossibleMoves = new ArrayList<>();
    	
    	for(Personne personne : ConfigPartie.list_personnes_partie)
    	{
        	List<List<Reservation>> allPossibleMoveIndiv = getAllPossibleMoveForAPerson(plan, personne);
        	
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

    	addAllSolutions(listSolutions, allPossibleMoves, listIndex);
    	
    	return(listSolutions);
    }
    
    public static void addAllSolutions(List<Solution> listSolutions, List<List<List<Reservation>>> allPossibleMoves, List<Integer> listIndex)
    {
    	List<Reservation> listToAdd = new ArrayList<>();
    	
    	for(int idPerso = 0; idPerso < listIndex.size(); idPerso++)
    	{
    		listToAdd.addAll(allPossibleMoves.get(idPerso).get(listIndex.get(idPerso)));
    	}

    	Solution solutionToAdd = new Solution(listToAdd);
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
    	    	listSolutions.add(solutionToAdd);

    	    	posToIncrement = listIndex.size() - 1;
    		}
    	} while(posToIncrement != -1);
    }

    public static List<List<Reservation>> getAllPossibleMoveForAPerson(Plan plan, Personne personne)
    {
    	List<List<Reservation>> allPossibleMove = new ArrayList<>();

    	getAllPossibleMoveForAPersonRecursive(allPossibleMove, new ArrayList<>(), plan, personne);
    	
        return(allPossibleMove);
    }
    
    public static void getAllPossibleMoveForAPersonRecursive(List<List<Reservation>> allPossibleMove, List<Reservation> actualMove, Plan plan, Personne personne)
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

    			getAllPossibleMoveForAPersonRecursive(allPossibleMove, actualMove, plan, personne);
    			
    			actualMove.remove(actualMove.size() - 1);
    		}
    		else
    		{
    			if(lieu.lien.contains(actualMove.get(actualMove.size() - 1).lieu.piece))
    			{
        			actualMove.add(new Reservation(lieu, personne, present));

        			getAllPossibleMoveForAPersonRecursive(allPossibleMove, actualMove, plan, personne);
        			
        			actualMove.remove(actualMove.size() - 1);
    			}
    		}
    	}
    }
    
    @Override
    public String toString() {
        String s="";
        for (Reservation reservation : solution){
            s=s+reservation.toString();
        }
        return s;
    }


    public void export(){


    }
}
