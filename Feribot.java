import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

public class Feribot {
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
		public static final String INPUT_FILE = "feribot.in";
		public static final String OUTPUT_FILE = "feribot.out";

		int N, K;
		long[] weights;

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			N = sc.nextInt();
			K = sc.nextInt();
			weights = new long[N];

			for (int i = 0; i < N; i++) {
				weights[i] = sc.nextLong();
			}

			long result = getResult();

			writer.write(Long.toString(result));
			writer.newLine();
			writer.close();
		}

		private long getResult() {
			// trebuie sa caut in intervalul celei mai grele masini
			// si suma tuturor masinilor
			// astfel, voi folosi binary search pentru a gasi valoarea minima
			long heaviest = Long.MIN_VALUE;
			long sum_all = 0;

			// am obtinut intervalul
			for (int i = 0; i < N; i++) {
				sum_all += weights[i];
				if (weights[i] > heaviest) {
					heaviest = weights[i];
				}
			}

			// acum caut cu binary search care ar fi costul minim
			while (heaviest < sum_all) {
				long mid = (heaviest + sum_all) / 2;

				// verific daca masinile pot fi distribuite pe k feriboturi
				if (canGroup(mid)) {
					sum_all = mid;
				} else {
					heaviest = mid + 1;
				}
			}

			return heaviest;
		}

		private boolean canGroup(long cost) {
			int count = 1;
			long sum_cost = 0;

			// parcurg weights si daca suma este mai mica decat costul, atunci
			// pot sa o adaug pe feribot, altfel nu, si trebuie sa o pun pe
			// alt feribot (unul nou)
			// daca a depasit k, nu pot sa le distribui pe k feriboturi
			for (int i = 0; i < N; i++) {
				if (sum_cost + weights[i] <= cost) {
					sum_cost += weights[i];
				} else {
					count++;
					sum_cost = weights[i];
					if (count > K) {
						return false;
					}
				}
			}

			return true;
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}
