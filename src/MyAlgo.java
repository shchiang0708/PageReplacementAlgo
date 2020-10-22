import java.util.Random;

public class MyAlgo extends PageAlgo{
    public MyAlgo(int[] refString, int frameSize, boolean[] modify){
        super(refString, frameSize, modify);
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
                dirty.put(refString[i], modify[i]);
//                if(p < prob){
//                    dirty.put(refString[i], true);
//                }else{
//                    dirty.put(refString[i], false);
//                }
            }
            else{ // current reference string is in frame
                if(modify[i] == true)
                    dirty.put(refString[i], modify[i]);
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
