package aop.aoptest.aop.annotation;

public enum EventType {
	ADD("1", "add"), 
	UPDATE("2", "update"), 
	DELETE("3", "delete");
	 
	private String event;
	private String name;
	  
	private EventType(String index, String name){
		this.event = index;
		this.name = name;
	}
	public String getEvent(){
		return this.event;
	}
	public String getName() {
		return name;
	}
}