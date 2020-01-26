import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class GreedySearch {

    static LinkedList<Mesto> mesta=new LinkedList<>();


    public static void main(String[] args) throws IOException{
        double maxCap=read("Problem2.txt");
        dijkstra(mesta.get(0), 0);

        for(int i=0; i<mesta.get(19).shortestPath.size();i++){
            System.out.println(mesta.get(19).shortestPath.get(i).index);
        }
    }

    private static double read(String s) throws IOException {

        File file = new File(s);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String [] line1 = br.readLine().split(",");
        int steviloMest = Integer.parseInt(line1[0]);
        double maxCap = Double.parseDouble(line1[1]);

        for (int i = 0; i<steviloMest; i++){
            String [] line = br.readLine().split(",");
            Mesto m = new Mesto(Integer.parseInt(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]),  Double.parseDouble(line[3]),  Double.parseDouble(line[4]),  Double.parseDouble(line[5]));
            mesta.add(m);
        }

        String st;
        while ((st = br.readLine()) != null){
            String [] line = st.split(",");
            Mesto m1 = mesta.get(Integer.parseInt(line[0])-1);
            Mesto m2 = mesta.get(Integer.parseInt(line[1])-1);
            Razdalja r = new Razdalja(Double.parseDouble(line[4]), Double.parseDouble(line[2]));
            if (Integer.parseInt(line[3]) == 0){
                if (m1.sosedje.get(m2.index) == null) {
                    m1.sosedje.put(m2.index, new LinkedList<Razdalja>());
                }
                m1.sosedje.get(m2.index).add(r);
                m1.sosedjeIndex.add(m2.index);
                if (m2.sosedje.get(m1.index) == null) {
                    m2.sosedje.put(m1.index, new LinkedList<Razdalja>());
                }
                m2.sosedje.get(m1.index).add(r);
                m2.sosedjeIndex.add(m1.index);
            }
            else {
                if (m1.sosedje.get(m2.index) == null) {
                    m1.sosedje.put(m2.index, new LinkedList<Razdalja>());
                }
                m1.sosedje.get(m2.index).add(r);
                m1.sosedjeIndex.add(m2.index);
            }
        }

        return maxCap;

    }

    private static Mesto getLowestDistanceMesto(Set<Mesto> neobravnavani) {
        Mesto lowestDistanceMesto = new Mesto();
        double lowestDistance = Double.MAX_VALUE;
        for (Mesto mesto: neobravnavani) {
            double nodeDistance = mesto.getDistSource();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceMesto = mesto;
            }
        }
        return lowestDistanceMesto;
    }
    private static void CalculateMinimumDistance(Mesto evaluationMesto, double razdalja, Mesto sourceMesto) {
        double sourceDistance = sourceMesto.getDistSource();
        if (sourceDistance + razdalja < evaluationMesto.getDistSource()) {
            evaluationMesto.setDistSource(sourceDistance + razdalja);
            LinkedList<Mesto> shortestPath = new LinkedList<>(sourceMesto.getShortestPath());
            shortestPath.add(sourceMesto);
            evaluationMesto.setShortestPath(shortestPath);
        }
    }
    public static void dijkstra(Mesto trenutno, double teza){
        Mesto source = trenutno;
        source.setDistSource(0);
        HashSet<Mesto> obravnavani=new HashSet<>();
        HashSet<Mesto> neobravnavani=new HashSet<>();

        neobravnavani.add(source);
        while(neobravnavani.size()!=0){
            Mesto current = new Mesto();
            current=getLowestDistanceMesto(neobravnavani);
            neobravnavani.remove(current);
            for(Map.Entry<Integer, LinkedList<Razdalja>> sosedRazd:
                current.getSosedje().entrySet()){
                Mesto sosed = mesta.get(sosedRazd.getKey() - 1);
                double razdalja = Double.MAX_VALUE;
                for (int i = 0; i < sosedRazd.getValue().size(); i++) {
                    if (sosedRazd.getValue().get(i).velikost < razdalja && teza <= sosedRazd.getValue().get(i).kapaciteta)
                        razdalja = sosedRazd.getValue().get(i).velikost;
                }
                if (!obravnavani.contains(sosed)) {
                    CalculateMinimumDistance(sosed, razdalja, current);
                    neobravnavani.add(sosed);
                }

            }
            obravnavani.add(current);

        }

    }

}
