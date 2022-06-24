package com.API.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class SchemaValidator {
	public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
	public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

	public static JsonNode getJsonNode(String jsonText) 
			throws IOException
	{
		return JsonLoader.fromString(jsonText);
	} // getJsonNode(text) ends

	public static JsonNode getJsonNode(File jsonFile) 
			throws IOException 
	{
		return JsonLoader.fromFile(jsonFile);
	} // getJsonNode(File) ends

	public static JsonNode getJsonNode(URL url) 
			throws IOException 
	{
		return JsonLoader.fromURL(url);
	} // getJsonNode(URL) ends

	public static JsonNode getJsonNodeFromResource(String resource) 
			throws IOException
	{
		return JsonLoader.fromResource(resource);
	} // getJsonNode(Resource) ends

	public static JsonSchema getSchemaNode(String schemaText)
			throws IOException, ProcessingException 
	{
		final JsonNode schemaNode = getJsonNode(schemaText);
		return _getSchemaNode(schemaNode);
	} // getSchemaNode(text) ends

	public static JsonSchema getSchemaNode(File schemaFile)
			throws IOException, ProcessingException
	{
		final JsonNode schemaNode = getJsonNode(schemaFile);
		return _getSchemaNode(schemaNode);
	} // getSchemaNode(File) ends

	public static JsonSchema getSchemaNode(URL schemaFile)
			throws IOException, ProcessingException
	{
		final JsonNode schemaNode = getJsonNode(schemaFile);
		return _getSchemaNode(schemaNode);
	} // getSchemaNode(URL) ends
	public static JsonSchema getSchemaNodeFromResource(String resource)
			throws IOException, ProcessingException 
	{
		final JsonNode schemaNode = getJsonNodeFromResource(resource);
		return _getSchemaNode(schemaNode);
	} // getSchemaNode() ends

	public static void validateJson(JsonSchema jsonSchemaNode, JsonNode jsonNode)
			throws ProcessingException 
	{
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		if (!report.isSuccess()) {
			for (ProcessingMessage processingMessage : report) {
				throw new ProcessingException(processingMessage);
			}
		}
	} // validateJson(Node) ends
	public static boolean isJsonValid(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException
	{
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		return report.isSuccess();
	} // validateJson(Node) ends

	public static boolean isJsonValid(String schemaText, String jsonText) throws ProcessingException, IOException
	{   
		final JsonSchema schemaNode = getSchemaNode(schemaText);
		final JsonNode jsonNode = getJsonNode(jsonText);
		return isJsonValid(schemaNode, jsonNode);
	} // validateJson(Node) ends
	private static JsonSchema _getSchemaNode(JsonNode jsonNode)
			throws ProcessingException
	{
		final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
		if (null == schemaIdentifier){
			((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
		}
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		return factory.getJsonSchema(jsonNode);
	} // _getSchemaNode() ends
	public static String getJsonExpected(String jsonLocation) throws IOException {
		String json="";
		File file=new File(jsonLocation);
		if (file.exists()){
			InputStream is = new FileInputStream(file);
			json = IOUtils.toString(is);
		}
		return json;
	}
}
