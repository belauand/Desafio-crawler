
public class TipoConteudo {//Classe para montar o par conteudo / tipo de conteudo auxiliando assim a montagem do JSON
	private String tipo;
	
	private String conteudo;
	public TipoConteudo(String tipo, String conteudo) {
		this.conteudo = conteudo;
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tag) {
		this.tipo = tag;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String elemento) {
		this.conteudo = elemento;
	}
}
