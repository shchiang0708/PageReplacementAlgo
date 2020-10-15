import java.util.*;

public class Main {
    private static final int lengthOfRefString = 200000;
    private static final int[] randomRefString = generateRandom();
    private static final int[] localityRefString = generateLocality();
    private static final int[] MyRefString = generateMyPick();
    private static final int prob = 25; // The probability of modify
    public static void main(String[] args) {

        int[] refString;
        Scanner sc = new Scanner(System.in);
//        int frameSize = 3;
//        refString = randomRefString;
//        fifo = new FIFO(refString, modify, frameSize);
//        fifo.run();2
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

            int frameSize = 20;
            while(frameSize <= 100) {
                System.out.println("Frame Size = " + frameSize);
                System.out.println("    \tPageFault\tInterrupt\tDiskWrite\t\t Cost");

                // FIFO
                FIFO fifo = new FIFO(refString, frameSize, prob);
                fifo.run();
                // Additional reference bit
                ARB arb = new ARB(refString, frameSize, prob);
                arb.run();
                // Enhanced Second Chance
                ESC esc = new ESC(refString, frameSize, prob);
                esc.run();
                // My algorithm
                // Based on FIFO, but running with dirty bits.
                MyAlgo myAlgo = new MyAlgo(refString, frameSize, prob);
                myAlgo.run();

                System.out.println();
                frameSize = frameSize + 20;
            }
        }

    }

    public static int[] generateRandom(){
//        int[] n = {1,3,0,3,5,6,3};
//        int[] n = {4,7,3,0,1,7,3,8,5,4,5,3,4,7};
//        int[] n = {1,2,3,4,2,1,5,6,2,1,2,3,7,6,3,2,1,2,3,6};
//        int[] n = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
//        int[] n = {1,2,3,4,1,2,5,1,2,3,4,5};
        Random rand = new Random();
        int idx = 0;
        int[] n = new int[lengthOfRefString];
        while(idx < lengthOfRefString){
            int number = rand.nextInt(800) + 1; // Random a number 1 - 800
            int range = rand.nextInt(25) + 1;   // Random range 1 - 25
            if(number + range > 800){  // Prevent from generating number > 800
                number = number - (number + range - 1 - 800);
            }

            for(int i = 0; i < range; i++){
                if(idx < lengthOfRefString) {
                    n[idx] = number;
                    number = number + 1;
                    idx = idx + 1;
                }
            }
        }
//        for(int i = 0; i < lengthOfRefString; i++){
//            int number = rand.nextInt(800) + 1;
//            n[idx] = number;
//            idx = idx + 1;
//        }
        return n;
    }

    public static int[] generateLocality(){
//*******************************************************************
//      To partition reference string as different procedures
//      subset of 1/25 to 1/15 (32 - 53)
        int idx = 0;
        int[] refString = new int[lengthOfRefString];

        Random rand = new Random();
        int procedures = rand.nextInt(10) + 21; // 20 - 30 procedures
        int part = lengthOfRefString / procedures;    // get each procedures size

        for(int i = 0; i < procedures; i++){
            int range = rand.nextInt(22) + 32; // The number of items of subset
            int number = rand.nextInt(800) + 1; // Random a number 1 - 800
            int left = range / 2;
            int right = range - left - 1;

            int start = number - left;
            int end = number + right;

//**********************************************************************
//          Adjust the interval to prevent start < 0 && end > 800
            if(start < 0){
                end = end + (0 - start);
                start = 0;
            }
            if(end > 800){
                start = start - (end - 800);
                end = 800;
            }
//***********************************************************************
            for(int j = 0; j < part; j++){
                int n = rand.nextInt(end - start + 1) + start; // Random a number from start and end
                refString[idx] = n;
                idx = idx + 1;
            }
        }

        // The partition may not be perfect (procedures * part may not equal to 200000)
        // so the remaining memory reference will be generated randomly.
        while(idx < lengthOfRefString){
            refString[idx] = rand.nextInt(800) + 1;
            idx = idx + 1;
        }
        return refString;
    }

    public static int[] generateMyPick(){
        int[] refString = new int[lengthOfRefString];
        Random rand = new Random();

        // Select a consecutive pages of length 7 simulated as a function
        int range = 7;
        int start = rand.nextInt(794) + 1;

        System.out.println();
        //int[] s = {1,2,3,4,5};
        int idx = 0;
        while(idx < lengthOfRefString){
            int r = rand.nextInt(100);
            // set threshold as 40%, which represent the frequent of function call
            if(r < 40) {
//                for(int i = 0; i < s.length; i++){
//                    if(idx < lengthOfRefString){
//                        refString[idx++] = s[i];
//                    }
//                }
                for (int i = start; i < start + range; i++) {
                    if(idx < lengthOfRefString) {
                        refString[idx++] = i;
                    }
                }
            }else{
                refString[idx++] = rand.nextInt(800) + 1;
            }
        }
        return refString;
    }
}

