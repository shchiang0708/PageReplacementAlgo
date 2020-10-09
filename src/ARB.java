import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ARB extends PageAlgo{

    private final static int interval = 8; // Time interval of OS
    private int count;
    private Map<Integer, boolean[]> additionalRef;
    public ARB(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
        count = 1;
        additionalRef = new HashMap<>();
    }

    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = page.size();
            if(!page.contains(refString[i])) { // page fault
                pageFault++;
                if (size == frameSize) { // frame is full
                    // Check the replaced page is modified, if so, write to disk
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if (isModify) {
                        WriteToDisk();
                    }
                    page.remove(new Integer(out));
                    ref.remove(out);
                    dirty.remove(out);
                    additionalRef.remove(out);
                }
                page.add(refString[i]);
                dirty.put(refString[i], modify[i]);
                ref.put(refString[i], false);
                additionalRef.put(refString[i], new boolean[8]);
            }
            else{ // current reference string is in page
                ref.put(refString[i], true);
                cost++;
                // Update dirty bit
                if(!dirty.get(refString[i]))
                    dirty.put(refString[i], modify[i]);
            }
//          if the current memory reference is modify, menas we need to set dirty bit
//          cost++
            if(modify[i] == true)
                cost++;

            count++;
            if(count == interval){
                count = 1;
                updateARB();
                cost++;
            }
        }
        System.out.format("ARB %13d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }
    private int findVictim(){
        List<Integer> list = new LinkedList<>(page);

        for(int i = 0; i < 8; i++){
            int size = list.size();
            List<Integer> cand = new LinkedList<>(); // Candidate to be selected as victim
            for(int j = 0; j < size; j++){
                int n = list.get(j);
                if(additionalRef.get(n)[i] == false){ // if the reference string in the page,
                    cand.add(n);                      // and the current ARB = 0, then add into candidate
                }
            }
            // if cand.size() == 0 || cand.size == list.size()
            // means the current bit of ARB of reference string in the page
            // are whole same
            if(cand.size() > 0 && cand.size() < list.size())
                list = new LinkedList<>(cand);
        }
        return list.get(0);
    }
    private void updateARB(){
        List<Integer> list = new LinkedList<>(page);

        // Shift right 1 bit
        for(int i = 0; i < list.size(); i++){
            int n = list.get(i); // get each reference string in page
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
