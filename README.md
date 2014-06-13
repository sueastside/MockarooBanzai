MockarooBanzai
====

Usage
----- 

Get your API key at http://www.mockaroo.com

Make a model and use the annotations
 '''
 public class MyModel 
 {
	@RegularExpression(regexp="\\d{7}")
	private Long id;
	
	@Type(value="Full Name")
    private String name;
    
    ...
 }
 '''
 
 Use the overthruster to go to the 8th dimension...
 '''
 OscillationOverthruster oscillationOverthruster = new OscillationOverthruster("<api-key>");
 JsonArray data = oscillationOverthruster.fetchData(MyModel.class, 100);
 List<MyModel> instance = oscillationOverthruster.mapData(MyModel.class, data);
 '''