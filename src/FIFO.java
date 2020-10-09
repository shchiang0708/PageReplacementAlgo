

public class FIFO extends PageAlgo{

    public FIFO(int[] refString, boolean[] modify, int frameSize) {
        super(refString, modify, frameSize);
    }

    public void run(){
        for(int i = 0; i < refString.length; i++){
            int size = page.size();
            if(!page.contains(refString[i])) { // page fault
                pageFault++;
                if (size == frameSize) { // frame is full
                    // Check the replaced page is modified, if so, write to disk
                    int out = page.get(0);
                    boolean isModify = dirty.get(out);
                    if (isModify) {
                        WriteToDisk();
                    }
                    dirty.remove(out);
                    page.remove(0);
                }
                page.add(refString[i]);
                dirty.put(refString[i], modify[i]);
            }
            else{ // current reference string is in page

                // Update dirty bit, only when the recorded dirty bit in page = 0
                // then we check the current memory reference is modify or not
                // if modify, then set dirty bit = 1
                if(dirty.get(refString[i]) == false)
                    dirty.put(refString[i], modify[i]);
            }
        }
        System.out.format("FIFO %12d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }
}
