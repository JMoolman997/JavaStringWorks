package src;

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

    private String pattern;
    private int[][] dfa;

    @Override
    public int search(String pattern, String txt) {
        return 0;
    }

    private int search(String txt) {
        int i, j, N = txt.length(), M = pattern.length();
        for (i = 0, j = 0; i < N && j < M; i++) {
            j = dfa[txt.charAt(i)][j];
        }
        if (j == M) {
            return i - M;
        }
        return N;
    }

    public void KMP(String pattern) {
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

