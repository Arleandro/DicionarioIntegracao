package br.com.mg.caixa.swagger;

public class LineAttribute {
	private int identityLevel;
	private String field;
	private String type;
	private String length;
	private Boolean required;
	private String format;
	
	public LineAttribute() {
		super();
	}
	
	public LineAttribute(int identityLevel) {
		this.identityLevel = identityLevel;
	}
	
	public int getIdentityLevel() {
		return identityLevel;
	}

	public void setIdentityLevel(int identityLevel) {
		this.identityLevel = identityLevel;
	}

	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getLength() {
		return length;
	}
	
	public void setLength(String length) {
		this.length = length;
	}
	
	public Boolean isRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
}
