import java.util.ArrayList;
import java.util.Scanner;


public class Juice {
    
    
    private int numberOfComponents = 0;
    private ArrayList<String> components = null;

    
    public Juice(String input) {
        components = new ArrayList<String>();
        Scanner sc = new Scanner(input);
        sc.useDelimiter("[ \\n\\r]");
        while (sc.hasNext()){
            components.add(sc.next().toLowerCase());
            numberOfComponents++;
        }
    }

    public int getNumberOfComponents() {
        return numberOfComponents;
    }

    
    public ArrayList<String> getComponents() {
        return components;
    }

    
    public void setComponents(ArrayList<String> components) {
        this.components = components;
        this.components.trimToSize();
        numberOfComponents = components.size();
    }
    
    
    
}
