package br.com.mg.caixa.swagger;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;

public class DicionarioIntegracao {
	private int CURRENT_LINE = 1;
	private Swagger swagger;
	
	public static void main(String[] args) {
		try {
			new DicionarioIntegracao().run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void run() throws Exception {
//	    final String specification = "D:\\gitlab\\ServicosSP\\CONSULTA_CONTA_API\\swagger.json";
//	    final String specification = "C:\\Users\\Arleandro\\Desktop\\Magna Sistemas\\Caixa\\Cartões\\ELO\\ELO - docs_Nemezio\\TSP api v2.36\\OTDPS_Teste.json";
//	    final String specification = "C:\\Users\\Arleandro\\Desktop\\Magna Sistemas\\Caixa\\Cartões\\ELO\\ELO - docs_Nemezio\\TSP api v2.36\\OTDPS_Issuer_API_2.36.0.swagger.json";
//	    final String specification = "C:\\Users\\Arleandro\\Desktop\\swagger\\FIX_BEM_GARANTIDOR_API.json";
//	    final String specification = "C:\\Users\\Arleandro\\Desktop\\swagger\\BAR_BLOQUEIO_GARANTIA_API.json";
//	    final String specification = "C:\\Users\\Arleandro\\Desktop\\swagger\\LCR_EVENTO_CONTABIL_API.json";
	    final String specification = "C:\\Users\\Arleandro\\Desktop\\swagger\\OTDPS_Issuer_API_2.36.0.swagger.json";
//	    final String specification = "C:\\Users\\Arleandro\\IBM\\IIBT10\\workspace\\POC_HABITACAO\\swagger.json";
	    
	    swagger = new SwaggerParser().read(specification);
	    
	    Map<String, Path> paths = swagger.getPaths();
	    
	    Set<String> keysPaths = paths.keySet();
	    
	    Iterator<String> itKeys = keysPaths.iterator();
	    
	    while (itKeys.hasNext()) {
	    	String pathStr = itKeys.next();
	    	
	    	Path path = paths.get(pathStr);
	    	
			List<Operation> operations = path.getOperations();
			
			Iterator<Operation> opIt = operations.iterator();
			
			while (opIt.hasNext()) {
				Operation operation = (Operation) opIt.next();
				
				HTTP_Method httpMethod = getHTTPMethod(path, operation);
				
				switch (httpMethod) {
				case POST:
				case DELETE:
						File f = new File("C:\\Users\\Arleandro\\Desktop\\swagger\\Dicionarios\\" + operation.getOperationId() + ".xls");
						
					    try  {
					        Workbook wb = new HSSFWorkbook();
					        Sheet sheet = wb.createSheet(operation.getOperationId());
					        
					        Row row = sheet.createRow(1);
					        row.createCell(1).setCellValue("Barramento - Dicionário de Integração - " + operation.getOperationId());
					        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));
					        
					        createInputDatas(wb, operation, sheet, row, pathStr);
					    	
					    	OutputStream fileOut = new FileOutputStream(f);
					        wb.write(fileOut);
					        wb.close();
					    } catch(Exception e) {
					    	e.printStackTrace();
					    }
					    
						break;
	
					default:
						break;
				}
				
			}
		}
	}
	
	private void createInputDatas(Workbook wb, Operation operation, Sheet sheet, Row row, String pathStr) {
        
    	CURRENT_LINE = 7;
    	
    	createHeaderInputData(wb, sheet, row, pathStr);
    	
    	createHeaderAttributes(operation, sheet, row);
    	
        createPathAttributes(operation, sheet, row);
        
        createQueryAttributes(operation, sheet, row);

        createBodyAttributes(wb, operation, sheet, row);
        
        PropertyTemplate pt = new PropertyTemplate();  
        pt.drawBorders(new CellRangeAddress(5, CURRENT_LINE - 1, 1, 7), BorderStyle.THIN, BorderExtent.ALL); 
        pt.applyBorders(sheet);
        
        sheet.setColumnWidth(0, 300);
        sheet.setColumnWidth(6, 10000);
        
        row = sheet.createRow(0);
        sheet.getRow(0).setHeight((short)150);
        
        row = sheet.createRow(2);
        sheet.getRow(2).setHeight((short)100);
        
        row = sheet.createRow(4);
        sheet.getRow(4).setHeight((short)100);
	}
	
	private void createHeaderInputData(Workbook wb, Sheet sheet, Row row, String pathStr) {
        row = sheet.createRow(3);
        
        CellStyle style = wb.createCellStyle(); 
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());  
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
        
        Cell cell = row.createCell(1);
        cell.setCellValue("DADOS DE ENTRADA DO SERVIÇO");
//        cell.setCellStyle(style);  
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));
        
        row = sheet.createRow(5);
        row.createCell(1).setCellValue("Contexto Chamado/Comando HTTP");
        row.createCell(2).setCellValue("CAMPO");
        row.createCell(3).setCellValue("TIPO");
        row.createCell(4).setCellValue("TAMANHO");
        row.createCell(5).setCellValue("OBRIGATÓRIO");
        row.createCell(6).setCellValue("FORMATO");
        row.createCell(7).setCellValue("ENDPOINT");
        
        CellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        
        row = sheet.createRow(6);
        cell = row.createCell(1);
		cell.setCellValue("Comando HTTP POST no contexto \n\"" + pathStr + "\" : ");
		cell.setCellStyle(cs);
		row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
        sheet.autoSizeColumn(1);
        
//        row.createCell(1).setCellValue("Comando HTTP POST no contexto \n\"" + pathStr + "\" : ");
	}
	
	private void createHeaderAttributes(Operation operation, Sheet sheet, Row row) {
        List<Parameter> headerParams = getHeaderParams(operation.getParameters());
        
        int headerSize = headerParams.size();
        
        if(headerSize > 0) {
	        row = sheet.createRow(CURRENT_LINE);
	        
        	if(headerSize > 1) {
        		sheet.addMergedRegion(new CellRangeAddress(CURRENT_LINE, CURRENT_LINE + (headerSize - 1), 1, 1));
        	}
        	
        	row.createCell(1).setCellValue("header");
        	
	        Iterator<Parameter> itParams = headerParams.iterator();
	        
	        while (itParams.hasNext()) {
				Parameter param = (Parameter) itParams.next();
		        row.createCell(2).setCellValue(param.getName());
		        row = sheet.createRow(++CURRENT_LINE);
			}
        }
	}
	
	private void createPathAttributes(Operation operation, Sheet sheet, Row row) {
			
        List<Parameter> pathParams = getPathParams(operation.getParameters());
        
        int pathSize = pathParams.size();
        
        if(pathSize > 0) {
	        row = sheet.createRow(CURRENT_LINE);
	        
        	if(pathSize > 1) {
        		sheet.addMergedRegion(new CellRangeAddress(CURRENT_LINE, CURRENT_LINE + (pathSize - 1), 1, 1));
        	}
        	
        	row.createCell(1).setCellValue("path");
        	
	        Iterator<Parameter> itParams = pathParams.iterator();
	        
	        while (itParams.hasNext()) {
				Parameter param = (Parameter) itParams.next();
		        row.createCell(2).setCellValue(param.getName());
		        row = sheet.createRow(++CURRENT_LINE);
			}
        }
	}
	
	private void createQueryAttributes(Operation operation, Sheet sheet, Row row) {
        List<Parameter> queryParams = getQueryParams(operation.getParameters());
        
        int querySize = queryParams.size();
        
        if(querySize > 0) {
	        row = sheet.createRow(CURRENT_LINE);
	        
        	if(querySize > 1) {
        		sheet.addMergedRegion(new CellRangeAddress(CURRENT_LINE, CURRENT_LINE + (querySize - 1), 1, 1));
        	}
        	
        	row.createCell(1).setCellValue("query");
        	
	        Iterator<Parameter> itParams = queryParams.iterator();
	        
	        while (itParams.hasNext()) {
				Parameter param = (Parameter) itParams.next();
		        row.createCell(2).setCellValue(param.getName());
		        row = sheet.createRow(++CURRENT_LINE);
			}
        }
	}
	
	private void createBodyAttributes(Workbook wb, Operation operation, Sheet sheet, Row row) {
        BodyParameter rootParam = getBodyParams(operation.getParameters());
        
        if(rootParam != null) {
        	
        	Model rootSchema = rootParam.getSchema();
        	
        	if(rootSchema.getReference() != null) {
	        	Model bodyModel = swagger.getDefinitions().get(rootSchema.getReference().substring(14));
	        	
	        	List<LineAttribute> allBodyAttributes = getAttributes(bodyModel, 0);
	        	
	        	createBodyAttributesItens(wb, sheet, row, allBodyAttributes);
        	}
        }
	}
	
	private void createBodyAttributesItens(Workbook wb, Sheet sheet, Row row, List<LineAttribute> allBodyAttributes) {
        row = sheet.createRow(CURRENT_LINE);
        
    	if(allBodyAttributes.size() > 0) {
    		sheet.addMergedRegion(new CellRangeAddress(CURRENT_LINE, (CURRENT_LINE + allBodyAttributes.size()) -1, 1, 1));
        	
        	row.createCell(1).setCellValue("body");
        	
        	Iterator<LineAttribute> it = allBodyAttributes.iterator();
        	
        	while (it.hasNext()) {
				LineAttribute attribute = (LineAttribute) it.next();
				
				char[] fill = new char[attribute.getIdentityLevel() * 4];
				
				Arrays.fill(fill, ' ');
				
		        row.createCell(2).setCellValue(new String(fill) + attribute.getField());
		        row.createCell(3).setCellValue(attribute.getType());
		        row.createCell(4).setCellValue(attribute.getLength());
		        
		        if(attribute.isRequired() != null) {
		        	row.createCell(5).setCellValue(attribute.isRequired() ? "SIM" : "NÃO");
		        }
		        
		        CellStyle cs = wb.createCellStyle();
		        cs.setWrapText(true);
		        
		        sheet.autoSizeColumn(1);

		        Cell cell = row.createCell(6);
				cell.setCellStyle(cs);
		        cell.setCellValue(attribute.getFormat());
		        
		        row = sheet.createRow(++CURRENT_LINE);
			}
    	}
	}
	
	private List<LineAttribute> getAttributes(Model model, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
    	Map<String, Property> mapProperties = model.getProperties();
    	
    	if(mapProperties == null) {
    		return allBodyAttributes;
    	}
    	
    	Set<String> y = mapProperties.keySet();
    	
    	Iterator<String> i = y.iterator();
    	
    	while (i.hasNext()) {
			String attributeName = (String) i.next();
			
			Property p = mapProperties.get(attributeName);
			
			if(RefProperty.class.isAssignableFrom(p.getClass())) {
				List<LineAttribute> list = getComplexAttribute(p, attributeName, identityLevel);
				
				allBodyAttributes.addAll(list);
			} else if(ObjectProperty.class.isAssignableFrom(p.getClass())) {
				List<LineAttribute> list = getNestedObjectAttribute(p, attributeName, identityLevel);
				
				allBodyAttributes.addAll(list);
			} else if(ArrayProperty.class.isAssignableFrom(p.getClass())) {
				ArrayProperty arrayProperty = (ArrayProperty)p;
				
				List<LineAttribute> list = getArrayAttribute(arrayProperty, attributeName, identityLevel);
				
				allBodyAttributes.addAll(list);
			} else {
				LineAttribute lineAttribute = getSimpleAttribute(p, attributeName, identityLevel);
				
				allBodyAttributes.add(lineAttribute);
			}
		}
    	
    	return allBodyAttributes;
	}
	
	public List<LineAttribute> getComplexAttribute(Property property, String attributeName, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
		RefProperty refProperty = (RefProperty)property;
		
		LineAttribute lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("\"" + attributeName + "\": {");
		lineAttribute.setType("object");
		lineAttribute.setRequired(property.getRequired());
		
		allBodyAttributes.add(lineAttribute);
		
        Model innerModel = swagger.getDefinitions().get(refProperty.get$ref().substring(14));
        
        List<LineAttribute> attrs = getAttributes(innerModel, (identityLevel + 1) );
        
        allBodyAttributes.addAll(attrs);
        
        lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("}");
		lineAttribute.setType("");
		
		allBodyAttributes.add(lineAttribute);
		
		return allBodyAttributes;
	}
	
	public List<LineAttribute> getNestedObjectAttribute(Property property, String attributeName, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
		ObjectProperty objectProperty = (ObjectProperty)property;
		
		LineAttribute lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("\"" + attributeName + "\": {");
		lineAttribute.setType("object");
		lineAttribute.setRequired(property.getRequired());
		
		allBodyAttributes.add(lineAttribute);
		
		Map<String, Property> map = objectProperty.getProperties();
		
		Set<String> key = map.keySet();
		
		Iterator<String> it = key.iterator();
		
		while (it.hasNext()) {
			String propertyName = (String) it.next();
			
			Property prop = map.get(propertyName);
			
			if(RefProperty.class.isAssignableFrom(prop.getClass())) {
				List<LineAttribute> list = getComplexAttribute(prop, propertyName, (identityLevel + 1));
				
				allBodyAttributes.addAll(list);
			} else if(ObjectProperty.class.isAssignableFrom(prop.getClass())) {
				List<LineAttribute> list = getNestedObjectAttribute(prop, propertyName, (identityLevel + 1));
				
				allBodyAttributes.addAll(list);
			} else if(ArrayProperty.class.isAssignableFrom(prop.getClass())) {
				ArrayProperty arrayProperty = (ArrayProperty)prop;
				
				List<LineAttribute> list = getArrayAttribute(arrayProperty, propertyName, (identityLevel + 1));
				
				allBodyAttributes.addAll(list);
			} else {
				LineAttribute att = getSimpleAttribute(prop, propertyName, (identityLevel + 1));
				
				allBodyAttributes.add(att);
			}
		}
		
        lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("}");
		lineAttribute.setType("");
		
		allBodyAttributes.add(lineAttribute);
		
		return allBodyAttributes;
	}
	
	public List<LineAttribute> getArrayAttribute(ArrayProperty arrayProperty, String attributeName, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
		if(RefProperty.class.isAssignableFrom(arrayProperty.getItems().getClass())) {
			allBodyAttributes = getComplexArrayAttribute(arrayProperty, attributeName, identityLevel);
		} else {
			allBodyAttributes = getSimpleArrayAttribute(arrayProperty, attributeName, identityLevel);
		}
		
		return allBodyAttributes;
	}
	
	public List<LineAttribute> getComplexArrayAttribute(ArrayProperty arrayProperty, String attributeName, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
		RefProperty ppp = (RefProperty)arrayProperty.getItems();
		
		LineAttribute lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("\"" + attributeName + "\": [{");
		lineAttribute.setType("array");
		lineAttribute.setRequired(arrayProperty.getRequired());
		
		allBodyAttributes.add(lineAttribute);
		
        Model innerModel = swagger.getDefinitions().get(ppp.get$ref().substring(14));
        
        List<LineAttribute> attrs = getAttributes(innerModel, (identityLevel + 1) );
        
        allBodyAttributes.addAll(attrs);
        
        lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("}]");
		lineAttribute.setType("");
		
		allBodyAttributes.add(lineAttribute);
		
		return allBodyAttributes;
	}
	
	public List<LineAttribute> getSimpleArrayAttribute(ArrayProperty arrayProperty, String attributeName, int identityLevel) {
		List<LineAttribute> allBodyAttributes = new ArrayList<>();
		
		LineAttribute lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("\"" + attributeName + "\": [");
		lineAttribute.setType(arrayProperty.getType());
		lineAttribute.setRequired(arrayProperty.getRequired());
		allBodyAttributes.add(lineAttribute);
		
		lineAttribute = new LineAttribute(identityLevel + 1);
		lineAttribute.setField("\"itemArray\":");
		lineAttribute.setType(arrayProperty.getItems().getType());
		allBodyAttributes.add(lineAttribute);
		
        lineAttribute = new LineAttribute(identityLevel);
		lineAttribute.setField("]");
		lineAttribute.setType("");
		
		allBodyAttributes.add(lineAttribute);

		return allBodyAttributes;
	}
	
	public LineAttribute getSimpleAttribute(Property p, String attributeName, int identityLevel) {
		LineAttribute lineAttribute = new LineAttribute(identityLevel);
		
		if(StringProperty.class.isAssignableFrom(p.getClass())) {
			StringProperty str = (StringProperty) p;
			
			if(str.getMaxLength() != null)
				lineAttribute.setLength("" + str.getMaxLength());
			
			if(str.getEnum() != null) {
				List<String> listEnum = str.getEnum();
				
				StringBuffer valueEnum = new StringBuffer();
				
				for (String string : listEnum) {
					valueEnum.append(string + ",\n");
				}
				
				valueEnum.delete(valueEnum.length() - 2, valueEnum.length());
				
				lineAttribute.setFormat(valueEnum.toString());
			}
		}
		
		if(AbstractNumericProperty.class.isAssignableFrom(p.getClass())) {
			AbstractNumericProperty num = (AbstractNumericProperty) p;
			
			if(num.getMaximum() != null)
				lineAttribute.setLength(getNumberLength(num.getMaximum()));
		}
		
		lineAttribute.setField("\"" + attributeName + "\":");
		lineAttribute.setType(p.getType());
//		if(p.getFormat() != null)
//			lineAttribute.setFormat(p.getFormat());
		lineAttribute.setRequired(p.getRequired());
		
		return lineAttribute;
	}
	
	private String getNumberLength(BigDecimal value) {
		String num = "" + value.toString();
		
		int index = num.indexOf(".");
		
		if(index != -1) {
			return num.substring(0, index).length() + "," + num.substring(index + 1).length(); 
		} else {
			return "" + num.length();
		}
	}
	
	private List<Parameter> getHeaderParams(List<Parameter> params) {
		List<Parameter> headerParams = new ArrayList<Parameter>();
		
        Iterator<Parameter> it = params.iterator();
        
        while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			
			if(param.getIn().equalsIgnoreCase("header")) {
				headerParams.add(param);
			}
		}
		
		return headerParams;
	}
	
	private List<Parameter> getQueryParams(List<Parameter> params) {
		List<Parameter> queryParams = new ArrayList<Parameter>();
		
        Iterator<Parameter> it = params.iterator();
        
        while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			
			if(param.getIn().equalsIgnoreCase("query")) {
				queryParams.add(param);
			}
		}
		
		return queryParams;
	}
	
	private List<Parameter> getPathParams(List<Parameter> params) {
		List<Parameter> pathParams = new ArrayList<Parameter>();
		
        Iterator<Parameter> it = params.iterator();
        
        while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			
			if(param.getIn().equalsIgnoreCase("path")) {
				pathParams.add(param);
			}
		}
		
		return pathParams;
	}
	
	private BodyParameter getBodyParams(List<Parameter> params) {
        Iterator<Parameter> it = params.iterator();
        
        while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			
			if(param.getIn().equalsIgnoreCase("body")) {
				return (BodyParameter)param;
			}
		}
		
		return null;
	}
	
	private HTTP_Method getHTTPMethod(Path path, Operation operation) {
		if(path.getDelete() == operation)
			return HTTP_Method.DELETE;
		
		if(path.getGet() == operation)
			return HTTP_Method.GET;
		
		if(path.getHead() == operation)
			return HTTP_Method.HEAD;
		
		if(path.getOptions() == operation)
			return HTTP_Method.OPTIONS;
		
		if(path.getPatch() == operation)
			return HTTP_Method.PATCH;
		
		if(path.getPost() == operation)
			return HTTP_Method.POST;
		
		if(path.getPut() == operation)
			return HTTP_Method.PUT;
		
		return null;
	}

}
