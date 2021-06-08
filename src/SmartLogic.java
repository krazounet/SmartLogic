
public final class SmartLogic {

    public static String repertoire = "";

    public static void main(String[] args) {
        Problem p = new Problem();
        p.export(ConfigPartie.nom_export, false);
        p.export("Expert "+ConfigPartie.nom_export, true);
        System.out.println(p);
    }

}
