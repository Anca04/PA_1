import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Prinel {
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
	}

	static class Task {
		public static final String INPUT_FILE = "prinel.in";
		public static final String OUTPUT_FILE = "prinel.out";

		long N, K;
		long[] target, p;

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			N = sc.nextInt();
			K = sc.nextInt();

			target = new long[(int) N];
			long maxTarget = 0;
			for (int i = 0; i < N; i++) {
				target[i] = sc.nextLong();
				// retine cel mai mare element din target
				maxTarget = Math.max(maxTarget, target[i]);
			}

			p = new long[(int) N];
			for (int i = 0; i < N; i++) {
				p[i] = sc.nextLong();
			}

			long[] steps = computeSteps(maxTarget);

			List<Item> items = new ArrayList<>();
			for (int i = 0; i < N; i++) {
				long cost = steps[(int) target[i]];
				if (cost <= K) {
					items.add(new Item(p[i], cost));
				}
			}

			long result = getResult(items, K);

			writer.write(Long.toString(result));
			writer.newLine();
			writer.close();
		}

		private long[] computeSteps(long maxTarget) {
			long[] steps = new long[(int) (maxTarget + 1)];

			for (int i = 0; i < steps.length; i++) {
				steps[i] = Integer.MAX_VALUE;
			}

			// caz de baza
			steps[1] = 0;

			Queue<Integer> q = new LinkedList<>();
			q.add(1);

			while (!q.isEmpty()) {
				long current = q.poll();
				long currentSteps = steps[(int) current];

				for (int div : getDivisors(current)) {
					long next = current + div;
					if (next <= maxTarget && steps[(int) next] > currentSteps + 1) {
						steps[(int) next] = currentSteps + 1;
						q.add((int) next);
					}
				}
			}
			return steps;
		}

		private List<Integer> getDivisors(long n) {
			List<Integer> divisors = new ArrayList<>();
			for (int i = 1; i * i <= n; i++) {
				if (n % i == 0) {
					divisors.add(i);
					if (i != n / i) {
						divisors.add((int) (n / i));
					}
				}
			}
			return divisors;
		}

		private long getResult(List<Item> items, long capacity) {
			long[] dp = new long[(int) (capacity + 1)];

			for (Item item : items) {
				for (int w = (int) capacity; w >= item.weight; w--) {
					dp[w] = Math.max(dp[w], dp[(int) (w - item.weight)] + item.value);
				}
			}

			long max = 0;
			for (long val : dp) {
				max = Math.max(max, val);
			}
			return max;
		}

		private static class Item {
			long value;
			long weight;

			public Item(long value, long weight) {
				this.value = value;
				this.weight = weight;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}

