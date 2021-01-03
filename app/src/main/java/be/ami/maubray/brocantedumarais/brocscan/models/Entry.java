package be.ami.maubray.brocantedumarais.brocscan.models;

public class Entry {
	
	private String id;
	private String description;
	private int value;
	
	public Entry() {
		this.id = null;
		this.description = null;
		this.value = 0;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Entry{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", value=" + value +
				'}';
	}
}
