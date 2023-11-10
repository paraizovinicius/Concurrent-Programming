import java.util.Random;

public class TriRapide {
    static final int taille = 1000000;
    static final int [] tableau = new int[taille];
    static final int borne = 10*taille;

    private static void echangerElements(int [] t, int m, int n){
        int temp = t[m];
        t[m] = t[n];
        t[n] = temp;
    }

    private static int partitionner(int [] t, int debut, int fin) {
        int v = t[fin];                         // choix(arbitraire) du pivot
        int place = debut;                      // place du pivot, à droite des elements deplaces
        for (int i = debut; i<fin; i++){        // parcours du reste du tableau
            if(t[i] < v){                       // cette valeur t[i] doit etre à droite du pivot
                echangerElements(t, i, place);  // on le met à sa place
                place++;                        // on met à jour la place du pivot
            }
        }
        echangerElements(t, place, fin); 
        return place;
    }

    private static void trierRapidement(int[] t, int debut, int fin){
        if(debut < fin){
            int p = partitionner(t, debut, fin);
            trierRapidement(t, debut, p-1);
            trierRapidement(t, p+1, fin);
        }
    }

    private static void afficher(int[] t, int debut, int fin){
        for(int i = debut; i<= debut+3; i++){
            System.out.println("_" + t[i]);
        }
        System.out.println("...");
        for(int i = fin-3; i<=fin ; i++){
            System.out.println("_" + t[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Random alea = new Random();
        for(int i=0; i<taille; i++){ // Remplissage aleatoire du tableau
            tableau[i] = alea.nextInt(2*borne) - borne;
        }
        System.out.println("Tableau initial : ");
        afficher(tableau, 0, taille-1); // Affiche le tableau à trier
    

    long debutDuTri = System.nanoTime();
    trierRapidement(tableau, 0, taille-1); //Tri du tableau
    long finDuTri = System.nanoTime();
    long dureeDuTri = (finDuTri - debutDuTri) / 1000000;

    System.out.println("Tableau trié : ");
    afficher(tableau, 0, taille-1); // Affiche le tableau obtenu
    System.out.println("Obtenu en " + dureeDuTri + " millisecondes.");

    }
}
