package com.mockaroo.banzai;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mockaroo.banzai.constraints.CustomList;
import com.mockaroo.banzai.constraints.RegularExpression;
import com.mockaroo.banzai.constraints.Type;
import com.mockaroo.banzai.constraints.Words;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;


public class OscillationOverthruster 
{
	private String apiKey;
	public Gson gson;
	
	public OscillationOverthruster(String apiKey)
	{
		this.apiKey = apiKey;
		gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'").create();
	}
	
	public <T> JsonArray generateModelDefinition(Class<T> klass) throws NoSuchFieldException, SecurityException
	{
		JsonArray columns = new JsonArray();
		
		Map<String, Field> properties = getAllProperties("", klass);
		for(Entry<String, Field> entry: properties.entrySet())
		{
			JsonObject column = generateFieldDefinition(entry.getKey(), entry.getValue());
			if (column != null)
				columns.add(column);
		}
		
		return columns;
	}
	
	public JsonObject generateFieldDefinition(String name, Field field)
	{
		JsonObject column = new JsonObject();
		column.addProperty("name", name);
		
		if (field.getAnnotation(RegularExpression.class) != null)
		{
			RegularExpression pattern = field.getAnnotation(RegularExpression.class);
			column.addProperty("type", "Regular Expression");
			column.addProperty("value", pattern.regexp());
		}
		else if (field.getAnnotation(Type.class) != null)
		{
			Type type = field.getAnnotation(Type.class);
			column.addProperty("type", type.value());
		}
		else if (field.getAnnotation(Words.class) != null)
		{
			Words words = field.getAnnotation(Words.class);
			column.addProperty("type", "Words");
			column.addProperty("min", words.min());
			column.addProperty("max", words.max());
		}
		else if (field.getAnnotation(CustomList.class) != null)
		{
			CustomList list = field.getAnnotation(CustomList.class);
			column.addProperty("type", "Custom List");
			column.add("values", gson.toJsonTree(list.values()));
		}
		else if (Date.class.equals(field.getType()))
		{
			column.addProperty("type", "Date");
		}
		else if (Number.class.equals(field.getType()))
		{
			column.addProperty("type", "Number");
			if (Integer.class.equals(field.getType()))
				column.addProperty("decimals", 0);
		}
		else 
		{
			System.out.println(field.getType());
			return null;
		}
		
		return column;
	}
	
	public <T> T mapData(Class<T> klass, JsonElement data) throws IllegalArgumentException, IllegalAccessException, ParseException, InvocationTargetException, NoSuchMethodException
	{
        T object = (T) gson.fromJson(data, klass);
		return object;
	}
	
	public <T> List<T> mapData(Class<T> klass, JsonArray data) throws IllegalArgumentException, IllegalAccessException, ParseException, InvocationTargetException, NoSuchMethodException
	{
		List<T> list = new ArrayList<T>(data.size());
		Iterator<JsonElement> it = data.iterator();
		while(it.hasNext())
		{
			JsonElement element = it.next();
			T object = (T) gson.fromJson(element, klass);
			list.add(object);
		}
        
		return list;
	}
	
	public JsonArray fetchData(JsonArray modelDefinition, int count) throws IOException
	{
		if (count < 1)
			throw new IllegalArgumentException("count needs to be 1 or more!");
		
		URL url = new URL("http://www.mockaroo.com/api/generate.json?key="+apiKey+"&count="+count);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		OutputStream os = conn.getOutputStream();
		os.write(modelDefinition.toString().getBytes());
		os.flush();
		
		String jsonStr = IOUtils.toString(conn.getInputStream());
		JsonArray data;
		if (count == 1)
		{
			JsonElement object = gson.fromJson (jsonStr, JsonElement.class);
			data = new JsonArray();
			data.add(object);
		}
		else
		{
			data = gson.fromJson (jsonStr, JsonArray.class);
		}
		
		return data;
	}
	
	public <T> String getCachedDataFilePath(Class<T> klass, int count)
	{
		String tempDir = System.getProperty("java.io.tmpdir");
		String file = klass.getName();
		String digestString = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("sha1");
			Map<String, Field> properties = getAllProperties("", klass);
			for(Entry<String, Field> entry: properties.entrySet())
			{
				Field field = entry.getValue();
				digest.update(field.getName().getBytes());
				for(Annotation annotation: field.getAnnotations())
				{
					digest.update(annotation.toString().getBytes());
				}
			}
			byte[] bytes = digest.digest();
			BigInteger bi = new BigInteger(1, bytes);
			digestString = String.format("%0" + (bytes.length << 1) + "X", bi);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String fullPath = tempDir + "/" + file + "-" + digestString + "-" + count + ".json";
		return fullPath;
	}
	
	public <T> boolean hasCachedData(Class<T> klass, int count)
	{
		String fullPath = getCachedDataFilePath(klass, count);
		System.out.println(fullPath);
		File f = new File(fullPath);
		return f.exists();
	}
	
	public <T> void cacheData(Class<T> klass, int count, JsonArray data) throws IOException
	{
		String path = getCachedDataFilePath(klass, count);
		File file = new File(path);
		
		FileWriter writer = new FileWriter(file);
		
		gson.toJson(data, writer);
		writer.close();
	}
	
	public <T> JsonArray cacheData(Class<T> klass, int count) throws IOException
	{
		String path = getCachedDataFilePath(klass, count);
		File file = new File(path);
		
		String jsonStr = IOUtils.toString(new FileInputStream(file));
		JsonArray data = gson.fromJson (jsonStr, JsonArray.class);
		return data;
	}
	
	public <T> JsonArray fetchData(Class<T> klass, int count) throws IOException, NoSuchFieldException, SecurityException
	{
		JsonArray data;
		
		if  (hasCachedData(klass, count))
		{
			System.out.println("From cache.");
			data = cacheData(klass, count);
		}
		else
		{
			System.out.println("From service.");
			JsonArray def = generateModelDefinition(klass);
			data = fetchData(def, count);
			cacheData(klass, count, data);
		}
		
		return data;
	}
	
	public Map<String, Field> getAllProperties(String baseName, Class<?> klass) throws NoSuchFieldException, SecurityException
	{
		Map<String, Field> map = new HashMap<String,Field>();
		
		PropertyDescriptor[] propertyDescriptor = BeanUtils.getPropertyDescriptors(klass);
		for (PropertyDescriptor p :propertyDescriptor)
		{
			if ("class".equals(p.getName())) continue;
			if (!BeanUtils.isSimpleProperty(p.getPropertyType()))
			{
				Map<String, Field> update = getAllProperties(p.getName()+".", p.getPropertyType());
				for(Entry<String, Field> entry: update.entrySet())
				{
					map.put(entry.getKey(), entry.getValue());
				}
			}
			else
			{
				Field field = getFieldInHierarchy(klass, p.getName());
				map.put(baseName+p.getName(), field);
			}
		}
		
		return map;
	}
	
	public Field getFieldInHierarchy(Class<?> klass, String name)
	{
		try
		{
			return klass.getDeclaredField(name);
		}
		catch (NoSuchFieldException e)
		{
			Class<?> superClass = klass.getSuperclass();
			if (superClass != null)
			{
				return  getFieldInHierarchy(superClass, name);
			}
		}
		return null;
	}
}
