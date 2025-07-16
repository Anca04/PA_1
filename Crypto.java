import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Crypto {
	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}

	static class Task {
		public static final String INPUT_FILE = "crypto.in";
		public static final String OUTPUT_FILE = "crypto.out";
		public static final int MOD = 1000000007;

		long N, L;
		char[] K;
		char[] S;
		Set<Character> uniqueChars;

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			N = sc.nextInt();
			L = sc.nextInt();

			K = sc.next().toCharArray();

			S = sc.next().toCharArray();

			uniqueChars = new HashSet<>();
			for (char c : S) {
				uniqueChars.add(c);
			}

			long result = getResult();

			writer.write(Long.toString(result));
			writer.newLine();
			writer.close();
		}

		private long getResult() {
			// TO DO
			long count = 0;
			// numar cate ? am
			for (char c : K) {
				if (c == '?') {
					count++;
				}
			}

			// daca nu am niciun ?, atunci numar ca in mod obisnuit, sirul e
			// format deja
			if (count == 0) {
				return countSubstring(K, S);
			}

			long[][] dp = new long[(int) (N + 1)][(int) (L + 1)];

			// caz de baza
			dp[0][0] = 1;

			// caz general
			for (int i = 0; i < N; i++) {
				for (int j = 0; j <= L; j++) {
					if (K[i] == '?') {
						// inlocuiesc ? cu litere din S
						for (char c : uniqueChars) {
							replace(dp, i, j, c);
						}
					} else {
						// nu am ce inlocui, am litera deja
						replace(dp, i, j, K[i]);
					}
				}
			}

			return dp[(int) N][(int) L];
		}

		private void replace(long[][] dp, int i, int j, char c) {
			// verific daca caracterul meu este cel din S
			if (j < L && S[j] == c) {
				dp[i + 1][j + 1] = (dp[i + 1][j + 1] + dp[i][j]) % MOD;
			}

			// pot ignora litera, adica am format deja pana la j
			dp[i + 1][j] = (dp[i + 1][j] + dp[i][j]) % MOD;
		}

		private long countSubstring(char[] a, char[] b) {
			long[][] dp = new long[a.length + 1][b.length + 1];

			// caz de baza
			for (int i = 0; i <= a.length; i++) {
				dp[i][0] = 1;
			}

			// caz general
			for (int i = 1; i <= a.length; i++) {
				for (int j = 1; j <= b.length; j++) {
					// daca am acelasi caracter, fie folosesc litera, fie nu
					if (a[i - 1] == b[j - 1]) {
						dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j]) % MOD;
					} else {
						// il ignora
						dp[i][j] = dp[i - 1][j];
					}
				}
			}

			return dp[a.length][b.length];
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}