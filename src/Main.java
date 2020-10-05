import java.util.*;

public class Main {
    private static final int lengthOfRefString = 100000;
    private static final boolean[] modify = generateModify(25);
    private static final int[] randomRefString = generateRandom();
    private static final int[] localityRefString = generateLocality();
    private static final int[] MyRefString = generateMyPick();
    public static void main(String[] args) {

        int[] refString;

        Scanner sc = new Scanner(System.in);
//        int frameSize = 3;
//        refString = randomRefString;
//        fifo = new FIFO(refString, modify, frameSize);
//        fifo.run();
//        opt = new Optimal(refString, modify, frameSize);
//        opt.run();
//        esc = new ESC(refString, modify, frameSize);
//        esc.run();
//        myAlgo = new MyAlgo(refString, modify, frameSize);
//        myAlgo.run();
//
        while(true){
            System.out.println("How to pick?");
            System.out.println("(1)Random pick (2)Locality pick (3)My pick (4)Exit");
            int choice = sc.nextInt();
            if(choice == 1){
                refString = randomRefString;
            }else if(choice == 2){
                refString = localityRefString;
            }else if (choice == 3){
                // Simulate a function being called very often, but irregularly
                // ex: list.add(), ;
                refString = MyRefString;
            }else {
                break;
            }

            int frameSize = 10;
            while(frameSize <= 100) {
                System.out.println("Frame = " + frameSize);
                // FIFO
                FIFO fifo = new FIFO(refString, modify, frameSize);
                fifo.run();
                // Optimal
                Optimal opt = new Optimal(refString, modify, frameSize);
                opt.run();
                // Enhanced Second Chance
                ESC esc = new ESC(refString, modify, frameSize);
                esc.run();
                // My algorithm
                // Based on FIFO, but running with dirty bits.
                MyAlgo myAlgo = new MyAlgo(refString, modify, frameSize);
                myAlgo.run();

                System.out.println();
                frameSize = frameSize + 10;
            }
        }

    }

    public static int[] generateRandom(){
//        int[] n = {1,3,0,3,5,6,3};
//        int[] n = {4,7,3,0,1,7,3,8,5,4,5,3,4,7};
//        int[] n = {1,2,3,4,2,1,5,6,2,1,2,3,7,6,3,2,1,2,3,6};
//        int[] n = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
//        int[] n = {1,2,3,4,1,2,5,1,2,3,4,5};
        int[] n = new int[lengthOfRefString];
        for(int i = 0; i < lengthOfRefString; i++){
            n[i] = (int)(Math.random() * 500) + 1;
        }
        return n;
    }
    public static boolean[] generateModify(double prob){
        boolean[] res = new boolean[lengthOfRefString];
        for(int i = 0; i < lengthOfRefString; i++){
            double rand = Math.random() * 100;
            res[i] = rand <= prob;
        }
        return res;
    }

    public static int[] generateLocality(){
//*******************************************************************
//      To partition reference string as different function
//      subset of 1/20 to 1/50 (10 - 25)
        Map<Integer, int[]> partition = new HashMap<>();
        int remain = 500;
        int cur = 1;
        int n = 0;
        while(remain > 25){
            int range = (int) (Math.random() * 15) + 10;
            partition.put(n, new int[]{cur, cur + range});
            n = n + 1;
            remain = remain - range;
            cur = cur + range + 1;
        }

        partition.put(n, new int[]{cur, 500});
//*******************************************************************
//      Randomly select partition and add whole elements of partition into reference string
        int[] refString = new int[lengthOfRefString];
        int idx = 0;
        while(idx < lengthOfRefString){
            int rand = (int) (Math.random() * partition.size());
            int[] p = partition.get(rand);
            for(int i = p[0]; i <= p[1]; i++){
                if(idx < lengthOfRefString)
                    refString[idx++] = i;
            }
        }
        System.out.println();
        return refString;
//        int idx = 0;
//
//        while(idx < lengthOfRefString) {
//            int range = (int) (Math.random() * 25) + 25;
//            int start = (int) (Math.random() * (500 - range));
//
//            int[] ts = Arrays.copyOfRange(orgRefString, start, start + range); // temp of reference string
//           // boolean[] tm = Arrays.copyOfRange(orgModify, start, start + range); // temp of modify bits
//
//            for(int i = 0; i < range; i++){
//                if(idx < lengthOfRefString) {
//                    refString[idx] = ts[i];
//                   // modify[idx] = tm[i];
//                    idx = idx + 1;
//                }
//            }
//        }
    }

    public static int[] generateMyPick(){
        int[] refString = new int[lengthOfRefString];

        // Select a consecurive pages of length 7 simulated as a function
        int range = 7;
        int start = (int)(Math.random() * 500) + 1 - range;

        System.out.println();
        //int[] s = {1,2,3,4,5};
        int idx = 0;
        while(idx < lengthOfRefString){
            double rand = Math.random() * 100;
            // set threshhold as 40%, which represent the frequent of function call
            if(rand <= 40) {
//                for(int i = 0; i < s.length; i++){
//                    if(idx < lengthOfRefString){
//                        refString[idx++] = s[i];
//                    }
//                }
                for (int i = start; i <= start + range; i++) {
                    if(idx < lengthOfRefString) {
                        refString[idx++] = i;
                    }
                }
            }else{
                refString[idx++] = (int)(Math.random() * 500) + 1;
            }
        }
        return refString;
    }
}

