import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Enum.*;

public class Problem {

    Plan plan;
    List<Indice> list_indices;
    Solution solution;

    public Problem() {
        List<Indice> liste_indices_retenus =new ArrayList<>();
    	List<Indice_P_L_M> liste_indices_retenus_depart = new ArrayList<>();
    	List<Indice> liste_indices_retenus_autres = new ArrayList<>();


        plan =new Plan();
        solution = new Solution(plan);

        //algo indice ici

        List<Indice_P_L_M> liste_indices_depart = new ArrayList<>(Indice_P_L_M.all_PLM(solution));
        List<Indice> liste_tous_les_indices =new ArrayList<>();
        liste_tous_les_indices.addAll(Indice_x_P_L.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L_M.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L.all_xL(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_M.all_xLdiffM(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_P.all_xLdiffP(solution));
        liste_tous_les_indices.addAll(Indice_P_P_M.all_PPM(solution));
        liste_tous_les_indices.addAll(Indice_P_P_L.all_PPL(solution));

        boolean monoSolution = false;

        do
        {
            //Melange des indices de references
            Collections.shuffle(liste_indices_depart);
            Collections.shuffle(liste_tous_les_indices);

            liste_indices_retenus.clear();
    	    liste_indices_retenus_depart.clear();
    	    liste_indices_retenus_autres.clear();

        	for (int index_indice = 0; index_indice<ConfigPartie.nombre_positions_depart;index_indice++){
        		liste_indices_retenus_depart.add(liste_indices_depart.get(index_indice));
	        }
        	liste_indices_retenus.addAll(liste_indices_retenus_depart);

	        for (int index_indice = 0; index_indice<ConfigPartie.nombre_indices;index_indice++){
	        	liste_indices_retenus_autres.add(liste_tous_les_indices.get(index_indice));
	        }
	        liste_indices_retenus.addAll(liste_indices_retenus_autres);

	        int nbSolutions = Solveur.getNbSolutions(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
	        if(nbSolutions == 1)   {
            	monoSolution = true;
                System.out.println("Mono solution trouvee");
            }else {
                System.out.println("Mono solution non trouvee");
            }
        } while(!monoSolution);

        Solveur.detectUselessIndices(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
        list_indices=       liste_indices_retenus;

        System.out.println("Indice integrable au tableau :"+isListIndiceExportable());
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
            if(ind.usefull)
            	DrawTools.drawText(fond,"*" + ind.description + "*",500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            else
            {
            	if(!onlyUsefull)
            		DrawTools.drawText(fond,ind.description,500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            }
            BufferedImage img_ind=ind.export();
            if (img_ind!=null && (ind.usefull || !onlyUsefull)){
                DrawTools.drawImageTransformed(fond.getGraphics(),img_ind,1800,1200+(i*100),0,100);
            }
        }

        //BufferedImage tableau = DrawTools.getImage(SmartLogic.repertoire+"Image\\4m"+ConfigPartie.nombre_lieu+"L.png");
        BufferedImage tableau = this.createTableau();
        DrawTools.drawImageTransformed(fond.getGraphics(),tableau,1800,850,0,100);

        DrawTools.saveFile(fond,SmartLogic.repertoire+"export\\" + filename + ".png");

    }

    //regarde si dans la liste des indices du problem si on devrait écrire plusieurs indices au meme endroit du tableau
    public boolean isListIndiceExportable (){
        List<String> emplacements=new ArrayList<>();
        for (Indice ind : list_indices){
            String emplacement = ind.getEmplacement();
            if (ind.localisationIndice != LocalisationIndice.HORS_TABLEAU){
                if (emplacements.contains(emplacement)){
                    System.out.println("indice au meme endroit dans le tableau : "+emplacement);
                    return false;
                }
            }
            else{
                emplacements.add(emplacement);
            }
        }
        return true;
    }

    public BufferedImage createTableau (){
        //image carre
        BufferedImage img_carre = DrawTools.getImage(SmartLogic.repertoire+"Image\\carre.png");
        //chaque element du tableau fait 100*100 en pixel.
        BufferedImage image_tableau = new BufferedImage((ConfigPartie.nombre_moment+2)*100,(ConfigPartie.nombre_lieu+2)*100,BufferedImage.TYPE_INT_ARGB);
        Graphics graph_tableau= image_tableau.getGraphics();

        //la première ligne est donc les moments
        int x_moment=150;
        int y_moment=050;
        for (Moment moment : ConfigPartie.list_moments_partie){
            BufferedImage img_moment = DrawTools.getImage(SmartLogic.repertoire+"Image\\"+moment+".png");
            DrawTools.drawImageCenter(graph_tableau,img_moment,x_moment,y_moment);
            x_moment=x_moment+100;
        }
        //les lignes suivantes sont donc pour les lieus.
        int y_lieu = 150;
        for (Piece piece : ConfigPartie.list_pieces_partie){
            int x_lieu = 50;

            BufferedImage img_piece = DrawTools.getImage(SmartLogic.repertoire+"Image\\"+piece+".png");
            DrawTools.drawImageCenter(graph_tableau,img_piece,x_lieu,y_lieu);
            for (int num_moment=0 ; num_moment<=ConfigPartie.list_moments_partie.size() ; num_moment++){
                x_lieu=x_lieu+100;
                DrawTools.drawImageCenter(graph_tableau,img_carre,x_lieu,y_lieu);
            }
            y_lieu = y_lieu+100 ;//l increment de y_lieu pour la ligne suivante.
        }
        //derniere ligne
        int x_ligne=150;
        int y_ligne=((ConfigPartie.list_pieces_partie.size()+1)*100)+50;
        for (int num_moment=0 ; num_moment<=ConfigPartie.list_moments_partie.size() ; num_moment++){
            DrawTools.drawImageCenter(graph_tableau,img_carre,x_ligne,y_ligne);
            x_ligne=x_ligne+100;
        }
        return image_tableau;
    }

}
