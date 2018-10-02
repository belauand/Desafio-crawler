import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LeituraXML {// Classe para ler o xml

	public void lerXML(StringBuffer response) {
		ArrayList<TipoConteudo> aTipoConteudo = new ArrayList<>();
		JSONArray itens = new JSONArray();
		JSONObject feed = new JSONObject();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response.toString())));
			NodeList errNodes = doc.getElementsByTagName("item");
			System.out.println(errNodes.getLength());
			int noticias = errNodes.getLength();

			int i = 0;
			while(noticias > i) {// Varro as noticias para montar o JSON
				Element err = (Element) errNodes.item(i);
				String titulo = err.getElementsByTagName("title").item(0).getTextContent();
				String link = err.getElementsByTagName("link").item(0).getTextContent();
				
				String descricao = err.getElementsByTagName("description").item(0).getTextContent();
				System.out.println("Descricao - " + StringEscapeUtils.unescapeHtml4(descricao));
				i++;
				aTipoConteudo = manipulaDescricao(descricao);
				Json json = new Json(aTipoConteudo);
				JSONObject myObject = new JSONObject();
				myObject = json.montaJson(titulo, link);
				JSONObject myObjectTemp = new JSONObject();
				
				myObjectTemp.put("item", myObject);
				
				itens.put(myObjectTemp);
			}
			feed.put("feed", itens);
			System.out.println(feed.toString());

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public StringBuffer prepararBuffer(HttpURLConnection conexao) {//Preparando o buffer para a leitura do xml
		BufferedReader in;
		StringBuffer response = new StringBuffer();
		try {
			in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

	private ArrayList<TipoConteudo> manipulaDescricao(String descricao) {//Pegando a descricao dos elementos no xml e buscando imagens, textos e links
		ArrayList<TipoConteudo> aTipoConteudo = new ArrayList<>();
		
		String p = "<(img.+?)/>";		// img
		Pattern r = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
    	Matcher m = r.matcher(descricao);
		while(m.find()) {
			aTipoConteudo.add(manipulaImagem(m.group(1)));
		}
		
		p = "<p>(.+?)</p>";
		r = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
		m = r.matcher(descricao);
		while(m.find()) {
			String texto = m.group(1);
			if(manipulaTexto(texto) != null) {
				aTipoConteudo.add(manipulaTexto(texto));
			}
		}
		
		p = "<ul>(.+?)</ul>";
		r = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
		m = r.matcher(descricao);
		while(m.find()) {
			String link = m.group(1);
			System.out.println(link);
			aTipoConteudo.add(manipulaLinks(link));
		}
		

		return aTipoConteudo;

	}
	private TipoConteudo manipulaImagem(String imagem) {//Funcao para pegar o links das imagens
		int inicio = imagem.indexOf("src");
		inicio = imagem.indexOf("\"", inicio);
		String content = "";
		
		content = imagem.substring(inicio, imagem.indexOf("\"", ++inicio)+1);

		return new TipoConteudo("image", content);

	}
	
	private TipoConteudo manipulaTexto(String texto) {//Funcao para pegar os textos e trabalhar eles
		if(texto.contains("&nbsp;")) {
			return null;
		}
		else {
			texto = texto.replaceAll("<strong>", "");
			texto = texto.replaceAll("</strong>", "");
			//Retirar os links
			String p = "<a(.+?)>";		// img
			Pattern r = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
	    	Matcher m = r.matcher(texto);
	    	
			while(m.find()) {
				System.out.println(m.group(1));
				texto = texto.replaceAll(m.group(1), "");
			}
			texto = texto.replaceAll("<a ", "");
			texto = texto.replaceAll("<a>", "");
			
			texto = texto.replaceAll("</a>", "");
			texto = texto.replaceAll("<br />", "");
			texto = StringEscapeUtils.unescapeHtml4(texto);
			 
			return new TipoConteudo("text", texto);
			
		}
	}
	
	private TipoConteudo manipulaLinks(String link) {//Funcao para pegar os links
		String p = "<li>(.+?)</li>";	
		Pattern r = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
    	Matcher m = r.matcher(link);
    	String content = "";
		while(m.find()) {
			String linkInterno = m.group(1);
			int inicio = linkInterno.indexOf("href");
			inicio = linkInterno.indexOf("\"", inicio);
			String links = linkInterno.substring(inicio, linkInterno.indexOf("\"", ++inicio)+1);
			content += links + "\n";

		}
		//System.out.println(content);
		return new TipoConteudo("links", content);
		
	}
}

