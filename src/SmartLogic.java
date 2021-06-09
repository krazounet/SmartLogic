
public final class SmartLogic {

    public static String repertoire = "";

    public static void main(String[] args) {
        String nom_export = ConfigPartie.nom_export;
        Problem p = new Problem();
        p.export(nom_export, false);
        p.export("Expert "+nom_export, true);
        System.out.println(p);
        System.out.println("Nom export : "+nom_export);
    }

}
