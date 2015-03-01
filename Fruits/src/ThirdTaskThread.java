import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Gennady Stanchik.
 */

public class ThirdTaskThread extends Thread {

    private ArrayList<Juice> allJuices;

    public ThirdTaskThread(ArrayList<Juice> allJuices) {
        this.allJuices = allJuices;
    }

    public void run(){
        fillingOut3(allJuices);
    }

    private static void fillingOut3(ArrayList<Juice> allJuices) {
        Collections.sort(allJuices, Main.getComparatorByComponents());
        allJuices.trimToSize();
        int counter = allJuices.size();
        for (int i = 0; i < allJuices.size(); i++){
            for (int j = i + 1; j < allJuices.size(); j++){
                if (allJuices.get(j).getComponents().containsAll(allJuices.get(i).getComponents())){
                    counter--;
                    break;
                }
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("juice3.out"));
            out.write(Integer.toString(counter));
            out.close();
        }
        catch (IOException io){
            System.err.println("Error writing to file juice3.out");
        }
    }
}
