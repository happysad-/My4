package myfour.src.memory;

import java.util.List;

import myfour.src.Main;

public class ReadOnlyMemory extends Memory
{
	private String[] rom;
	
	/**
	 * Create ROM with a specified size.
	 * If size is not specified, 16k rom will be created.
	 * @param size Size of read only memory.
	 */
	public ReadOnlyMemory(int offset)
	{
		setOffset(offset);
		
		rom = new String[0x3FFF];
	}
	
	@Override
	public void storeAt(String data, int address)
	{
		
	}
	
	@Override
	public void programMemory(List<String> program)
	{
		for(String code : program)
		{
			if((getPointer() - getOffset())< rom.length)
			{
				rom[getPointer() - getOffset()] = code;
			}
			else
				System.out.println("[!]: ROM memory address " + Main.hex(getPointer()) + " does not exist.");
			
			incrementPointer();
		}
		
//		for(String i : rom)
//		{
//			if(!i.isEmpty())
//			System.out.println("ROM: " + i);
//		}
		
//		for(int i = 0; i < rom.length; i++)
//		{
//			if(!rom[i].isEmpty())
//				System.out.println("rom[" + i + "]: " + rom[i]);
//		}
		
//		System.exit(0);
	}
	
	@Override
	public String readAt(int address)
	{
//		System.out.println("[address]: " + Main.hex(address - getOffset()));
//		System.out.println("[rom]@ " + rom[address - getOffset()]);
		
		if(address > -1 && address < rom.length + getOffset())
			return rom[address - getOffset()];
		else
			return null;
	}
	
	@Override
	public int getSize()
	{
		return rom.length;
	}

	@Override
	public void clearMemory()
	{
		int pointer = 0;
		
		for(@SuppressWarnings("unused") String s : rom)
		{
			rom[pointer] = "";
			pointer++;
		}
	}
}
