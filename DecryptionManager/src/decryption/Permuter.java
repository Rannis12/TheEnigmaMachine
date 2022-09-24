package decryption;

public class Permuter {
    private int[] perms;
    private int[] indexPerms;
    private int[] directions;
    private int[] iSwap;
    private int N; //permute 0..N-1
    private int movingPerm = N;

    static int FORWARD = +1;
    static int BACKWARD = -1;


    public Permuter(int N) {
        this.N = N;
        perms = new int[N];     // permutations
        indexPerms = new int[N];     // index to where each permutation value 0..N-1 is
        directions = new int[N];     // direction = forward(+1) or backward (-1)
        iSwap = new int[N]; //number of swaps we make for each integer at each level
        for (int i = 0; i < N; i++) {
            directions[i] = BACKWARD;
            perms[i]  = i;
            indexPerms[i] = i;
            iSwap[i] = i;
        }
        movingPerm = N;
    }

    public int[] getNext() {
        //each call returns the next permutation
        do {
            if (movingPerm == N) {
                movingPerm--;
                return perms;
            } else if (iSwap[movingPerm] > 0) {
                //swap
                int swapPerm = perms[indexPerms[movingPerm] + directions[movingPerm]];
                perms[indexPerms[movingPerm]] = swapPerm;
                perms[indexPerms[movingPerm] + directions[movingPerm]] = movingPerm;
                indexPerms[swapPerm] = indexPerms[movingPerm];
                indexPerms[movingPerm] = indexPerms[movingPerm] + directions[movingPerm];
                iSwap[movingPerm]--;
                movingPerm = N;
            } else {
                iSwap[movingPerm] = movingPerm;
                directions[movingPerm] = -directions[movingPerm];
                movingPerm--;
            }
        } while (movingPerm > 0);
        return null;
    }

}

