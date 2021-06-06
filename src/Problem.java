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

        List<Indice> liste_indices_retenus =new ArrayList<>();
        for (int index_indice = 0; index_indice<ConfigPartie.nombre_positions_depart;index_indice++){
            liste_indices_retenus.add(liste_indices_depart.get(index_indice));
        }
        for (int index_indice = 0; index_indice<ConfigPartie.nombre_indices;index_indice++){
            liste_indices_retenus.add(liste_tous_les_indices.get(index_indice));
        }

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
    public void export(){
        plan.export();
        BufferedImage fond = DrawTools.getImage(SmartLogic.repertoire+"Image\\FondSmartLogic.png");
        BufferedImage rosace = DrawTools.getImage(SmartLogic.repertoire+"export\\rosace.png");
        DrawTools.drawImageTransformed(fond.getGraphics(),rosace,500,500,0,100);

        int ybase=1050;
        for(int i=0;i<list_indices.size();i++){
            DrawTools.drawText(fond,list_indices.get(i).description,500,ybase+(i*30),"Arial", Color.BLACK,30,0);
        }

        BufferedImage tableau = DrawTools.getImage(SmartLogic.repertoire+"Image\\4m"+ConfigPartie.nombre_lieu+"L.png");
        DrawTools.drawImageTransformed(fond.getGraphics(),tableau,1800,850,0,100);

        DrawTools.saveFile(fond,SmartLogic.repertoire+"export\\problem.png");



    }

}
