import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class GreedySearch {
    static LinkedList<Tovornjak> tOrganski=new LinkedList<>();
    static LinkedList<Tovornjak> tPlastika=new LinkedList<>();
    static LinkedList<Tovornjak> tPapir=new LinkedList<>();
    public static double [] vsaMestaOrgranski;
    public static double [] vsaMestaPlastika;
    public static double [] vsaMestaPapir;

    static LinkedList<Mesto> mesta=new LinkedList<>();
    static double maxCap;

    public static void main(String[] args) throws IOException{
        maxCap=read("Problem1.txt");
        vsaMestaOrgranski = new double [mesta.size()];
        vsaMestaPlastika = new double [mesta.size()];
        vsaMestaPapir = new double [mesta.size()];
        initVsaMesta();

        dijkstra(mesta.get(0), 0);
        greedyPot(1);
        greedyPot(2);
        greedyPot(3);
        System.out.println("Organski: ");
        for(int i=0;i<tOrganski.size();i++) {
            for (int j = 0; j < tOrganski.get(i).pot.size(); j++) {
                System.out.println(tOrganski.get(i).pot.get(j));
            }
            System.out.println();
        }
        System.out.println("Pobrano: "+jeCisto(1));
        System.out.println("Plastika: ");
        for(int i=0;i<tPlastika.size();i++) {
            for (int j = 0; j < tPlastika.get(i).pot.size(); j++) {
                System.out.println(tPlastika.get(i).pot.get(j));
            }
            System.out.println();
        }
        System.out.println("Pobrano: "+jeCisto(2));

        System.out.println("Papir: ");
        for(int i=0;i<tPapir.size();i++) {
            for (int j = 0; j < tPapir.get(i).pot.size(); j++) {
                System.out.println(tPapir.get(i).pot.get(j));
            }
            System.out.println();
        }
        System.out.println("Pobrano: "+jeCisto(3));

        double cena=cost(1, tOrganski)+cost(2, tPlastika) + cost(3, tPapir);
        System.out.println("Cena vseh treh: "+cena);

    }

    private static double cost(int tip, LinkedList<Tovornjak> tovornjaki){
        double [] tab;
        double cena=10*tovornjaki.size();
        if (tip == 1)
            tab = vsaMestaOrgranski;
        else if (tip == 2)
            tab = vsaMestaPlastika;
        else
            tab = vsaMestaPapir;
        for(int i=0;i<tovornjaki.size();i++){
            Tovornjak t=tovornjaki.get(i);
            for(int j=0;j<tovornjaki.get(i).pot.size();j++){
                if(j!=tovornjaki.get(i).pot.size()-1) {
                    t.cas += findShortest(mesta.get(tovornjaki.get(i).pot.get(j) - 1), tovornjaki.get(i).pot.get(j + 1)) * 6 / 5;
                    cena += findShortest(mesta.get(tovornjaki.get(i).pot.get(j) - 1), tovornjaki.get(i).pot.get(j + 1)) * 0.1;
                    if (tab[tovornjaki.get(i).pot.get(j) - 1] > 0) {
                        t.cas += 12;
                    }
                }
                if(mesta.get(tovornjaki.get(i).pot.get(j)-1).index == 1 && t.pobrano>0 && j!=0){
                    t.cas+=30;
                }
            }
            if (t.cas > 8*60){
                cena += 8*10;
                cena += (t.cas - 8*60) / 60 * 20;
            }
            else {
                cena += t.cas / 60 * 10;
            }
        }

        return cena;
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

    private static void greedyPot(int tip){
        double [] tab;

        if (tip == 1)
            tab = vsaMestaOrgranski;
        else if (tip == 2)
            tab = vsaMestaPlastika;
        else
            tab = vsaMestaPapir;

        while(jeCisto(tip)!=0) {
            Tovornjak t = new Tovornjak(tip);
            Mesto m1 = mesta.get(0);
            Mesto naslednje = new Mesto();
            int index = lahkoPobere(t, m1, tab);
            while (index != -1) {
                t.pot.add(m1.index);
                naslednje = mesta.get(index - 1);
                //double razdalja = findClosest(m1, naslednje.index);
                m1 = naslednje;
                index = lahkoPobere(t, m1, tab);
                t.pobrano += m1.getOdpadki(tip);
                tab[m1.index-1]=0;
                if (index == -1)
                    t.pot.add(m1.index);
            }
            for (int i = m1.shortestPath.size() - 1; i >= 0; i--) {
                t.pot.add(m1.shortestPath.get(i).index);
            }
            if(t.pot.size()==0) {
                for (int i = 0; i < tab.length; i++) {
                    if (tab[i] != 0) {
                        for (int j = 0; j < mesta.get(i).shortestPath.size(); j++) {
                            t.pot.add(mesta.get(i).shortestPath.get(j).index);
                        }
                        t.pot.add(i+1);
                        for (int j = mesta.get(i).shortestPath.size() - 1; j >= 0; j--) {
                            t.pot.add(mesta.get(i).shortestPath.get(j).index);
                        }
                        t.pobrano+=tab[i];
                        tab[i]=0;
                        break;
                    }
                }
            }
            if (tip == 1)
                tOrganski.add(t);
            else if (tip == 2)
                tPlastika.add(t);
            else
                tPapir.add(t);
        }
    }

    public static void initVsaMesta() {
        for (int i = 0; i<mesta.size(); i++){
            vsaMestaOrgranski[i] = mesta.get(i).getOdpadki(1);
            vsaMestaPlastika[i] = mesta.get(i).getOdpadki(2);
            vsaMestaPapir[i] = mesta.get(i).getOdpadki(3);
        }
    }

    private static double findShortest(Mesto trenutno, int naslednje){
        double min=Double.MAX_VALUE;
        for(int i=0;i<trenutno.sosedje.get(naslednje).size();i++){
            if(trenutno.sosedje.get(naslednje).get(i).velikost<min)
                min=trenutno.sosedje.get(naslednje).get(i).velikost;
        }
        return min;
    }

    public static int jeCisto (int tip){

        int count = 0;

        for (int i = 0; i<vsaMestaOrgranski.length; i++){
            if (tip == 1 && vsaMestaOrgranski[i] > 0)
                count++;
            else if (tip == 2 && vsaMestaPlastika[i] > 0)
                count++;
            else if (tip == 3 && vsaMestaPapir[i] > 0)
                count++;
        }

        return count;
    }


    private static int lahkoPobere(Tovornjak t, Mesto trenutno, double[] tab){
        double min=Double.MAX_VALUE;
        int index=-1;
        if(t.vrstaSmeti==1) {
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).organski <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost < min) {
                            min=trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost;
                            index = trenutno.sosedjeIndex.get(i);
                        }
                    }
                }
            }
        }
        else if(t.vrstaSmeti==2){
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).plastika <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost<min)
                            index=trenutno.sosedjeIndex.get(i);
                    }
                }
            }
        }
        else{
            for (int i = 0; i < trenutno.sosedjeIndex.size(); i++) {
                if (t.pobrano + mesta.get(trenutno.sosedjeIndex.get(i)-1).papir <= maxCap && tab[trenutno.sosedjeIndex.get(i)-1] > 0) {
                    for(int j=0;j<trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).size();j++){
                        if (trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).kapaciteta >=t.pobrano && trenutno.sosedje.get(trenutno.sosedjeIndex.get(i)).get(j).velikost<min)
                            index=trenutno.sosedjeIndex.get(i);
                    }
                }
            }
        }

        return index;
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
