package com.mockaroo.banzai;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.google.gson.JsonArray;
import com.mockaroo.banzai.models.AbstractTestModel;
import com.mockaroo.banzai.models.Employee;
import com.mockaroo.banzai.models.NestedTestModel;
import com.mockaroo.banzai.models.TestModel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.junit.Test;

public class OscillationOverthrusterTest 
{
    @Test
    public void testgenerateModelDefinition() throws NoSuchFieldException, SecurityException 
    {
    	OscillationOverthruster oscillationOverthruster = new OscillationOverthruster("");
    	JsonArray def = oscillationOverthruster.generateModelDefinition(TestModel.class);
    	assertThat(def.get(0).getAsJsonObject().get("name").getAsString(), is("born"));
    	assertThat(def.get(1).getAsJsonObject().get("name").getAsString(), is("nestedTestModel.street"));
    }
    
    @Test
    public void testgetFieldInHierarchy() throws NoSuchFieldException, SecurityException 
    {
    	OscillationOverthruster oscillationOverthruster = new OscillationOverthruster("");
    	Field field = oscillationOverthruster.getFieldInHierarchy(NestedTestModel.class, "telephone");
    	assertThat(field, is(AbstractTestModel.class.getDeclaredField("telephone")));
    }
    
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException, NoSuchFieldException, SecurityException, IOException 
    {
    	OscillationOverthruster oscillationOverthruster = new OscillationOverthruster("129f9870");
    	//Class<?> klass = TestModel.class;
		Class<?> klass = Employee.class;
		
		
		JsonArray data = oscillationOverthruster.fetchData(klass, 2);
		
		Object instance = oscillationOverthruster.mapData(klass, data.get(0));
		System.out.println("-"+oscillationOverthruster.gson.toJson(instance));
    }
}
