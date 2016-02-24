package myfour.src.memory;

public class Register
{
	private String name;
	private int value;
	
	public Register(String name)
	{
		this.name = name;
		value = 0;
	}
	
	public void changeValue(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void increment()
	{
		value++;
	}
	
	public void decrement()
	{
		value--;
	}
	
	public String getName()
	{
		return name;
	}
}
