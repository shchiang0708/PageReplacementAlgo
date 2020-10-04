import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

abstract public class PageAlgo {
    protected LinkedList<Integer> frame;
    protected int[] refString;
    protected boolean[] modify;
    protected int frameSize;
    protected int pageFault;
    protected int interrupt;
    protected int diskWrite;
    protected Map<Integer, Boolean> ref; // reference bits
    protected Map<Integer, Boolean> dirty;
    public PageAlgo(int[] refString, boolean[] modify, int frameSize){
        frame = new LinkedList<>();
        this.refString = refString;
        this.modify = modify;
        this.frameSize = frameSize;
        this.pageFault = 0;
        this.interrupt = 0;
        this.diskWrite = 0;
        ref = new HashMap<>();
        dirty = new HashMap<>();
    }

    protected void WriteToDisk(){
        interrupt++;
        diskWrite++;
    }

    abstract public void run();
}
