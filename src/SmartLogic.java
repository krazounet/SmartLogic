
public final class SmartLogic {

    static String repertoire = "";
    static String nom_export = ConfigPartie.nom_export;

    public static void main(String[] args) {

        Problem p = new Problem();
        p.export(nom_export, false);
        p.export("Expert "+nom_export, true);
        System.out.println(p);
        System.out.println("Nom export : "+nom_export);
    }

}
