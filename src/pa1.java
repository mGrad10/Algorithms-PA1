import java.io.*;
import java.util.StringTokenizer;


/*** Algorithms PA1
 * Taylor Coury and Melinda Grad
 */
public class pa1 {

    public static void main(String[] args) {

        BufferedReader br = null;
        BufferedWriter bw = null;

        // Get input and output files from cmd line
        File infile = new File(args[0]);
        File outfile = new File(args[1]);

        try {https://github.com/mGrad10/Algorithms-PA1.git
        br = new BufferedReader(new FileReader(infile));
            bw = new BufferedWriter(new FileWriter(outfile));

            // Read line from file and parse
            String s = br.readLine();

            // Array which holds n,j,k,l
            int[] params = getParams(s);
            for(int i = 0; i < params.length; i++){
                System.out.println(params[i]);
            }


        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

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
    }

 }


