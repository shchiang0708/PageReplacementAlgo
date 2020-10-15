import java.util.Random;

public class ESC extends PageAlgo{
    public ESC(int[] refString, int frameSize, int prob){
        super(refString, frameSize, prob);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            Random rand = new Random();
            int p = rand.nextInt(100);
            if(!frame.contains(refString[i])){
                pageFault++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify)
                        WriteToDisk();
                    ref.remove(out);
                    frame.remove(new Integer(out));
                    dirty.remove(out);
                }
                frame.add(refString[i]);
                ref.put(refString[i], false);
                if(p < prob){
                    dirty.put(refString[i], true);
                }else{
                    dirty.put(refString[i], false);
                }
            }
            else{// current reference string is in frame

                // if frame contains current string, set reference bit = 1
                ref.put(refString[i], true);
                if(p < prob){
                    dirty.put(refString[i], true);
                }
            }
        }
        System.out.format("ESC %13d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }

    private int findVictim(){
        // 1. find (0, 0)
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) == false && dirty.get(frame.get(i)) == false){
                return frame.get(i);
            }
        }
        // 2. find (0, 1)
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) == false && dirty.get(frame.get(i)) == true){
                return frame.get(i);
            }
            ref.put(frame.get(i), false); // Clear the reference bit bypass until we find victim
            cost++;                      // if clear, then we need cost.
        }
//        // 3. clear reference bit, cost++
//        for(int i = 0; i < frame.size(); i++){
//            ref.put(frame.get(i), false);
//        }

        // 4. find (0, 0) again
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) == false && dirty.get(frame.get(i)) == false){
                return frame.get(i);
            }
        }
        // 5. find (0, 1) again
        for(int i = 0; i < frame.size(); i++){
            if(ref.get(frame.get(i)) == false && dirty.get(frame.get(i)) == true){
                return frame.get(i);
            }
        }
        // This should not happened.
        System.out.println("error");
        return -1;
    }
}
