public class factor {

	public static void main(String[] args) {
		int factorValue = Integer.parseInt(args[0]);

			for (int i = 1; i <= factorValue; i++) {
				if (factorValue % i == 0)
					System.out.println(i);
		}

	}

}
