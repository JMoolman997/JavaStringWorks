package src;

import java.math.BigInteger;
import java.util.Random;

public interface SubStringSearch {

    int search(String pattern, String txt );

}
class BruteForceSubStringSearch implements SubStringSearch {

    @Override
    public int search(String pattern, String txt) {
        int M = pattern.length();
        int N = txt.length();
        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++) {
                if (txt.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == M) {
                return i;
            }
        }
        return N;
    }
}
class KMPSubStringSearch implements SubStringSearch {

    class KMP {

        private String pattern;
        private int[][] dfa;

        public int search(String txt) {
            int i, j, N = txt.length(), M = pattern.length();
            for (i = 0, j = 0; i < N && j < M; i++) {
                j = dfa[txt.charAt(i)][j];
            }
            if (j == M) {
                return i - M;
            }
            return N;
        }
    
        public KMP(String pattern) {
            this.pattern = pattern;
            int M = pattern.length();
            int R = 256;
            dfa = new int[R][M];
            dfa[pattern.charAt(0)][0] = 1;
            for (int X = 0, j = 1; j < M; j++) {
                for (int c = 0; c < R; c++) {
                    dfa[c][j] = dfa[c][X];
                }
                dfa[pattern.charAt(j)][j] = j + 1;
                X = dfa[pattern.charAt(j)][X];
            }
    
        }

    }

    @Override
    public int search(String pattern, String txt) {
        KMP kmp = new KMP(pattern);
        int offset = kmp.search(txt);
        return offset;
    }
}
class RabinKarpSubStringSearch_Monte_Carlo implements SubStringSearch {
    
    class RabinKarp {
        private long patHash;
        private int M;
        private long Q;
        private int R = 256;
        private long RM; // R^(M-1) % Q

        public RabinKarp(String pattern) {
            this.M = pattern.length();
            Q = longRandomPrime();
            RM = 1;
            for (int i = 1; i <= M - 1; i++) {
                RM = (R * RM) % Q;
            }
            patHash = hash(pattern,M);    
        }

        // a random 31-bit prime
        private long longRandomPrime() {
            BigInteger prime = BigInteger.probablePrime(31, new Random());
            return prime.longValue();
        }

        // Compute hash for key[0..m-1].
        private long hash(String key, int m) {
            long h = 0;
            for (int j = 0; j < m; j++)
                h = (R * h + key.charAt(j)) % Q;
            return h;
        }        

    }
    @Override
    public int search(String pattern, String txt) {
        return 0;
    }
}


