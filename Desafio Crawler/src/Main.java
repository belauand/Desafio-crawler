import java.net.HttpURLConnection;

public class Main {
	static String url = "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml";
	

	public static void main(String[] args) {
		HttpURLConnection conexao = new Conexao(url).abreConexao();
		LeituraXML leitura = new LeituraXML();
		leitura.lerXML(leitura.prepararBuffer(conexao));
	}
}
