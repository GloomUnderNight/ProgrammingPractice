import java.io.*;
import java.util.*;

public class Main {

    
    public static void main(String[] args) {
        ArrayList<String> allStrings = new ArrayList<String>();
        ArrayList<Juice> allJuices =  new ArrayList<Juice>();
        try {
            readingFromFile(allStrings);
            juiceFilling(allJuices, allStrings);
            new FirstTaskThread(allStrings).start();
            new SecondTaskThread(allStrings).start();
            new ThirdTaskThread(allJuices).start();
        }
        catch (FileNotFoundException f){
            System.err.println("File \"juice.in\" not found!");
        }
    }
    
    private static void readingFromFile(ArrayList<String> allStrings) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("juice.in"));
        String tmpLine;
        while (sc.hasNextLine()){
            tmpLine = sc.nextLine();
            tmpLine = tmpLine.replaceAll("[^A-z ]", "").trim();
            allStrings.add(tmpLine);
        }
    }
    
    private static void juiceFilling(ArrayList<Juice> allJuices,  ArrayList<String> allStrings){
        for (String allString : allStrings) {
            allJuices.add(new Juice(allString));
        }
    }

    public static Comparator<String> getComparatorByAlphabet() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
    }

    public static Comparator<Juice> getComparatorByComponents() {
        return new Comparator<Juice>() {
            @Override
            public int compare(Juice o1, Juice o2) {
                if (o1.getNumberOfComponents() > o2.getNumberOfComponents()) return 1;
                if (o1.getNumberOfComponents() < o2.getNumberOfComponents()) return -1;
                return 0;
            }
        };
    }
}

