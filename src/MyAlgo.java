import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyAlgo extends PageAlgo{
    List<Integer> frame;

    public MyAlgo(int[] refString, boolean[] modify, int frameSize){
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
        System.out.format("MyAlgo %10d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
    }

    private int findVictim(){
        for(Map.Entry<Integer, Boolean> entry: map.entrySet()){
            if(entry.getValue() == false)
                return entry.getKey();
        }
        return frame.get(0);
    }
}
