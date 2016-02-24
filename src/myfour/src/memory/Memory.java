package myfour.src.memory;

import java.util.List;

public abstract class Memory
{
	private int pointer = 0;
	private int offset = 0x0000;
	
	public abstract void storeAt(String data, int address);
	
	public abstract String readAt(int address);
	
	public abstract int getSize();
	
	public abstract void clearMemory();
	
	public void programMemory(List<String> program)
	{
		
	}
	
	public void incrementPointer()
	{
		pointer++;
	}
	
	public void decrementPointer()
	{
		pointer--;
	}
	
	public void setPointer(int address)
	{
		pointer = address;
	}
	
	public void setOffset(int offset)
	{
		this.offset = offset;
	}
	
	public int getOffset()
	{
		return offset;
	}
	
	public int getPointer()
	{
		return (pointer + offset);
	}
}
