package mate.academy;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class MergeSortAction extends RecursiveAction {

    private final int[] array;

    public MergeSortAction(int[] array) {
        this.array = array;
    }

    @Override
    protected void compute() {
        if (array.length > 2) {
            MergeSortTask subTask = new MergeSortTask(Arrays.copyOf(array, array.length));
            subTask.fork();
            int[] tmp = subTask.join();
            System.arraycopy(tmp, 0, array, 0, tmp.length);
        } else if (array.length == 2) {
            int min = Math.min(array[0], array[1]);
            int max = Math.max(array[0], array[1]);
            array[0] = min;
            array[1] = max;
        }
    }
}
