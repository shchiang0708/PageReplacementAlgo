import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

abstract public class PageAlgo {
    protected LinkedList<Integer> page;
    protected int[] refString;
    protected int frameSize;
    protected int pageFault;
    protected int cost;
    protected int diskWrite;
    protected Map<Integer, Boolean> ref; // reference bits, set by hardware, clear by OS
    protected Map<Integer, Boolean> dirty; // dirty bits, set by hardware, clear by OS
    protected int prob;
    public PageAlgo(int[] refString, int frameSize, int prob){
        page = new LinkedList<>();
        this.refString = refString;
        this.frameSize = frameSize;
        this.prob = prob;
        this.pageFault = 0;
        this.cost = 0;
        this.diskWrite = 0;
        ref = new HashMap<>();
        dirty = new HashMap<>();
    }

    protected void WriteToDisk(){
        cost++;
        diskWrite++;
    }

    abstract public void run();
}
