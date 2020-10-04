import java.util.*;

public class FIFO extends PageAlgo{
    private Queue<Integer> frame;

    public FIFO(int[] refString, boolean[] modify, int frameSize) {
        super(refString, modify, frameSize);
        this.frame = new LinkedList<>();
    }

    public void run(){
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            if(!frame.contains(refString[i])) { // page fault
                pageFault++;
                interrupt++;
                if (size == frameSize) { // frame is full
                    // Check the replaced page is modified, if so, write to disk
                    int out = frame.poll();
                    boolean isModify = map.get(out);
                    if (isModify) {
                        WriteToDisk();
                    }
                    map.remove(out);
                }
                frame.offer(refString[i]);
                map.put(refString[i], modify[i]);
            }
        }
        System.out.println("    \tPageFault\tInterrupt\tDiskWrite");
        System.out.format("FIFO %12d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
//        System.out.println("The number of page fault = " + pageFault);
//        System.out.println("The number of interrupt = " + interrupt);
//        System.out.println("The number of disk write = " + diskWrite);

    }
}
