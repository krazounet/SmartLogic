import java.util.ArrayList;
import java.util.List;

public class Solveur
{
	public static int getNbSolutions(Plan plan, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres)
	{
		int nbSolutions = 0;
		
		List<Solution> listSolutions = Solution.getAllValidSolutionFromListIndices(plan, liste_indices_depart, liste_indices_autres);
		
		for(Solution solutionToTest : listSolutions)
		{
			boolean allIndicesOK = true;
			for(Indice indiceToTest : liste_indices_autres)
			{
				if(!indiceToTest.check(solutionToTest))
				{
					allIndicesOK = false;
					break;
				}
			}
			
			if(allIndicesOK)
			{
				nbSolutions++;
				if(nbSolutions > 1)
					break;
			}
		}
		
		return(nbSolutions);
	}

	public static void detectUselessIndices(Plan plan, List<Indice_P_L_M> liste_indices_depart, List<Indice> liste_indices_autres)
	{

		for(Indice indiceToCheck : liste_indices_autres)
			indiceToCheck.usefull = true;
		for(Indice indiceToCheck : liste_indices_depart)
			indiceToCheck.usefull = true;


		// Test sur les indices standards
		boolean canOptimize;
		do
		{
			canOptimize = false;
			
			for(int idToRemove = 0; idToRemove < liste_indices_autres.size(); idToRemove++)
			{
				if(!liste_indices_autres.get(idToRemove).usefull)
					continue;
				
				List<Indice> liste_indices_autres_optimize = new ArrayList<>();
				for(int idToAdd = 0; idToAdd < liste_indices_autres.size(); idToAdd++)
				{
					if(idToAdd != idToRemove && liste_indices_autres.get(idToAdd).usefull)
						liste_indices_autres_optimize.add(liste_indices_autres.get(idToAdd));
				}
				
				int nbSolutions = getNbSolutions(plan, liste_indices_depart, liste_indices_autres_optimize);
				
				if(nbSolutions == 1)
				{
					liste_indices_autres.get(idToRemove).usefull = false;
					canOptimize = true;
					System.out.println("Simplification : " + liste_indices_autres_optimize.size() + " indices");
					break;
				}
			}
		} while(canOptimize);

		if(ConfigPartie.optimize_start_indices)
		{
			// Test sur les indices de départ
			List<Indice> liste_indices_autres_optimize = new ArrayList<>();
			for(int idToAdd = 0; idToAdd < liste_indices_autres.size(); idToAdd++)
			{
				if(liste_indices_autres.get(idToAdd).usefull)
					liste_indices_autres_optimize.add(liste_indices_autres.get(idToAdd));
			}
			do
			{
				canOptimize = false;
				
				for(int idToRemove = 0; idToRemove < liste_indices_depart.size(); idToRemove++)
				{
					if(!liste_indices_depart.get(idToRemove).usefull)
						continue;
					
					List<Indice_P_L_M> liste_indices_depart_optimize = new ArrayList<>();
					for(int idToAdd = 0; idToAdd < liste_indices_depart.size(); idToAdd++)
					{
						if(idToAdd != idToRemove && liste_indices_depart.get(idToAdd).usefull)
							liste_indices_depart_optimize.add(liste_indices_depart.get(idToAdd));
					}
					
					int nbSolutions = getNbSolutions(plan, liste_indices_depart_optimize, liste_indices_autres_optimize);
					
					if(nbSolutions == 1)
					{
						liste_indices_depart.get(idToRemove).usefull = false;
						canOptimize = true;
						System.out.println("Simplification : " + liste_indices_depart_optimize.size() + " indices");
						break;
					}
				}
			} while(canOptimize);
		}
	}
}
