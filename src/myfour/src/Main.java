package myfour.src;

import myfour.src.memory.MemoryMap;
import myfour.src.memory.Register;

public class Main
{
	public static final boolean DEBUG = true;
	
	private Register AX;	// Accumulator Register
	private Register BX;	// Base Register
	private Register CX;	// Count Register
	private Register DX;	// Data Register
	private Register FX;	// Flag Register
	private Register IP;	// Instruction Pointer Register
	private Register SP;	// Stack Pointer Register
	
	private Parser parser = new Parser();
	private MemoryMap memMap = new MemoryMap();
	
	public Main()
	{
		initialise();
		
		runProgram();
	}
	
	public void initialise()
	{
		AX = new Register("AX");
		BX = new Register("BX");
		CX = new Register("CX");
		DX = new Register("DX");
		FX = new Register("FX");
		IP = new Register("IP");
		SP = new Register("SP");
	}
	
	public void runProgram()
	{		
		memMap.storeProgram(parser.loadProgramToMemory("programs", "loop.txt"));
		
		parser.executeProgram(memMap, AX, BX, CX, DX, FX, IP, SP);
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public static String hex(int n)
	{
		return String.format("0x%4s", Integer.toHexString(n).toUpperCase()).replace(' ', '0');
	}
}
