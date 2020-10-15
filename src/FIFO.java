import java.util.Random;

public class FIFO extends PageAlgo{

    public FIFO(int[] refString, int frameSize, int prob) {
        super(refString, frameSize, prob);
    }

    public void run(){
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            Random rand = new Random();
            int p = rand.nextInt(100);
            if(!frame.contains(refString[i])) { // page fault
                pageFault++;
                if (size == frameSize) { // frame is full
                    // Check the replaced page is modified, if so, write to disk
                    int out = frame.get(0);
                    boolean isModify = dirty.get(out);
                    if (isModify) {
                        WriteToDisk();
                    }
                    dirty.remove(out);
                    frame.remove(0);
                }
                frame.add(refString[i]);
                if(p < prob){
                    dirty.put(refString[i], true);
                }else{
                    dirty.put(refString[i], false);
                }
            }
            else{ // current reference string is in frame
                if(p < prob){
                    dirty.put(refString[i], true);
                }
            }
        }
        System.out.format("FIFO %12d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }
}
