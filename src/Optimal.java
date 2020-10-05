import java.util.*;

public class Optimal extends PageAlgo{

    public Optimal(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            if(!frame.contains(refString[i])){
                pageFault++;
                interrupt++;
                if(size == frameSize){ // frame is full
                    int out = findVictim(i + 1);
                    boolean isModify = dirty.get(out);
                    if(isModify)
                        WriteToDisk();
                    frame.remove(new Integer(out));
                    dirty.remove(out);
                }
                frame.add(refString[i]);
                dirty.put(refString[i], modify[i]);
            }else{
                // Update dirty bit
                if(!dirty.get(refString[i]))
                    dirty.put(refString[i], modify[i]);
            }
        }
        System.out.format("OPT %13d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
    }

    private int findVictim(int n){
        List<Integer> list = new LinkedList<>(frame);

        for(int i = n; i < refString.length; i++){
            list.remove(new Integer(refString[i]));
            if(list.size() == 1)
                break;
        }
        return list.get(0);
    }
}

