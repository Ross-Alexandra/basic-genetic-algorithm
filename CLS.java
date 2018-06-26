import java.io.IOException;

public class CLS {
    public static void CLS() throws IOException, InterruptedException {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}
		catch (IOException | InterruptedException e) {
			System.err.println(e);
		}
    }
}
