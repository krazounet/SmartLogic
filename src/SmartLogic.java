
public final class SmartLogic {

    public static String repertoire = "";

    public static void main(String[] args) {
        Problem p = new Problem();
        p.export("Problem", false);
        p.export("ProblemExpert", true);
        System.out.println(p);
    }

}
