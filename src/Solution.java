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
        // Création des HashMaps des réservations
        for(Reservation reservation : solution)
        {
            // Liste des réservations : "Res_PPMML" = "Yes"
        	stats.put("Res_" + reservation.personne.name() + reservation.moment.name() + reservation.lieu.piece.name(), "Yes");
        }
        
        // Création des HashMaps qui concernent deux réservations
        for(int r1 = 0; r1 < solution.size(); r1++)
        {
        	Reservation resa1 = solution.get(r1);
        	for(int r2 = 0; r2 < solution.size(); r2++)
        	{
            	Reservation resa2 = solution.get(r2);
            	if(resa1 == resa2)
            		continue;
            	if(resa1.moment == resa2.moment && resa1.lieu == resa2.lieu)
            	{
                    // Deux personnes dans le même lieu connu à un moment inconnu : "Met_P1P2L" = "MM"
                	stats.put("Met_" + resa1.personne.name() + resa2.personne.name() + resa1.lieu.piece.name(), resa1.moment.name());
                    // Deux personnes dans le même lieu inconnu à un moment connu : "Met_P1P2MM" = "L"
                	stats.put("Met_" + resa1.personne.name() + resa2.personne.name() + resa1.moment.name(), resa1.lieu.piece.name());
            	}
        	}
        }
        
        // Création des HashMaps qui concernent un lieu connu et un moment connu
        for(Moment present : ConfigPartie.list_moments_partie)
        {
        	for(Piece piece : ConfigPartie.list_pieces_partie)
        	{
        		int compteur = 0;
        		for(Reservation reservation : solution)
        			if(reservation.moment == present && reservation.lieu.piece == piece)
        				compteur++;
        		// N personnes dans le même lieu connu à un moment connu : "Grp_MML" = "N"
            	stats.put("Grp_" + present.name() + piece.name(), "" + compteur);
        	}
        }
        
        // Création des HashMaps qui indiquent le nombre d'occurence des lieux
    	for(Piece piece : ConfigPartie.list_pieces_partie)
    	{
    		int compteur = 0;
    		for(Reservation reservation : solution)
    			if(reservation.lieu.piece == piece)
    				compteur++;
    		// Occurence d'un lieu dans le graphe : "OcL_L" = "N"
        	stats.put("OcL_" + piece.name(), "" + compteur);
    	}
    	
    	// Création des HashMaps qui indiquent le nombre de lieux différents à un moment donné
        for(Moment present : ConfigPartie.list_moments_partie)
        {
        	List<Piece> listDiffPieces = new ArrayList<Piece>();
    		for(Reservation reservation : solution)
    		{
    			if(reservation.moment == present)
    			{
    				if(!listDiffPieces.contains(reservation.lieu.piece))
    					listDiffPieces.add(reservation.lieu.piece);
    			}
    		}
    		// Nombre de lieu à un moment : "OcLM_MM" = "N"
        	stats.put("OcLM_" + present.name(), "" + listDiffPieces.size());
        }

        // Création des HashMaps qui indiquent le nombre de lieux différents visités par un personnage donné
        for(Personne personne : ConfigPartie.list_personnes_partie)
        {
        	List<Piece> listDiffPieces = new ArrayList<Piece>();
    		for(Reservation reservation : solution)
    		{
    			if(reservation.personne == personne)
    			{
    				if(!listDiffPieces.contains(reservation.lieu.piece))
    					listDiffPieces.add(reservation.lieu.piece);
    			}
    		}
    		// Nombre de lieu visité par une personne : "OcLP_PP" = "N"
        	stats.put("OcLP_" + personne.name(), "" + listDiffPieces.size());
        }

        // Création des HashMaps qui indiquent le nombre de fois qu'une personne à visité un lieu donné
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
        		// Nombre de fois qu'une personne donné à visité un lieu donné : "OcPinL_PPL" = "N"
            	stats.put("OcPinL_" + personne.name() + piece.name(), "" + compteur);
        	}
        }
    }
    
    public static List<Solution> getAllValidSolutionFromListIndicesDepart(Plan plan, List<Indice_P_L_M> liste_indices_depart)
    {
    	List<Solution> listSolutions = new ArrayList<Solution>();
    	
    	List<List<List<Reservation>>> allPossibleMoves = new ArrayList<List<List<Reservation>>>();
    	
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
    	
    	List<Integer> listIndex = new ArrayList<Integer>();
    	for(int i = 0; i < ConfigPartie.list_personnes_partie.size(); i++)
    		listIndex.add(0);

    	addAllSolutions(listSolutions, allPossibleMoves, listIndex);
    	
    	return(listSolutions);
    }
    
    public static void addAllSolutions(List<Solution> listSolutions, List<List<List<Reservation>>> allPossibleMoves, List<Integer> listIndex)
    {
    	List<Reservation> listToAdd = new ArrayList<Reservation>();
    	
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

    	    	listToAdd = new ArrayList<Reservation>();
    	    	
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
    	List<List<Reservation>> allPossibleMove = new ArrayList<List<Reservation>>();

    	getAllPossibleMoveForAPersonRecursive(allPossibleMove, new ArrayList<Reservation>(), plan, personne);
    	
        return(allPossibleMove);
    }
    
    public static void getAllPossibleMoveForAPersonRecursive(List<List<Reservation>> allPossibleMove, List<Reservation> actualMove, Plan plan, Personne personne)
    {
    	if(actualMove.size() >= ConfigPartie.list_moments_partie.size())
    	{
    		List<Reservation> moveToAdd = new ArrayList<Reservation>();
    		moveToAdd.addAll(actualMove);
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
