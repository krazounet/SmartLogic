import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {

    Plan plan;
    List<Indice> list_indices;
    Solution solution;

    public Problem() {
        List<Indice> liste_indices_retenus =new ArrayList<>();
    	List<Indice_P_L_M> liste_indices_retenus_depart = new ArrayList<Indice_P_L_M>();
    	List<Indice> liste_indices_retenus_autres = new ArrayList<Indice>();

        boolean monoSolution = false;
        
        do
        {
        plan =new Plan();
        solution = new Solution(plan);

        //algo indice ici

        List<Indice_P_L_M> liste_indices_depart = new ArrayList<>(Indice_P_L_M.all_PLM(solution));
        Collections.shuffle(liste_indices_depart);

        List<Indice> liste_tous_les_indices =new ArrayList<>();
        liste_tous_les_indices.addAll(Indice_x_P_L.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L_M.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L.all_xL(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_M.all_xLdiffM(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_P.all_xLdiffP(solution));
        liste_tous_les_indices.addAll(Indice_P_P_M.all_PPM(solution));
        liste_tous_les_indices.addAll(Indice_P_P_L.all_PPL(solution));

        Collections.shuffle(liste_tous_les_indices);

        liste_indices_retenus.clear();
    	liste_indices_retenus_depart.clear();
    	liste_indices_retenus_autres.clear();

        	liste_indices_retenus.clear();

        	liste_indices_retenus_depart.clear();

        	for (int index_indice = 0; index_indice<ConfigPartie.nombre_positions_depart;index_indice++){
        		liste_indices_retenus_depart.add(liste_indices_depart.get(index_indice));
	        }
        	
        	liste_indices_retenus.addAll(liste_indices_retenus_depart);
        
        	liste_indices_retenus_autres.clear();

	        for (int index_indice = 0; index_indice<ConfigPartie.nombre_indices;index_indice++){
	        	liste_indices_retenus_autres.add(liste_tous_les_indices.get(index_indice));
	        }
	        
	        liste_indices_retenus.addAll(liste_indices_retenus_autres);
	        
	        // Debuggage pour voir si les indices sont tous ok dans la solution de base :
			for(Indice indiceToTest : liste_indices_retenus)
			{
				if(!indiceToTest.check(solution))
				{
					indiceToTest.check(solution);
					break;
				}
			}

	        int nbSolutions = Solveur.getNbSolutions(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
	        System.out.println("Nb solutions : " + nbSolutions);
	        if(nbSolutions == 1)
            {
            	monoSolution = true;
            	break;
            }
        } while(!monoSolution);

        Solveur.detectUselessIndices(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
        list_indices=       liste_indices_retenus;

    }

    @Override
    public String toString() {
        String retour = "Problem{" +
                "plan=" + plan.toString() +""+
                //", indicelist=" + indicelist +
                ", solution=\n" + solution.toString() +
                "}\n Indices : \n";
        for (Indice ind : list_indices){
            retour=retour+ind.description+"\n";
        }
        return retour;
    }
    public void export(String filename, boolean onlyUsefull){

        BufferedImage fond = DrawTools.getImage(SmartLogic.repertoire+"Image\\FondSmartLogic.png");
        //BufferedImage rosace = DrawTools.getImage(SmartLogic.repertoire+"export\\rosace.png");
        DrawTools.drawImageTransformed(fond.getGraphics(),plan.export(),500,500,0,100);

        int ybase=1050;
        for(int i=0;i<list_indices.size();i++){
            Indice ind = list_indices.get(i);
            if(list_indices.get(i).usefull)
            	DrawTools.drawText(fond,"*" + ind.description + "*",500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            else
            {
            	if(!onlyUsefull)
            		DrawTools.drawText(fond,ind.description,500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            }
            BufferedImage img_ind=ind.export();
            if (img_ind!=null){
                DrawTools.drawImageTransformed(fond.getGraphics(),img_ind,1800,1200+(i*100),0,100);
            }
        }

        BufferedImage tableau = DrawTools.getImage(SmartLogic.repertoire+"Image\\4m"+ConfigPartie.nombre_lieu+"L.png");
        DrawTools.drawImageTransformed(fond.getGraphics(),tableau,1800,850,0,100);

        DrawTools.saveFile(fond,SmartLogic.repertoire+"export\\" + filename + ".png");



    }

}
