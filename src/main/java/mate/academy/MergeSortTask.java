package mate.academy;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask extends RecursiveTask<int[]> {

    private final int[] array;
    private final int divide;

    public MergeSortTask(int[] array) {
        this.array = array;
        this.divide = array.length / 2;
    }

    @Override
    protected int[] compute() {
        if (array.length > 2) {
            MergeSortTask left = createFirstSubTask(array, divide);
            MergeSortTask right = createSecondSubTask(array, divide);
            left.fork();
            right.fork();
            merge(left.join(), right.join());
        }
        if (array.length == 2) {
            compareAndSwap(0, 1);
        }
        return array;
    }

    private void merge(int[] sub0, int[] sub1) {
        int headSub0 = 0;
        int headSub1 = 0;
        for (int i = 0; i < array.length; i++) {
            boolean sub0Empty = sub0.length <= headSub0;
            boolean sub1Empty = sub1.length <= headSub1;
            if (!sub0Empty && !sub1Empty) {
                int getSub0 = sub0[headSub0++];
                int getSub1 = sub1[headSub1++];
                if (getSub0 < getSub1) {
                    array[i] = getSub0;
                    headSub1--;
                } else {
                    array[i] = getSub1;
                    headSub0--;
                }
            } else if (!sub0Empty) {
                array[i] = sub0[headSub0++];
            } else if (!sub1Empty) {
                array[i] = sub1[headSub1++];
            }
        }
    }

    private void compareAndSwap(int index0, int index1) {
        int min = Math.min(array[index0], array[index1]);
        int max = Math.max(array[index0], array[index1]);
        array[index0] = min;
        array[index1] = max;
    }

    public static MergeSortTask createFirstSubTask(int[] array, int divide) {
        return new MergeSortTask(Arrays.copyOf(array, divide));
    }

    public static MergeSortTask createSecondSubTask(int[] array, int divide) {
        return new MergeSortTask(Arrays.copyOfRange(array, divide, array.length));
    }
}
