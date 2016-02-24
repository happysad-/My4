package myfour.src.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myfour.src.Main;

public class MemoryMap
{
	private Map<Integer, Memory> memoryMap;
	
	public MemoryMap()
	{
		memoryMap = new HashMap<Integer, Memory>();
		
		memoryMap.put(0x0000, new RandomAccessMemory(0x0000));	//[0x0000-0x3FFF]
		memoryMap.put(0x4000, new RandomAccessMemory(0x4000));	//[0x4000-0x7FFF]
		memoryMap.put(0x8000, new RandomAccessMemory(0x8000));	//[0x8000-0xBFFF]
		memoryMap.put(0xC000, new ReadOnlyMemory(0xC000));		//[0xC000-0xFFFF]
	}
	
	public void store(String data, int address)
	{
		if(address > -1)
		{
			if(address < 0xC000) // Check if address does not equal to the ROM.
			{
				for(Integer offset : memoryMap.keySet())
				{
					if(address >= offset && address < offset + 0x4000)
						memoryMap.get(offset).storeAt(data, address);
				}
			}
			else
				System.out.println("[!]: Error - address: " + Main.hex(address) + " exceeds RAM addresses.");
		}
		else
		{
			System.out.println("[!]: Error - address: " + Main.hex(address) + " is negative.");
		}
	}
	
	public void storeProgram(List<String> program)
	{
		ReadOnlyMemory rom = (ReadOnlyMemory)memoryMap.get(0xC000);
		
		rom.clearMemory();
		
		rom.programMemory(program);
	}
	
	public String read(int address)
	{
		if(address > -1 && address < 0x10000)
		{
			for(Integer offset : memoryMap.keySet())
			{
				if(address >= offset -1 && address < offset + 0x4000)
				{
					return memoryMap.get(offset).readAt(address);
				}
			}
		}
		else
			System.out.println("[!]: Error - address: " + Main.hex(address) + " is not a valid address.");
		
		return null;
	}
	
	public Map<Integer, Memory> getMap()
	{
		return memoryMap;
	}
}
