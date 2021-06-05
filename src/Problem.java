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
        List<Indice> liste_tous_les_indices =new ArrayList<>();
        liste_tous_les_indices.addAll(Indice_P_L_M.all_PLM(solution));
        liste_tous_les_indices.addAll(Indice_x_P_L.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L_M.all_xPL(solution));
        liste_tous_les_indices.addAll(Indice_x_L.all_xL(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_M.all_xLdiffM(solution));
        liste_tous_les_indices.addAll(Indice_x_Ldiff_P.all_xLdiffP(solution));


        Collections.shuffle(liste_tous_les_indices);

        List<Indice> liste_indices_retenus =new ArrayList<>();
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
}
