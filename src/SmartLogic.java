
public final class SmartLogic {

    static String repertoire = "";
    static String nom_export = ConfigPartie.nom_export;

    public static void main(String[] args) {

        Problem p = new Problem();
//        p.exportVierge(nom_export + "_EMPTY");
        p.export(nom_export, false);
        p.export(nom_export + "_Expert", true);
        p.exportSolution(nom_export + "_SOL");
        System.out.println(p);
        System.out.println("Nom export : "+nom_export);
    }

}
