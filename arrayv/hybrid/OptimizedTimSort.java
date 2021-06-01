package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class OptimizedTimSort extends Sort {
    static final int MAX_MERGE_PENDING_2 = 170;
    static final int MIN_MERGE = 32;
    static final int MIN_GALLOP = 7;

    static final int MASK_SHIFT = 31;

    public OptimizedTimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized Tim");
        this.setRunAllSortsName("Optimized Tim Sort");
        this.setRunSortName("Optimized Timsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected static int abs(int a) {
        int mask = a >> MASK_SHIFT;
        return (mask + a) ^ mask;
    }

    private int monoboundFw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;
            
            if (Reads.compareValueIndex(array, value, end - mid, 0.5, true) <= 0) {
                end -= mid;
            }
            top -= mid;
        }

        if (Reads.compareValueIndex(array, value, end - 1, 0.5, true) <= 0) {
            return end - 1;
        }
        return end;
    }

    private int monoboundBw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareIndexValue(array, start + mid, value, 0.5, true) > 0) {
                start += mid;
            }
            top -= mid;
        }

        if (Reads.compareIndexValue(array, start, value, 0.5, true) > 0) {
            return start + 1;
        }
        return start;
    }

    protected int countRun(int[] array, int lo, int hi) {
        int i = lo + 1;
        if(Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
            while(i < hi && Reads.compareIndices(array, i - 1, i, 1, true) == 1) {
                i++;
            }
            return lo - i;
        }
        else {
            while(i < hi && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) {
                i++;
            }
            return i - lo;
        }
    }

    protected void binarySortFw(int[] array, int lo, int mid, int hi) {
        if (mid == lo) {
            mid++;
        }
        for (; mid < hi; mid++) {
            int pivot = array[mid];
            int dest = monoboundFw(array, lo, mid, pivot);
            int n = mid - dest;
            switch(n) {
                case 2:  Writes.write(array, dest + 2, array[dest + 1], 1, true, false); 
                case 1:  Writes.write(array, dest + 1, array[dest], 1, true, false);
                         break;
                default: Writes.reversearraycopy(array, dest, array, dest + 1, n, 1, true, false);
            }
            Writes.write(array, dest, pivot, 1, true, false);
        }
    }

    protected void binarySortBw(int[] array, int lo, int mid, int hi) {
        if (mid == lo) {
            mid++;
        }
        for (; mid < hi; mid++) {
            int pivot = array[mid];
            int dest = monoboundBw(array, lo, mid, pivot);
            int n = mid - dest;
            switch(n) {
                case 2:  Writes.write(array, dest + 2, array[dest + 1], 1, true, false); 
                case 1:  Writes.write(array, dest + 1, array[dest], 1, true, false);
                         break;
                default: Writes.reversearraycopy(array, dest, array, dest + 1, n, 1, true, false);
            }
            Writes.write(array, dest, pivot, 1, true, false);
        }
    }

    protected void binarySort(int[] array, int lo, int mid, int hi, boolean bw) {
        if (bw) {
            binarySortBw(array, lo, mid, hi);
        } else {
            binarySortFw(array, lo, mid, hi);
        }
    }

    public void sort(int[] array, int lo, int hi) {
        int length = hi - lo;
        if (length < 2)
            return;

        if (length < MIN_MERGE) {
            int runLen = countRun(array, lo, hi);
            Highlights.clearMark(2);
            boolean bw = runLen < 0;
            binarySort(array, lo, abs(runLen), hi, bw);
            if (bw) {
                Writes.reversal(array, lo, hi - 1, 1, true, false);
            }
            return;
        }

        int[] runStack = Writes.createExternalArray(MAX_MERGE_PENDING_2);
        int[] buffer = Writes.createExternalArray(length / 2);

        Writes.deleteExternalArray(runStack);
        Writes.deleteExternalArray(buffer);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sort(array, 0, currentLength);
    }
}
