package src;

public class Rabin_Karp_Monte_Carlos {
    private String pat;      // the pattern
    private long patHash;    // pattern hash value
    private int M;           // pattern length
    private long Q;          // a large prime
    private int R;           // radix
    private long RM;         // R^(M-1) % Q

    public Rabin_Karp_Monte_Carlos(String pat) {
        this.pat = pat;      // save pattern
        this.R = 10; // 256;
        this.M = pat.length();
        Q = longRandomPrime();
        RM = 1;
        for (int i = 1; i <= M-1; i++)
            RM = (R * RM) % Q;
        patHash = hash(pat, M);
    }

    public int search(String txt) {
        int N = txt.length();
        long txtHash = hash(txt, M);
        if (patHash == txtHash) {
            if (check(0)) { return 0; }
        }
        for (int i = M; i < N; i++) {
            // TextBook inplementation
            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
            txtHash = (txtHash * R + txt.charAt(i)) % Q;
            // One expression version
            //txtHash = (R * (txtHash + Q - txt.charAt(i - M) * RM % Q) + txt.charAt(i)) % Q;
            if (patHash == txtHash) {
                if (check(i - M + 1)) { return i - M + 1; }
            }
        } 
        return N;
    }


    public String searchToString(String txt) {
        StringBuilder sb = new StringBuilder();
        int N = txt.length();
        long txtHash = hash(txt, M);
        // Calculate and log initial patHash
        sb.append("Calculating initial pattern hash:\n");
        long tempPatHash = 0;
        long previousPatHash = 0;
        for (int j = 0; j < M; j++) {
            tempPatHash = (R * tempPatHash + pat.charAt(j))%Q;
            // System.out.println((R * tempPatHash + pat.charAt(j)) % Q);
            sb.append(String.format("Step %d: (%d * %d + %c) %% %d = %d\n", j+1, R, previousPatHash, pat.charAt(j), Q, tempPatHash));
            previousPatHash = tempPatHash;
        }
        
        if (patHash == txtHash && check(0))
            sb.append(String.format("Pattern found at index 0\n"));

        for (int i = M; i < N; i++) {
            long oldHash = txtHash;
            char leadingChar = txt.charAt(i - M);
            char trailingChar = txt.charAt(i);            
            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
            txtHash = (txtHash * R + txt.charAt(i)) % Q;
            sb.append(String.format("%d match %% %d = ((%d + %c*(%d - %d)) * %d + %c) %% %d = %d\n", 
                oldHash, Q, oldHash, leadingChar, Q, RM * leadingChar % Q, R, trailingChar, Q, txtHash));

            if (patHash == txtHash && check(i - M + 1)) {
                sb.append(String.format("Pattern found at index %d\n", i - M + 1));
            }
        }
        return sb.toString();
    }    

    public boolean check(int i) {
        return true;
    }

    private long hash(String key, int M) {
        long h = 0;
        for (int j = 0; j < M; j++) {
            h = (R * h + key.charAt(j)) % Q;
        }
        return h;
    }

    private long longRandomPrime() {
        return 997;
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Rabin_Karp_Monte_Carlos rk = new Rabin_Karp_Monte_Carlos("26535");
        int offset = rk.search("3141592653589793");
        System.out.println("Pattern found at index: " + offset);
        String result = rk.searchToString("3141592653589793");
        System.out.println(result);
    }   
}
