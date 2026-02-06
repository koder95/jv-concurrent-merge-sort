package mate.academy;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class MergeSortAction extends RecursiveAction {

    private final int[] array;
    private final int start;
    private final int endExclusive;

    public MergeSortAction(int[] array) {
        this(array, 0, array.length);
    }

    public MergeSortAction(int[] array, int start, int endExclusive) {
        this.array = array;
        this.start = start;
        this.endExclusive = endExclusive;
    }

    @Override
    protected void compute() {
        int length = endExclusive - start;
        if (length > 2) {
            int divide = (start + endExclusive) / 2;
            MergeSortAction subAction0 = new MergeSortAction(array, start, divide);
            MergeSortAction subAction1 = new MergeSortAction(array, divide, endExclusive);
            subAction0.fork();
            subAction1.fork();
            subAction0.join();
            subAction1.join();
            merge(
                    subAction0.start, subAction0.endExclusive,
                    subAction1.start, subAction1.endExclusive
            );
        } else if (length == 2) {
            compareAndSwap(start, start + 1);
        }
    }

    private void compareAndSwap(int index0, int index1) {
        int minI = Math.min(index0, index1);
        int maxI = Math.max(index0, index1);
        int min = Math.min(array[minI], array[maxI]);
        int max = Math.max(array[minI], array[maxI]);
        array[minI] = min;
        array[maxI] = max;
    }

    private void merge(int sub0Start, int sub0EndExclusive, int sub1Start, int sub1EndExclusive) {
        int[] sub0Copy = Arrays.copyOfRange(array, sub0Start, sub0EndExclusive);
        int[] sub1Copy = Arrays.copyOfRange(array, sub1Start, sub1EndExclusive);
        int headSub0 = 0;
        int headSub1 = 0;
        for (int i = start; i < endExclusive; i++) {
            boolean sub0Empty = sub0Copy.length <= headSub0;
            boolean sub1Empty = sub1Copy.length <= headSub1;
            if (!sub0Empty && !sub1Empty) {
                if (sub0Copy[headSub0] < sub1Copy[headSub1]) {
                    array[i] = sub0Copy[headSub0++];
                } else {
                    array[i] = sub1Copy[headSub1++];
                }
            } else if (!sub0Empty) {
                array[i] = sub0Copy[headSub0++];
            } else if (!sub1Empty) {
                array[i] = sub1Copy[headSub1++];
            }
        }
    }
}
