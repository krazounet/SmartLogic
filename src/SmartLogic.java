
public final class SmartLogic {

    public static String repertoire = "";

    public static void main(String[] args) {
        String nom_export = ConfigPartie.nom_export;
        Problem p = new Problem();
        p.exportVierge(nom_export + "_EMPTY");
        p.export(nom_export, false);
        p.export(nom_export + "_Expert", true);
        p.exportSolution(nom_export + "_SOL");
        System.out.println(p);
        System.out.println("Nom export : "+nom_export);
    }

}
