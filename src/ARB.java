import java.util.*;

public class ARB extends PageAlgo{

    private final static int interval = 80; // Time interval of OS
    private int count;
    private Map<Integer, boolean[]> additionalRef;
    public ARB(int[] refString, int frameSize, boolean[] modify){
        super(refString, frameSize, modify);
        count = 0;
        additionalRef = new HashMap<>();
    }

    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            Random rand = new Random();
            int p = rand.nextInt(100);
            if(!frame.contains(refString[i])) { // page fault
                pageFault++;
                if (size == frameSize) { // frame is full
                    // Check the replaced page is modified, if so, write to disk
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if (isModify) {
                        WriteToDisk();
                    }
                    frame.remove(new Integer(out));
                    ref.remove(out);
                    dirty.remove(out);
                    additionalRef.remove(out);
                }
                frame.add(refString[i]);
                dirty.put(refString[i], modify[i]);
                ref.put(refString[i], false);
                additionalRef.put(refString[i], new boolean[8]);
            }
            else{ // current reference string is in frame
                ref.put(refString[i], true);
                if(modify[i] == true)
                    dirty.put(refString[i], modify[i]);
            }
            count++;
            if(count == interval){
                count = 0;
                updateARB();
                cost++;
                interrupt++;
            }
        }
        System.out.format("ARB %13d" + "%12d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite, cost);
    }
    private int findVictim(){
        List<Integer> list = new LinkedList<>(frame);

        for(int i = 0; i < 8; i++){
            int size = list.size();
            List<Integer> cand = new LinkedList<>(); // Candidate to be selected as victim
            for(int j = 0; j < size; j++){
                int n = list.get(j);
                if(additionalRef.get(n)[i] == false){ // if the reference string in the page,
                    cand.add(n);                      // and the current ARB = 0, then add into candidate
                }
            }

            // if cand.size == 0, means additional reference bit of the index i of all pages = 1
            // So there is no candidate generated, the list should be the original list
            // else, cand should be the new list.
            if(cand.size() != 0)
                list = new LinkedList<>(cand);
        }
        return list.get(0);
    }
    private void updateARB(){
        List<Integer> list = new LinkedList<>(frame);

        // Shift right 1 bit
        for(int i = 0; i < list.size(); i++){
            int n = list.get(i); // get each reference string in frame
            boolean[] ar = additionalRef.get(n);

            for(int j = ar.length - 1; j > 0; j--){
                ar[j] = ar[j - 1];
            }

            ar[0] = ref.get(n); // set highest bit as reference bits
            additionalRef.put(n, ar);
            ref.put(n, false);
        }
    }
}
