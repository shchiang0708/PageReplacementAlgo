import java.util.Random;

public class MyAlgo extends PageAlgo{
    public MyAlgo(int[] refString, int frameSize, int prob){
        super(refString, frameSize, prob);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = page.size();
            Random rand = new Random();
            int p = rand.nextInt(100);
            if(!page.contains(refString[i])){ // page fault
                pageFault++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify) {
                        WriteToDisk();
                    }
                    page.remove(new Integer(out));
                    dirty.remove(out);
                }
                page.add(refString[i]);
                if(p < prob){
                    dirty.put(refString[i], true);
                }else{
                    dirty.put(refString[i], false);
                }
            }
            else{ // current reference string is in page

                // Update dirty bit, only when the recorded dirty bit in page = 0
                // then we check the current memory reference is modify or not
                // if modify, then set dirty bit = 1
                if(p < prob){
                    dirty.put(refString[i], true);
                }
            }
        }
        System.out.format("MyAlgo %10d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }

    private int findVictim(){
        for(int i = 0; i < page.size(); i++){
            if(dirty.get(page.get(i)) == false)
                return page.get(i);
        }
        return page.get(0);
    }
}
