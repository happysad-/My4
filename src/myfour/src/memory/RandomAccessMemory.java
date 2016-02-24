package myfour.src.memory;


public class RandomAccessMemory extends Memory
{
	private String[] ram;
	
	public RandomAccessMemory(int offset)
	{
		setOffset(offset);
		
		ram = new String[0x3FFF];
	}

	@Override
	public void storeAt(String data, int address)
	{
		ram[address & getOffset()] = data;
	}
	
	@Override
	public String readAt(int address)
	{
		return ram[address - getOffset()];
	}
	
	@Override
	public int getSize()
	{
		return ram.length;
	}
	
	public void loadToMemory(String[] dataArray)
	{
		for(String data : dataArray)
		{
			ram[getPointer() - getOffset()] = data;
			incrementPointer();
		}
	}
	
	@Override
	public void clearMemory()
	{
		int pointer = 0;
		
		for(@SuppressWarnings("unused") String s : ram)
		{
			ram[pointer] = "";
			pointer++;
		}
	}
}
