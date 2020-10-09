

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

//              Update dirty bit
                if(!dirty.get(refString[i]))
                    dirty.put(refString[i], modify[i]);
            }
//          if the current memory reference is modify, menas we need to set dirty bit
//          cost++
            if(modify[i] == true)
                cost++;
        }
        System.out.format("FIFO %12d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }
}
