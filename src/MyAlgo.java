import java.util.Random;

public class MyAlgo extends PageAlgo{
    public MyAlgo(int[] refString, int frameSize, int prob){
        super(refString, frameSize, prob);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            Random rand = new Random();
            int p = rand.nextInt(100);
            if(!frame.contains(refString[i])){ // page fault
                pageFault++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify) {
                        WriteToDisk();
                    }
                    frame.remove(new Integer(out));
                    dirty.remove(out);
                }
                frame.add(refString[i]);
                if(p < prob){
                    dirty.put(refString[i], true);
                }else{
                    dirty.put(refString[i], false);
                }
            }
            else{ // current reference string is in frame

                // Update dirty bit, only when the recorded dirty bit in page = 0
                // then we check the current memory reference is modify or not
                // if modify, then set dirty bit = 1
                if(p < prob){
                    dirty.put(refString[i], true);
                }
            }
        }
        System.out.format("MyAlgo %10d" + "%12d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite, cost);
    }

    private int findVictim(){
        for(int i = 0; i < frame.size(); i++){
            if(dirty.get(frame.get(i)) == false)
                return frame.get(i);
        }
        return frame.get(0);
    }
}
