import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Walsh {
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
		public static final String INPUT_FILE = "walsh.in";
		public static final String OUTPUT_FILE = "walsh.out";

		int N, K;
		List<int[]> queries = new ArrayList<>();

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			N = sc.nextInt();
			K = sc.nextInt();

			for (int i = 0; i < K; i++) {
				int x = sc.nextInt();
				int y = sc.nextInt();
				queries.add(new int[]{x, y});
			}

			int[] results = getResult();

			for (int i = 0; i < K; i++) {
				writer.write(Integer.toString(results[i]));
				writer.newLine();
			}
			writer.close();
		}

		private int[] getResult() {
			int[] results = new int[K];
			for (int i = 0; i < K; i++) {
				// salvez coordonatele x si y
				int x = queries.get(i)[0];
				int y = queries.get(i)[1];
				// caut pentru fiecare pereche (x, y) elementul corespunzator
				results[i] = findElem(N	, x - 1, y - 1, false);
			}
			return results;
		}

		private int findElem(int n, int x, int y, boolean negCadr) {
			// 2^0 = 1, adica am matrice 1x1, intoarce 0
			if (n == 0) {
				if (negCadr) {
					return 0;
				} else {
					return 1;
				}
			}

			// pentru a imparti matricea in jumatati, deoarece n este o putere
			// a lui 2, nu voi elimina nicio coloana sau linie
			int halfSize = n / 2;

			// cadranul 1, stanga-sus
			if (x < halfSize && y < halfSize) {
				return findElem(halfSize, x, y, negCadr);
			} else if (x < halfSize && y >= halfSize) {
				// cadranul 2, dreapta-sus
				return findElem(halfSize, x, y - halfSize, negCadr);
			} else if (x >= halfSize && y < halfSize) {
				// cadranul 3, stanga-jos
				return findElem(halfSize, x - halfSize, y, negCadr);
			} else {
				// cadranul 4, dreapta-jos
				// negCadr devine true, deoarece cadranul 4 trebuie negat,
				// adica 0 devine 1 sau 1 devine 0
				return findElem(halfSize, x - halfSize, y - halfSize, !negCadr);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}
