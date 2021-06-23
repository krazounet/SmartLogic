
public final class SmartLogic {

    static String repertoire = "";
    static String nom_export = ConfigPartie.nom_export;

    public static void main(String[] args) {

    	int nbProblemWanted = 10;
    	for(int n = 0; n < nbProblemWanted; n++)
    	{
	        Problem p = new Problem();
	//        p.exportVierge(nom_export + "_EMPTY");
	        p.export(nom_export + n, false);
	        p.export(nom_export + n +  "_Expert", true);
	        p.exportSolution(nom_export + n + "_SOL");
	        System.out.println(p);
	        System.out.println("Nom export : " + nom_export + n);
    	}
    }

}
