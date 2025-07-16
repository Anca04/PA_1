import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Badgpt {
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
		public static final String INPUT_FILE = "badgpt.in";
		public static final String OUTPUT_FILE = "badgpt.out";
		public static final long MOD = 1000000007;

		List<LetterGroup> letterGroups;

		public void solve() throws IOException {
			MyScanner sc = new MyScanner(new FileReader(INPUT_FILE));
			BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

			String compressed = sc.nextLine();
			List<LetterGroup> letterGroups = parseCompressedString(compressed);

			long result = getResult();

			writer.write(Long.toString(result));
			writer.newLine();
			writer.close();
		}

		private List<LetterGroup> parseCompressedString(String compressed) {
			letterGroups = new ArrayList<>();
			int i = 0;

			while (i < compressed.length()) {
				char letter = compressed.charAt(i);
				i++;
				long nrAparitii = 0;

				// citesc litera cu litera, adica daca am a12, eu trebuie sa obtin 12
				while (i < compressed.length() && Character.isDigit(compressed.charAt(i))) {
					nrAparitii = nrAparitii * 10 + (compressed.charAt(i) - '0');
					i++;
				}

				letterGroups.add(new LetterGroup(letter, nrAparitii));
			}

			return letterGroups;
		}

		private long getResult() {
			long count = 1;
			// parcurg perechile create astfel (litera, nrAparitii)
			for (LetterGroup letterGroup : letterGroups) {
				char ch = letterGroup.letter;
				long nrAparitii = letterGroup.nrAparitii;

				// daca intalneste n sau u, calculez in cate moduri pot sa
				// transform sirul adica daca am nnn, voi avea combinatiile
				// mn sau nm, deci 2, adica calculez fib(2) si adaug la
				// rezultat
				if ((ch == 'n' || ch == 'u')) {
					count = (count * fib(nrAparitii + 1)) % MOD;
				} else {
					// nu se modifica, deoarece exista o singura combinatie
					// pe care o adaug de la inceput
					count = count % MOD;
				}
			}
			return count;
		}

		private long fib(long n) {
			if (n == 0) {
				return 0;
			}

			if (n == 1) {
				return 1;
			}

			if (n == 2) {
				return 1;
			}

			// pentru eficientizare calculez fib(n) cu matricea [(1 1), (1, 0)]
			long[][] matrix = {{1, 1}, {1, 0}};
			// trebuie sa calculez matrix^(n - 1)
			long[][] matrixN = powerMatrix(matrix, n - 1);

			long result = matrixN[0][0];
			return result;
		}

		private long[][] powerMatrix(long[][] matrix, long n) {
			long[][] matriceaIdentitate = {{1, 0}, {0, 1}};

			if (n == 0) {
				return matriceaIdentitate;
			}

			// calculez ca la exponentierea logaritmica
			long[][] half = powerMatrix(matrix, n / 2);

			long[][] tmp_res = new long[2][2];
			tmp_res[0][0] = (half[0][0] * half[0][0] + half[0][1] * half[1][0]) % MOD;
			tmp_res[0][1] = (half[0][0] * half[0][1] + half[0][1] * half[1][1]) % MOD;
			tmp_res[1][0] = (half[1][0] * half[0][0] + half[1][1] * half[1][0]) % MOD;
			tmp_res[1][1] = (half[1][0] * half[0][1] + half[1][1] * half[1][1]) % MOD;

			long[][] result = tmp_res;

			// daca n e impar trebuie sa mai inmultesc cu un matrix
			if (n % 2 == 1) {
				long[][] aux_res = new long[2][2];
				aux_res[0][0] = (tmp_res[0][0] * matrix[0][0] + tmp_res[0][1] * matrix[1][0]) % MOD;
				aux_res[0][1] = (tmp_res[0][0] * matrix[0][1] + tmp_res[0][1] * matrix[1][1]) % MOD;
				aux_res[1][0] = (tmp_res[1][0] * matrix[0][0] + tmp_res[1][1] * matrix[1][0]) % MOD;
				aux_res[1][1] = (tmp_res[1][0] * matrix[0][1] + tmp_res[1][1] * matrix[1][1]) % MOD;

				result = aux_res;
			}

			return result;
		}
	}

	static class LetterGroup {
		char letter;
		long nrAparitii;

		public LetterGroup(char letter, long nrAparitii) {
			this.letter = letter;
			this.nrAparitii = nrAparitii;
		}
	}

	public static void main(String[] args) throws IOException {
		new Task().solve();
	}
}
