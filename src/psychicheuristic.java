/**
 *
 * Algorithms PA1 -- Hueristic
 * Taylor Coury and Melinda Grad
 * Fall 2017
 *
 */
import java.io.*;
import java.util.*;

public class psychicheuristic {

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

        // Randomly choose a ticket
        Random rand = new Random();
        int ticketToChoose = rand.nextInt(tickets.size());
        ArrayList<Set<Integer>> firstTicket = new ArrayList<Set<Integer>>();
        firstTicket.add(tickets.get(ticketToChoose));
        bestSets.addAll(firstTicket);

        ArrayList<Integer> cover = new ArrayList<Integer>();
        checkCoverage(promisedSets,firstTicket,cover,l);

        while(cover.size() != promisedSets.size()) {

            // Set of all promised that are not covered by tickets
            ArrayList<Set<Integer>> promiseUncovered = new ArrayList<Set<Integer>>();

            for(int i = 0; i < promisedSets.size(); i++) {
                if(!cover.contains(i)) { // If set is not covered
                    promiseUncovered.add(promisedSets.get(i));

                }
            }

            int coverSize = 0;
            Set<Integer> nextTicket = new TreeSet<Integer>();

            for(int i = 0; i<tickets.size(); i++) {
                ArrayList<Integer> newCover = new ArrayList<Integer>();
                ArrayList<Set<Integer>> checkTicket = new ArrayList<Set<Integer>>();

                if(!bestSets.contains(tickets.get(i))){
                    checkTicket.add(tickets.get(i));
                    checkCoverage(promiseUncovered, checkTicket, newCover, l);
                    if(coverSize < newCover.size()) {
                        coverSize = newCover.size();
                        nextTicket = tickets.get(i);

                        if(coverSize == promiseUncovered.size()){
                            break;
                        }
                    }
                }
            }

            bestSets.add(nextTicket);
            checkCoverage(promisedSets, bestSets, cover, l);
        }

        return bestSets.size();
    }
    /* Method to check which sets of the promised are covered
     * @param a list of sets of possible combinations of promised winning numbers
     *        promisedSets, a list of tickets chosen to try to cover chosenTickets,
     *        list of elements that were covered cover, number of winning numbers
     *        needed on a ticket to win l
     */
    public static void checkCoverage(ArrayList<Set<Integer>> promisedSets,ArrayList<Set<Integer>> chosenTickets,ArrayList<Integer> cover, int l){

        for(int i = 0; i < chosenTickets.size(); i++) {
            for(int j = 0; j < promisedSets.size(); j++) {
                //Do not look at covered
                if (!cover.contains(j)) {
                    int contains = 0;
                    Object[] temp = new Object[chosenTickets.get(i).size()];
                    chosenTickets.get(i).toArray(temp);
                    for (int k = 0; k < chosenTickets.get(i).size(); k++) {
                        if (promisedSets.get(j).contains(temp[k])) {
                            contains++;
                        }
                    }
                    if (contains >= l)
                        cover.add(j);
                }
            }
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

