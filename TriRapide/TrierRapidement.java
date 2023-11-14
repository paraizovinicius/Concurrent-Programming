import java.util.Random;
import java.util.concurrent.*;

public class TrierRapidement {

    private static int threshold;
    static final int taille = 100000;
    static final int[] tableau = new int[taille];
    static final int[] tableau2 = new int[taille];
    static final int borne = 10 * taille;


    private static void afficher(int[] t, int debut, int fin) {
        for (int i = debut; i <= debut + 3 && i < t.length; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println("...");
        for (int i = fin - 3; i <= fin && i < t.length; i++) {
            System.out.println("_" + t[i]);
        }
        System.out.println();
    }

    private static void echangerElements(int[] tableau, int i, int j) {
        int temp = tableau[i];
        tableau[i] = tableau[j];
        tableau[j] = temp;
    }

    private static int partitionner(int[] t, int debut, int fin) {
        int v = t[fin];
        int place = debut;
        for (int i = debut; i < fin; i++) {
            if (t[i] < v) {
                echangerElements(t, i, place);
                place++;
            }
        }
        echangerElements(t, place, fin);
        return place;
    }

    private static void trierRapidement(int[] t, int debut, int fin) {
        if (debut < fin) {
            int p = partitionner(t, debut, fin);
            trierRapidement(t, debut, p - 1);
            trierRapidement(t, p + 1, fin);
        }
    }


    private static boolean Comparer(int[] t1, int[] t2) {
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] != t2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Random alea = new Random();
        for (int i = 0; i < taille; i++) {
            tableau[i] = alea.nextInt(2 * borne) - borne;
            tableau2[i] = tableau[i];
        }

        threshold = taille/100;
        long debutDuTri = System.nanoTime();
        parallelQuicksort(tableau, 0, taille - 1);
        long finDuTri = System.nanoTime();
        long dureeDuTriRapide = (finDuTri - debutDuTri);

        long debutDuTriA = System.nanoTime();
        trierRapidement(tableau2, 0, taille - 1);
        long finDuTriA = System.nanoTime();
        long dureeDuTri = (finDuTriA - debutDuTriA);


        afficher(tableau, 0, taille - 1); // Affiche le tableau obtenu
        System.out.println("Parallel obtenu en " + dureeDuTriRapide + " millisecondes.");
        System.out.println("Sequence obtenu en " + dureeDuTri + " millisecondes.");
        System.out.println("Les tableaux sont identiques: "+ Comparer(tableau, tableau2));
    }

    private static void parallelQuicksort(int[] tableau, int low, int high) {
        ForkJoinPool pool = new ForkJoinPool(4); // Ce sont 4 le chiffre de threads
        pool.invoke(new PQuicksort(tableau, low, high));
        pool.shutdown();
    }

    private static class PQuicksort extends RecursiveAction {
        private int[] tableau;
        private int low, high;

        public PQuicksort(int[] tableau, int low, int high) {
            this.tableau = tableau;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if ((high - low) < threshold) {
                trierRapidement(tableau, low, high);
            } else {
                int i = low, j = high;
                int pivot = tableau[low + (high - low) / 2]; // un autre methode pour trouver le pivot

                while (i <= j) {
                    while (tableau[i] < pivot) {
                        i++;
                    }

                    while (tableau[j] > pivot) {
                        j--;
                    }

                    if (i <= j) {
                        echangerElements(tableau, i, j);
                        i++;
                        j--;
                    }
                }

                if (low < j) {
                    ForkJoinTask.invokeAll(new PQuicksort(tableau, low, j));
                }

                if (i < high) {
                    ForkJoinTask.invokeAll(new PQuicksort(tableau, i, high));
                }
            }
        }
    }

}
