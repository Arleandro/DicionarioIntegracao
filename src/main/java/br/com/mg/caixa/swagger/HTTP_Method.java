package br.com.mg.caixa.swagger;

public enum HTTP_Method {
	GET ("GET"),
	POST ("POST"),
	PUT ("PUT"),
	PATCH ("PATCH"),
	DELETE ("DELETE"),
	HEAD ("HEAD"),
	OPTIONS ("OPTIONS");
	
    private final String method;
    
    private HTTP_Method(String method) {
    	this.method = method;
    }
    
    public static HTTP_Method forValue(String value) {
        if (value.equalsIgnoreCase("GET")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("POST")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("PUT")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("PATCH")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("DELETE")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("HEAD")) {
            return POST;
        }
        
        if (value.equalsIgnoreCase("OPTIONS")) {
            return POST;
        }
        
        return null;
    }

	public String getMethod() {
		return method;
	}    
}
