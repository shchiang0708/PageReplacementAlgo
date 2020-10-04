import sun.awt.image.ImageWatched;

import java.util.*;

public class Optimal extends PageAlgo{
    private List<Integer> frame;

    public Optimal(int[] refString, boolean[] modify, int frameSize){
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
                    int out = findVictim(i + 1);
                    boolean isModify = map.get(out);
                    if(isModify)
                        WriteToDisk();
                    map.remove(out);
                    frame.remove(new Integer(out));
                }
                frame.add(refString[i]);
                map.put(refString[i], modify[i]);
            }
        }
        System.out.format("OPT %13d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
    }

    private int findVictim(int n){
        List<Integer> list = new LinkedList<>();
        for(int i = 0; i < frame.size(); i++){
            list.add(frame.get(i));
        }

        for(int i = n; i < refString.length; i++){
            if(list.contains(new Integer(refString[i])) && list.size() > 1){
                list.remove(new Integer(refString[i]));
            }
        }

        return list.get(0);
    }
}

