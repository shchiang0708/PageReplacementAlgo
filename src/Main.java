public class Main {
    private static int lengthOfRefString = 100000;
    public static void main(String[] args) {
        int frameSize = 10;
        int[] refString = generateRefString();
        boolean[] modify = generateModify(25, refString.length);
//        FIFO fifo = new FIFO(refString, modify, frameSize);
//        fifo.run();
//        Optimal opt = new Optimal(refString, modify, frameSize);
//        opt.run();
        while(frameSize <= 100) {
            System.out.println("Frame = " + frameSize);
            FIFO fifo = new FIFO(refString, modify, frameSize);
            fifo.run();
            Optimal opt = new Optimal(refString, modify, frameSize);
            opt.run();
            ESC esc = new ESC(refString, modify, frameSize);
            esc.run();
            MyAlgo myAlgo = new MyAlgo(refString, modify, frameSize);
            myAlgo.run();

            System.out.println();
            frameSize = frameSize + 10;
        }
    }

    public static int[] generateRefString(){
//        int[] n = {1,3,0,3,5,6,3};
//        int[] n = {4,7,3,0,1,7,3,8,5,4,5,3,4,7};
//        int[] n = {1,2,3,4,2,1,5,6,2,1,2,3,7,6,3,2,1,2,3,6};
//        int[] n = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
//        int[] n = {1,2,3,4,1,2,5,1,2,3,4,5};
        int[] n = new int[lengthOfRefString];
        for(int i = 0; i < lengthOfRefString; i++){
            n[i] = (int)(Math.random() * 500);
        }
        return n;
    }
    public static boolean[] generateModify(double prob, int length){
        boolean[] res = new boolean[length];
        int count = 0;
        for(int i = 0; i < length; i++){
            double rand = Math.random() * 100;
            res[i] = rand <= prob;
        }
        return res;
    }
}
