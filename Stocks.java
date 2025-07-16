import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

public class Stocks {
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

		public long nextLong() {
			return Long.parseLong(next());
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
		public static final String INPUT_FILE = "stocks.in";
		public static final String OUTPUT_FILE = "stocks.out";

		int N;
		long B;
		long L;
		Stock[] stocks;

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			N = sc.nextInt();
			B = sc.nextLong();
			L = sc.nextLong();

			stocks = new Stock[N];

			for (int i = 0; i < N; i++) {
				long currentValue = sc.nextLong();
				long minValue = sc.nextLong();
				long maxValue = sc.nextLong();
				stocks[i] = new Stock(currentValue, minValue, maxValue);
			}

			long result = getResult();

			writer.write(Long.toString(result));
			writer.newLine();
			writer.close();
		}

		private long getResult() {
			long[][] dp = new long[(int) (B + 1)][(int) (L + 1)];

			// caz de baza, nu cumpara nicio actiune, nu investeste nimic
			for (int i = 0; i <= B; i++) {
				for (int j = 0; j <= L; j++) {
					dp[i][j] = 0;
				}
			}

			// caz general
			for (int i = 0; i < N; i++) {
				// calculez costul pentru a cumpara actiunea, profitul obtinut
				// daca o cumpar si pierderea
				long cost = stocks[i].currentValue;
				long profit = stocks[i].maxValue - stocks[i].currentValue;
				long loss = stocks[i].currentValue - stocks[i].minValue;

				for (int j = (int) B; j >= cost; j--) {
					for (int k = (int) L; k >= loss; k--) {
						// nu cumpar actiunea
						if (j < cost && k < loss) {
							dp[j][k] = dp[j][k];
						} else {
							// verific daca, cumpararea actiunii imi aduce un profit mai mare sau nu
							dp[j][k] = Math.max(dp[j][k], dp[j - (int) cost][k - (int) loss]
									+ profit);
						}
					}
				}
			}

			return dp[(int) B][(int) L];
		}
	}

	static class Stock {
		long currentValue;
		long minValue;
		long maxValue;

		public Stock(long currentValue, long minValue, long maxValue) {
			this.currentValue = currentValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}
