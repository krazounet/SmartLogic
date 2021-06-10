import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
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


        boolean monoSolution = false;
        boolean exportable = false;//passe a true si aucun indice ne s'affiche dans la meme case


        do
        {
            monoSolution = false;
            exportable = false;
            //algo indice ici
            while(!exportable){
            solution = new Solution(plan);

            List<Indice_P_L_M> liste_indices_depart = new ArrayList<>(Indice_P_L_M.all_PLM(solution));
            List<Indice> liste_tous_les_indices =new ArrayList<>();
            liste_tous_les_indices.addAll(Indice_x_P_L.all_xPL(solution));
            liste_tous_les_indices.addAll(Indice_x_L_M.all_xPL(solution));
            liste_tous_les_indices.addAll(Indice_x_L.all_xL(solution));
            liste_tous_les_indices.addAll(Indice_x_Ldiff_M.all_xLdiffM(solution));
            liste_tous_les_indices.addAll(Indice_x_Ldiff_P.all_xLdiffP(solution));
            liste_tous_les_indices.addAll(Indice_P_P_M.all_PPM(solution));
            liste_tous_les_indices.addAll(Indice_P_P_L.all_PPL(solution));

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
	        list_indices = liste_indices_retenus;
            exportable = isListIndiceExportable();
	        System.out.println("Indice integrable au tableau :"+isListIndiceExportable());
            }

	        int nbSolutions = Solveur.getNbSolutions(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
	        if(nbSolutions == 1)   {
            	monoSolution = true;
                System.out.println("Mono solution trouvee");
            }else {
                monoSolution = false;
                System.out.println("Mono solution non trouvee");
            }
        } while(!monoSolution);

        if (ConfigPartie.find_optimisation) {
            Solveur.detectUselessIndices(plan, liste_indices_retenus_depart, liste_indices_retenus_autres);
        }



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
    
    public void exportSolution(String filename)
    {
        BufferedImage tableau = this.createTableau(false, true);
        
        for(int m = 0; m < ConfigPartie.list_moments_partie.size(); m++)
        {
        	Moment ceMoment = ConfigPartie.list_moments_partie.get(m);
        	for(int p = 0; p < ConfigPartie.list_pieces_partie.size(); p++)
        	{
        		Piece cettePiece = ConfigPartie.list_pieces_partie.get(p);
        		List<Personne> listPersonnesHere = new ArrayList<Personne>();
        		
        		for(Reservation reservationToTest : solution.solution)
        		{
        			if(reservationToTest.moment == ceMoment && reservationToTest.lieu.piece == cettePiece)
        			{
        				listPersonnesHere.add(reservationToTest.personne);
        			}
        		}
        		
        		int centerCaseX = 250 + m * 100;
        		int centerCasey = 150 + p * 100;
        		
        		BufferedImage caseToDraw = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        		
				List<List<Point>> listListPosCharacter = Arrays.asList(
						Arrays.asList(new Point(2, 2)),
						Arrays.asList(new Point(1, 2), new Point(3, 2)),
						Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 3)),
						Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 3), new Point(3, 3)),
						Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(2, 2), new Point(1, 3), new Point(3, 3)),
						Arrays.asList(new Point(1, 1), new Point(3, 1), new Point(1, 2), new Point(3, 2), new Point(1, 3), new Point(3, 3))
						);
				
				for(int c = 0; c < listPersonnesHere.size(); c++)
					DrawTools.drawImageTransformed(caseToDraw.getGraphics(), DrawTools.getImage("Image/" + listPersonnesHere.get(c).name() + ".png"), 25 * listListPosCharacter.get(listPersonnesHere.size() - 1).get(c).x, 25 * listListPosCharacter.get(listPersonnesHere.size() - 1).get(c).y, 0, 50);
				
				DrawTools.drawImageCenter(tableau.getGraphics(), caseToDraw, centerCaseX, centerCasey);

        	}
        }
        
        DrawTools.saveFile(tableau, SmartLogic.repertoire+"export/" + filename + ".png");
    }
    
    public void exportVierge(String filename)
    {
        BufferedImage tableau = this.createTableau(false, false);
        
        DrawTools.saveFile(tableau, SmartLogic.repertoire+"export/" + filename + ".png");
    }
    
    public void export(String filename, boolean onlyUsefull){

        BufferedImage fond = DrawTools.getImage(SmartLogic.repertoire+"Image\\FondSmartLogic.png");
        DrawTools.drawImageTransformed(fond.getGraphics(),plan.export(),500,500,0,100);
        BufferedImage tableau = this.createTableau(true, true);
        int ybase=1050;
        int y_hors_tableau=1300;//positionnnement de depart pour les infos qui ne rentre pas dans le tableau
        for(int i=0;i<list_indices.size();i++){
            Indice ind = list_indices.get(i);
            BufferedImage img_ind=ind.export();
            if(ind.usefull)
            	DrawTools.drawText(fond,"*" + ind.description + "*",500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            else
            {
            	if(!onlyUsefull)
            		DrawTools.drawText(fond,ind.description,500,ybase+(i*30),"Arial", Color.BLACK,30,0);
            }

            if ((ind.localisationIndice == LocalisationIndice.HORS_TABLEAU)&&((ind.usefull || !onlyUsefull))){
                DrawTools.drawImageTransformed(fond.getGraphics(),img_ind,1800,y_hors_tableau,0,100);
                y_hors_tableau=y_hors_tableau+50;
            }else{
                if  (ind.usefull || !onlyUsefull){
                    DrawTools.drawImageTransformed(tableau.getGraphics(),img_ind,ind.getCoordonnee().x + 100,ind.getCoordonnee().y,0,100);
                }
            }
        }


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
                   // System.out.println("indice au meme endroit dans le tableau : "+emplacement);
                    return false;
                }
                else{
                    emplacements.add(emplacement);
                }
            }

        }
        return true;
    }

    public BufferedImage createTableau (boolean displayBorder, boolean displayRoads){
        //image carre
        BufferedImage img_carre = DrawTools.getImage(SmartLogic.repertoire+"Image\\carre.png");
        BufferedImage img_carre_bord = DrawTools.getImage(SmartLogic.repertoire+"Image\\carre_bord.png");
        //chaque element du tableau fait 100*100 en pixel.
        BufferedImage image_tableau = new BufferedImage((ConfigPartie.nombre_moment+2)*100 + 100,(ConfigPartie.nombre_lieu+2)*100,BufferedImage.TYPE_INT_ARGB);
        Graphics graph_tableau= image_tableau.getGraphics();
        
        if(displayRoads)
        {
	        Graphics2D g2 = (Graphics2D) graph_tableau;
	        g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(7));
	        for(Conexion conexionToDraw : plan.list_conexions)
	        {
	        	int startY1 = ConfigPartie.list_pieces_partie.indexOf(conexionToDraw.Piece1);
	        	int startY2 = ConfigPartie.list_pieces_partie.indexOf(conexionToDraw.Piece2);
	        	if(startY1 > startY2)
	        	{
	        		int temp = startY1;
	        		startY1 = startY2;
	        		startY2 = temp;
	        	}
	        	int gap = startY2 - startY1;
	
	        	g2.drawArc((120 - (gap * 10)) - (10 * gap),  150 + startY1 * 100, (120 - (gap * 10)) + (10 * gap), gap * 100, 90, 180);
	        }
        }

        //la première ligne est donc les moments
        int x_moment=250;
        int y_moment=055;
        for (Moment moment : ConfigPartie.list_moments_partie){
            BufferedImage img_moment = DrawTools.getImage(SmartLogic.repertoire+"Image\\"+moment+".png");
            DrawTools.drawImageCenter(graph_tableau,img_moment,x_moment,y_moment);
            x_moment=x_moment+100;
        }
        //les lignes suivantes sont donc pour les lieus.
        int y_lieu = 150;
        for (Piece piece : ConfigPartie.list_pieces_partie){
            int x_lieu = 150;

            BufferedImage img_piece = DrawTools.getImage(SmartLogic.repertoire+"Image\\"+piece+".png");
            DrawTools.drawImageCenter(graph_tableau,img_piece,x_lieu,y_lieu);
            for (int num_moment=0 ; num_moment<=ConfigPartie.list_moments_partie.size() ; num_moment++){
                x_lieu=x_lieu+100;
                if(num_moment != ConfigPartie.list_moments_partie.size())
                	DrawTools.drawImageCenter(graph_tableau,img_carre,x_lieu,y_lieu);
                else
                {
                	if(displayBorder)
                		DrawTools.drawImageTransformed(graph_tableau, img_carre_bord, x_lieu, y_lieu, 270, 100);
                }
            }
            y_lieu = y_lieu+100 ;//l increment de y_lieu pour la ligne suivante.
        }
        //derniere ligne
        if(displayBorder)
        {
	        int x_ligne=250;
	        int y_ligne=((ConfigPartie.list_pieces_partie.size()+1)*100)+50;
	        for (int num_moment=0 ; num_moment<ConfigPartie.list_moments_partie.size() ; num_moment++){
	        	DrawTools.drawImageTransformed(graph_tableau, img_carre_bord, x_ligne, y_ligne, 0, 100);
	            x_ligne=x_ligne+100;
	        }
        }

        return image_tableau;
    }

}
