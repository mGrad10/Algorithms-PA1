import java.io.*;
import java.util.*;

/**
 *
 * Algorithms PA1
 * Taylor Coury and Melinda Grad
 * Fall 2017
 *
 */
public class psychicoptimal {

    public static void main(String[] args) {

        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            // Get input and output files from cmd line
            File infile = new File(args[0]);
            File outfile = new File(args[1]);

            br = new BufferedReader(new FileReader(infile));
            pw = new PrintWriter(outfile);

            // Read line from file and parse
            String s = br.readLine();

            // Array which holds n,j,k,l
            int[] params = getParams(s);

            ArrayList<Set<Integer>> bestSets = new ArrayList<Set<Integer>>();
            int numTickets = solve(params[0], params[1], params[2], params[3],bestSets);

            pw.println(numTickets);

            ArrayList<String> bestSetsSorted = new ArrayList<String>();

            for(int i = 0; i < bestSets.size(); i ++){
                bestSetsSorted.add(bestSets.get(i).toString());
            }
            Collections.sort(bestSetsSorted);

            for(int i = 0; i < bestSetsSorted.size(); i++) {
                String temp = bestSetsSorted.get(i).replaceAll("(\\[|,|\\])","");
                pw.println(temp);
            }


            /*
            for(int i = 0; i < bestSets.size(); i++) {
                Object[] temp = new Object[bestSets.get(i).size()];
                bestSets.get(i).toArray(temp);
                for(int j = 0; j < temp.length; j++) {

                    pw.print((int)temp[j]);
                    pw.print(' ');
                }
                pw.println();
            }
            */

            br.close();
            pw.close();

        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    /* Method to solve to solve the Psychic Modeling problem
     * @param the number of candidate numbers n, the number of numbers
     *        on a ticket k, the number of promised winning numbers j,
     *        the list of the tickets that guarantee a win
     * @return min number of tickets that guarantee a win
     */
    public static int solve(int n, int j, int k, int l, ArrayList<Set<Integer>> bestSets){

        ArrayList<Set<Integer>> tickets = new ArrayList<Set<Integer>>();
        ArrayList<Set<Integer>> promisedSets = new ArrayList<Set<Integer>>();
        Set<Integer> current = new TreeSet<Integer>();
        createTicketTree(n,j,k,tickets, promisedSets,current);

        ArrayList<Set<Integer>> currentSet = new ArrayList<Set<Integer>> ();
        int level = 0;
        createSetsOfTickets(l, level, tickets,promisedSets, bestSets, currentSet);

        return bestSets.size();
    }

    /* Method to create all possible sets of tickets
     * @param number of winning numbers needed on a ticket to win l,
     *        level of the tree level, the set of all possible tickets ,a list of sets of possible
     *        combinations of promised winning numbers promisedSets,
     *        the current working set currentSet, list of min number of sets needed to cover bestSets
     */
    public static void createSetsOfTickets(int l, int level, ArrayList<Set<Integer>> tickets, ArrayList<Set<Integer>> promisedSets,
                                           ArrayList<Set<Integer>> bestSets, ArrayList<Set<Integer>> currentSet) {

        // Base case is leaf node
        if(level == tickets.size()) {
            if(!currentSet.isEmpty()) {
                ArrayList<Integer> cover = new ArrayList<Integer>();
                for(int i = 0; i <currentSet.size(); i++) {
                    for(int j = 0; j < promisedSets.size(); j++) {
                        //Do not look at covered
                        if(!cover.contains(j)){
                            int contains = 0;
                            Object[] temp = new Object[currentSet.get(i).size()];
                            currentSet.get(i).toArray(temp);
                            for(int k = 0; k < currentSet.get(i).size(); k++) {
                                if(promisedSets.get(j).contains(temp[k])) {
                                    contains++;
                                }
                            }
                            if(contains >= l)
                                cover.add(j);
                        }
                    }
                }
                if(cover.size() == promisedSets.size()) { // Then it covers all
                    if(bestSets.isEmpty()) {
                        bestSets.addAll(currentSet);
                    }
                    else{ // Check if new best
                        if(currentSet.size() < bestSets.size()) {
                            bestSets.removeAll(bestSets);
                            bestSets.addAll(currentSet);
                        }
                    }
                }
            }

        }
        else{
            // Don't include ticket
            createSetsOfTickets(l, level + 1, tickets, promisedSets, bestSets, currentSet);

            // Do include ticket
            currentSet.add(tickets.get(level));
            createSetsOfTickets(l, level + 1, tickets, promisedSets, bestSets, currentSet);
            currentSet.remove(tickets.get(level));
        }

    }

    /* Method to create the tree containing all possible tickets
     * @param the number of candidate numbers n, the number of numbers
     *        on a ticket k, the number of promised winning numbers j,
     *        the set of all possible tickets , a list of sets of possible
     *        combinations of promised winning numbers promisedSets,
     *        the current working set current
     */
    public static void createTicketTree(int n, int j, int k, ArrayList<Set<Integer>> tickets,
                                        ArrayList<Set<Integer>> promisedSets, Set<Integer> current){

        // Base case
        if(current.contains(n)) {
            if (current.size() == k) {
                tickets.add(current);
            }
            if (current.size() == j) {
                promisedSets.add(current);
            }
        }
        else {
            if(current.size() == k) {
                tickets.add(current);
                if(current.size() == j) {
                    promisedSets.add(current);
                }
            }
            else{
                if(current.size() == j) {
                    promisedSets.add(current);
                }
                int greatest= 0;
                if(!current.isEmpty())
                    greatest = findGreatest(current);

                for(int i = greatest + 1; i <= n; i ++) {
                    Set<Integer> tempSet = new TreeSet<Integer>();
                    tempSet.addAll(current);
                    tempSet.add(i);

                    createTicketTree(n,j,k,tickets,promisedSets,tempSet);
                }
            }
        }
    }//End createTicketTree

    /* Method to find to find the greatest number in the set
     * @param the set to search
     * @return the greatest number in the set
     */
    public static int findGreatest(Set<Integer> set) {

        Object[] arraySet= new Object[set.size()];
        set.toArray(arraySet);
        int greatest = (int)arraySet[0];

        for(int i = 1; i < arraySet.length; i++) {
            if((int)arraySet[i] > greatest) {
                greatest = (int)arraySet[i];
            }
        }
        return greatest;
    } //End findGreatest

    /* Method to tokenize the first line of the file
     * into an integer array
     * @param string to hold the first line of the file
     * @return integer array with problem params
     */
    public static int[] getParams(String s){

        StringTokenizer st = new StringTokenizer(s);

        int[] intArr = new int[4];
        for(int i = 0; i < 4; i++){
            String temp = st.nextToken();
            intArr[i] = Integer.parseInt(temp);
        }
        return intArr;
    }//End getParams

 }


