public class ESC extends PageAlgo{

    public ESC(int[] refString, boolean[] modify, int frameSize){
        super(refString, modify, frameSize);
    }
    @Override
    public void run() {
        for(int i = 0; i < refString.length; i++){
            int size = page.size();
            if(!page.contains(refString[i])){
                pageFault++;
                if(size == frameSize){ // frame is full
                    int out = findVictim();
                    boolean isModify = dirty.get(out);
                    if(isModify)
                        WriteToDisk();
                    ref.remove(out);
                    page.remove(new Integer(out));
                    dirty.remove(out);
                }
                page.add(refString[i]);
                ref.put(refString[i], false);
                dirty.put(refString[i], modify[i]);
            }
            else{// current reference string is in page

                // if frame contains current string, set reference bit = 1
                ref.put(refString[i], true);
                // Update dirty bit, only when the recorded dirty bit in page = 0
                // then we check the current memory reference is modify or not
                // if modify, then set dirty bit = 1
                if(dirty.get(refString[i]) == false)
                    dirty.put(refString[i], modify[i]);
            }
        }
        System.out.format("ESC %13d" + "%12d" + "%12d\n", pageFault, cost, diskWrite);
    }

    private int findVictim(){
        // 1. find (0, 0)
        for(int i = 0; i < page.size(); i++){
            if(ref.get(page.get(i)) == false && dirty.get(page.get(i)) == false){
                return page.get(i);
            }
        }
        // 2. find (0, 1)
        for(int i = 0; i < page.size(); i++){
            if(ref.get(page.get(i)) == false && dirty.get(page.get(i)) == true){
                return page.get(i);
            }
        }
        // 3. clear reference bit, cost++
        for(int i = 0; i < page.size(); i++){
            ref.put(page.get(i), false);
        }
        cost++;
        // 4. find (0, 0) again
        for(int i = 0; i < page.size(); i++){
            if(ref.get(page.get(i)) == false && dirty.get(page.get(i)) == false){
                return page.get(i);
            }
        }
        // 5. find (0, 1) again
        for(int i = 0; i < page.size(); i++){
            if(ref.get(page.get(i)) == false && dirty.get(page.get(i)) == true){
                return page.get(i);
            }
        }

        return page.get(0);
    }
}
