import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ESC extends PageAlgo{
    private List<Integer> frame;

    public ESC(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
        frame = new LinkedList<>();
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            if(!frame.contains(refString[i])){
                pageFault++;
                interrupt++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify)
                        WriteToDisk();
                    dirty.remove(out);
                    ref.remove(out);
                    frame.remove(new Integer(out));
                }
                frame.add(refString[i]);
                dirty.put(refString[i], modify[i]);
                ref.put(refString[i], false);
            }else{ // if frame contains current string, set reference bit = 1
                ref.put(refString[i], true);
            }
        }
        System.out.format("ESC %13d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
    }

    private int findVictim(){
        // 1. find (0, 0)
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) && dirty.get(frame.get(i))){
                return frame.get(i);
            }
        }
        // 2. find (0, 1)
        for(int i = 0; i < frame.size(); i++){
            if(!ref.get(frame.get(i)) && dirty.get(frame.get(i))){
                return frame.get(i);
            }
        }
        // 3. clear reference bit
        for(int i = 0; i < frame.size(); i++){
            ref.put(frame.get(i), false);
        }
        // 4. find (0, 0) again
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) && dirty.get(frame.get(i))){
                return frame.get(i);
            }
        }
        // 4. find(0, 1) again
        for(int i = 0; i < frame.size(); i++){
            if(!ref.get(frame.get(i)) && dirty.get(frame.get(i))){
                return frame.get(i);
            }
        }

        return frame.get(0);
    }
}
