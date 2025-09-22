import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Question3 {

    public static void main(String[] args) {
        //criaremos um threadpool com 4 threads abaixo
        ExecutorService threadpool = Executors.newFixedThreadPool(4);


        while(!arretDuServeur){
            //iremos dar submit em uma thread que irá executar o código abaixo
        threadpool.submit(() -> {
            Requête r = accepterUneRequête();
            r.traiterUneRequête();
            
        });
        }
        
    }
    
}
