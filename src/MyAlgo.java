public class MyAlgo extends PageAlgo{
    public MyAlgo(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = page.size();
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
                dirty.put(refString[i], modify[i]);
            }
            else{ // current reference string is in page
//              Update dirty bit, if modify bit = 1, cost++
                if(!dirty.get(refString[i])) {
                    dirty.put(refString[i], modify[i]);
                }
            }
//          if the current memory reference is modify, menas we need to set dirty bit
//          cost++
            if(modify[i] == true)
                cost++;
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
