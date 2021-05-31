package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class OptimizedTimSort extends Sort {
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
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        
    }
}
