import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Conexao {//Classe para criar a conexao com o servidor
	private String endereco;

	public Conexao(String endereco) {
		this.endereco = endereco;
	}

	@SuppressWarnings("finally")
	public HttpURLConnection abreConexao() {//Funcao para criar conexao com o servidor
		HttpURLConnection conn = null;
		try {
			URL url = new URL(this.endereco);

			conn = (HttpsURLConnection) url.openConnection();		
			

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return conn;
		}
	}
}
