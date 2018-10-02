import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
	ArrayList<TipoConteudo> aTipoConteudo;
	
	public Json(ArrayList<TipoConteudo> aTipoConteudo) {//Classe para montar o JSON
		this.aTipoConteudo = aTipoConteudo;
	}
	public JSONObject montaJson(String titulo, String link) { //Funcao para montar o JSON
		JSONObject myObject = new JSONObject();
		JSONArray descricao = new JSONArray();
		
		try {
			myObject.put("title", titulo);
			myObject.put("link", link);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		for(TipoConteudo t: aTipoConteudo) {
			try {
				JSONObject myObjectTemp = new JSONObject();
				if(t.getTipo().equals("links")) {
					myObjectTemp.put("type", t.getTipo());
					String[] links = t.getConteudo().split("\n");
					JSONArray content = new JSONArray();
					for(int i = 0; i<links.length; i++) {
						content.put(links[i]);
					}
					myObjectTemp.put("content", content);
				}
				else {
					myObjectTemp.put("type", t.getTipo());
					myObjectTemp.put("content", t.getConteudo());
				}
				
		        descricao.put(myObjectTemp);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			myObject.put("content", descricao);
			return myObject;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(myObject.toString());
		return null;
	}
	
	
}
