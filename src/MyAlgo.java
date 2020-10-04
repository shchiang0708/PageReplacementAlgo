public class MyAlgo extends PageAlgo{
    public MyAlgo(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = frame.size();
            if(!frame.contains(refString[i])){ // page fault
                pageFault++;
                interrupt++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify) {
                        WriteToDisk();
                    }
                    frame.remove(new Integer(out));
                    dirty.remove(new Integer(out));
                }
                frame.add(refString[i]);
                dirty.put(refString[i], modify[i]);
            }else{
                // Update dirty bit
                if(!dirty.get(refString[i]))
                    dirty.put(refString[i], modify[i]);
            }
        }
        System.out.format("MyAlgo %10d" + "%12d" + "%12d\n", pageFault, interrupt, diskWrite);
    }

    private int findVictim(){
        for(int i = 0; i < frame.size(); i++){
            if(dirty.get(frame.get(i)) == false)
                return frame.get(i);
        }
        return frame.get(0);
    }
}
