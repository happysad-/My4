package myfour.src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import myfour.src.memory.MemoryMap;
import myfour.src.memory.Register;
import myfour.src.memory.Stack;

public class Parser
{
	private Register AX;	// Accumulator Register
	private Register BX;	// Base Register
	private Register CX;	// Count Register
	private Register DX;	// Data Register
	private Register FX;	// Flag Register
	private Register IP;	// Instruction Pointer Register
	
	private MemoryMap memMap;
	private Stack stack;
	
	private boolean halt = false;
	
	public Parser()
	{
		
	}
	
	public void executeProgram(MemoryMap memMap, Register... registers)
	{
		AX = registers[0];
		BX = registers[1];
		CX = registers[2];
		DX = registers[3];
		FX = registers[4];
		IP = registers[5];
		
		this.memMap = memMap;
		this.stack = new Stack();
		
		while(!halt)
		{
			String code = memMap.read(IP.getValue() + 0xC000);
			
			System.out.println("ROM: " + Main.hex(IP.getValue() + 0xC000));
			executeCode(code);
			
			IP.increment();
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		//TODO: GET PROGRAM SIZE ETC!
	}
	
	public void executeCode(String code)
	{
		if(Main.DEBUG)
		{
			System.out.println("[code]: " + code);
			System.out.println("[IP]: " + IP.getValue());
			System.out.println("[AX]: " + AX.getValue());
			System.out.println("[BX]: " + BX.getValue());
			System.out.println("[CX]: " + CX.getValue());
			System.out.println("[DX]: " + DX.getValue());
			System.out.println("[FX]: " + FX.getValue());
		}
		
		if(code != null)
		{
			String operator = code.split(" ")[0];
			
			if(operator.equals("NOP"))
			{
				System.out.println("NOP");
			}
			else if(operator.equals("JMP"))
			{
				System.out.println("JMP");
				
				String address = code.split(" ")[1];
				IP.changeValue(Integer.decode(address));
			}
			else if(operator.equals("STO"))
			{
				System.out.println("STO");
				
				String address = code.split(" ")[1];
				String value = code.split(" ")[2];
				
				if(address.equals("AX"))
					AX.changeValue(Integer.decode(value));
				else if(address.equals("BX"))
					BX.changeValue(Integer.decode(value));
				else if(address.equals("CX"))
					CX.changeValue(Integer.decode(value));
				else if(address.equals("DX"))
					DX.changeValue(Integer.decode(value));
				else if(address.equals("FX"))
					FX.changeValue(Integer.decode(value));
				else
					memMap.store(value, Integer.decode(address));
			}
			else if(operator.equals("INT"))
			{
				System.out.println("INT");
				
				// TODO: Handle interrupt service.
			}
			else if(operator.equals("PUSH"))
			{
				System.out.println("PUSH");
				
				String address = null;
				
				try
				{
					address = code.split(" ")[1];
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					
				}
				
				if(address == null)
					stack.pushAll(AX, BX, CX, DX, FX, IP);
				else
				{
					switch(address)
					{
					case "AX":
						stack.push(AX);
						break;
					case "BX":
						stack.push(BX);
						break;
					case "CX":
						stack.push(CX);
						break;
					case "DX":
						stack.push(DX);
						break;
					case "FX":
						stack.push(FX);
						break;
					case "IP":
						stack.push(IP);
						break;
					}
				}
			}
			else if(operator.equals("POP"))
			{
				System.out.println("POP");
				String address = code.split(" ")[1];
				
				if(address == null)
				{
					List<Register> registers = stack.popAll();
					AX = registers.get(0);
					BX = registers.get(1);
					CX = registers.get(2);
					DX = registers.get(3);
					FX = registers.get(4);
					IP = registers.get(5);
				}
				else
				{
					switch(address)
					{
					case "AX":
						AX = stack.pop();
						break;
					case "BX":
						BX = stack.pop();
						break;
					case "CX":
						CX = stack.pop();
						break;
					case "DX":
						DX = stack.pop();
						break;
					case "FX":
						FX = stack.pop();
						break;
					case "IP":
						IP = stack.pop();
						break;
					}
				}
			}
			/*
			 * ANDS two values. If AND is = 0, then set flag register to 1 
			 * to show that they're equal
			 */
			else if(operator.equals("AND"))
			{
				System.out.println("AND");
				
				String address = code.split(" ")[1];
				String value = code.split(" ")[2];
				
				switch(address)
				{
				case "AX":
					switch(value)
					{
					case "AX":
						AX.changeValue(AX.getValue() & AX.getValue());
						if(AX.getValue() == AX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "BX":
						AX.changeValue(AX.getValue() & BX.getValue());
						if(AX.getValue() == BX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "CX":
						AX.changeValue(AX.getValue() & CX.getValue());
						if(AX.getValue() == CX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "DX":
						AX.changeValue(AX.getValue() & DX.getValue());
						if(AX.getValue() == DX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "FX":
						AX.changeValue(AX.getValue() & FX.getValue());
						if(AX.getValue() == FX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					default:
						AX.changeValue(AX.getValue() & Integer.decode(value));
						if((AX.getValue() & Integer.decode(value)) == 0)
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					}
					break;
				case "BX":
					switch(value)
					{
					case "AX":
						BX.changeValue(BX.getValue() & AX.getValue());
						if(BX.getValue() == AX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "BX":
						BX.changeValue(BX.getValue() & BX.getValue());
						if(BX.getValue() == BX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "CX":
						BX.changeValue(BX.getValue() & CX.getValue());
						if(BX.getValue() == CX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "DX":
						BX.changeValue(BX.getValue() & DX.getValue());
						if(BX.getValue() == DX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "FX":
						BX.changeValue(BX.getValue() & FX.getValue());
						if(BX.getValue() == FX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					default:
						BX.changeValue(BX.getValue() & Integer.decode(value));
						if((BX.getValue() & Integer.decode(value)) == 0)
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					}
					break;
				case "CX":
					switch(value)
					{
					case "AX":
						CX.changeValue(CX.getValue() & AX.getValue());
						if(CX.getValue() == AX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "BX":
						CX.changeValue(CX.getValue() & BX.getValue());
						if(CX.getValue() == BX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "CX":
						CX.changeValue(CX.getValue() & CX.getValue());
						if(CX.getValue() == CX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "DX":
						CX.changeValue(CX.getValue() & DX.getValue());
						if(CX.getValue() == DX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "FX":
						CX.changeValue(CX.getValue() & FX.getValue());
						if(CX.getValue() == FX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					default:
						CX.changeValue(CX.getValue() & Integer.decode(value));
						if((CX.getValue() & Integer.decode(value)) == 0)
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					}
					break;
				case "DX":
					switch(value)
					{
					case "AX":
						DX.changeValue(DX.getValue() & AX.getValue());
						if(DX.getValue() == AX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "BX":
						DX.changeValue(DX.getValue() & BX.getValue());
						if(DX.getValue() == BX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "CX":
						DX.changeValue(DX.getValue() & CX.getValue());
						if(DX.getValue() == CX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "DX":
						DX.changeValue(DX.getValue() & DX.getValue());
						if(DX.getValue() == DX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "FX":
						DX.changeValue(DX.getValue() & FX.getValue());
						if(DX.getValue() == FX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					default:
						DX.changeValue(DX.getValue() & Integer.decode(value));
						if((DX.getValue() & Integer.decode(value)) == 0)
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					}
					break;
				case "FX":
					switch(value)
					{
					case "AX":
						FX.changeValue(FX.getValue() & AX.getValue());
						if(FX.getValue() == AX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "BX":
						FX.changeValue(FX.getValue() & BX.getValue());
						if(FX.getValue() == BX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "CX":
						FX.changeValue(FX.getValue() & CX.getValue());
						if(FX.getValue() == CX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "DX":
						FX.changeValue(FX.getValue() & DX.getValue());
						if(FX.getValue() == DX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					case "FX":
						FX.changeValue(FX.getValue() & FX.getValue());
						if(FX.getValue() == FX.getValue())
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					default:
						FX.changeValue(FX.getValue() & Integer.decode(value));
						if((FX.getValue() & Integer.decode(value)) == 0)
							FX.changeValue(1);
						else
							FX.changeValue(0);
						break;
					}
					break;
				}
			}
			else if(operator.equals("OR"))
			{
				System.out.println("OR");
				
				String address = code.split(" ")[1];
				String value = code.split(" ")[2];
				
				switch(address)
				{
				case "AX":
					switch(value)
					{
					case "AX":
						AX.changeValue(AX.getValue() | AX.getValue());
						break;
					case "BX":
						AX.changeValue(AX.getValue() | BX.getValue());
						break;
					case "CX":
						AX.changeValue(AX.getValue() | CX.getValue());
						break;
					case "DX":
						AX.changeValue(AX.getValue() | DX.getValue());
						break;
					case "FX":
						AX.changeValue(AX.getValue() | FX.getValue());
						break;
					default:
						AX.changeValue(AX.getValue() | Integer.decode(value));
						break;
					}
					break;
				case "BX":
					switch(value)
					{
					case "AX":
						BX.changeValue(BX.getValue() | AX.getValue());
						break;
					case "BX":
						BX.changeValue(BX.getValue() | BX.getValue());
						break;
					case "CX":
						BX.changeValue(BX.getValue() | CX.getValue());
						break;
					case "DX":
						BX.changeValue(BX.getValue() | DX.getValue());
						break;
					case "FX":
						BX.changeValue(BX.getValue() | FX.getValue());
						break;
					default:
						BX.changeValue(BX.getValue() | Integer.decode(value));
						break;
					}
					break;
				case "CX":
					switch(value)
					{
					case "AX":
						CX.changeValue(CX.getValue() | AX.getValue());
						break;
					case "BX":
						CX.changeValue(CX.getValue() | BX.getValue());
						break;
					case "CX":
						CX.changeValue(CX.getValue() | CX.getValue());
						break;
					case "DX":
						CX.changeValue(CX.getValue() | DX.getValue());
						break;
					case "FX":
						CX.changeValue(CX.getValue() | FX.getValue());
						break;
					default:
						CX.changeValue(CX.getValue() | Integer.decode(value));
						break;
					}
					break;
				case "DX":
					switch(value)
					{
					case "AX":
						DX.changeValue(DX.getValue() | AX.getValue());
						break;
					case "BX":
						DX.changeValue(DX.getValue() | BX.getValue());
						break;
					case "CX":
						DX.changeValue(DX.getValue() | CX.getValue());
						break;
					case "DX":
						DX.changeValue(DX.getValue() | DX.getValue());
						break;
					case "FX":
						DX.changeValue(DX.getValue() | FX.getValue());
						break;
					default:
						DX.changeValue(DX.getValue() | Integer.decode(value));
						break;
					}
					break;
				case "FX":
					switch(value)
					{
					case "AX":
						FX.changeValue(FX.getValue() | AX.getValue());
						break;
					case "BX":
						FX.changeValue(FX.getValue() | BX.getValue());
						break;
					case "CX":
						FX.changeValue(FX.getValue() | CX.getValue());
						break;
					case "DX":
						FX.changeValue(FX.getValue() | DX.getValue());
						break;
					case "FX":
						FX.changeValue(FX.getValue() | FX.getValue());
						break;
					default:
						FX.changeValue(FX.getValue() | Integer.decode(value));
						break;
					}
					break;
				}
			}
			else if(operator.equals("NEG"))
			{
				System.out.println("NEG");
				
				String value = code.split(" ")[1];
				
				DX.changeValue(~(Integer.decode(value)));
			}
			else if(operator.equals("ADD"))
			{
				System.out.println("ADD");
				
				String address = code.split(" ")[1];
				String value = code.split(" ")[2];
				
				switch(address)
				{
				case "AX":
					switch(value)
					{
					case "AX":
						AX.changeValue(AX.getValue() + AX.getValue());
						break;
					case "BX":
						AX.changeValue(AX.getValue() + BX.getValue());
						break;
					case "CX":
						AX.changeValue(AX.getValue() + CX.getValue());
						break;
					case "DX":
						AX.changeValue(AX.getValue() + DX.getValue());
						break;
					case "FX":
						AX.changeValue(AX.getValue() + FX.getValue());
						break;
					default:
						AX.changeValue(AX.getValue() + Integer.decode(value));
						break;
					}
					break;
				case "BX":
					switch(value)
					{
					case "AX":
						BX.changeValue(BX.getValue() + AX.getValue());
						break;
					case "BX":
						BX.changeValue(BX.getValue() + BX.getValue());
						break;
					case "CX":
						BX.changeValue(BX.getValue() + CX.getValue());
						break;
					case "DX":
						BX.changeValue(BX.getValue() + DX.getValue());
						break;
					case "FX":
						BX.changeValue(BX.getValue() + FX.getValue());
						break;
					default:
						BX.changeValue(BX.getValue() + Integer.decode(value));
						break;
					}
					break;
				case "CX":
					switch(value)
					{
					case "AX":
						CX.changeValue(CX.getValue() + AX.getValue());
						break;
					case "BX":
						CX.changeValue(CX.getValue() + BX.getValue());
						break;
					case "CX":
						CX.changeValue(CX.getValue() + CX.getValue());
						break;
					case "DX":
						CX.changeValue(CX.getValue() + DX.getValue());
						break;
					case "FX":
						CX.changeValue(CX.getValue() + FX.getValue());
						break;
					default:
						CX.changeValue(CX.getValue() + Integer.decode(value));
						break;
					}
					break;
				case "DX":
					switch(value)
					{
					case "AX":
						DX.changeValue(DX.getValue() + AX.getValue());
						break;
					case "BX":
						DX.changeValue(DX.getValue() + BX.getValue());
						break;
					case "CX":
						DX.changeValue(DX.getValue() + CX.getValue());
						break;
					case "DX":
						DX.changeValue(DX.getValue() + DX.getValue());
						break;
					case "FX":
						DX.changeValue(DX.getValue() + FX.getValue());
						break;
					default:
						DX.changeValue(DX.getValue() + Integer.decode(value));
						break;
					}
					break;
				case "FX":
					switch(value)
					{
					case "AX":
						FX.changeValue(FX.getValue() + AX.getValue());
						break;
					case "BX":
						FX.changeValue(FX.getValue() + BX.getValue());
						break;
					case "CX":
						FX.changeValue(FX.getValue() + CX.getValue());
						break;
					case "DX":
						FX.changeValue(FX.getValue() + DX.getValue());
						break;
					case "FX":
						FX.changeValue(FX.getValue() + FX.getValue());
						break;
					default:
						FX.changeValue(FX.getValue() + Integer.decode(value));
						break;
					}
					break;
				}
			}
			else if(operator.equals("SUB"))
			{
				System.out.println("SUB");
				
				String address = code.split(" ")[1];
				String value = code.split(" ")[2];
				
				switch(address)
				{
				case "AX":
					switch(value)
					{
					case "AX":
						AX.changeValue(AX.getValue() - AX.getValue());
						break;
					case "BX":
						AX.changeValue(AX.getValue() - BX.getValue());
						break;
					case "CX":
						AX.changeValue(AX.getValue() - CX.getValue());
						break;
					case "DX":
						AX.changeValue(AX.getValue() - DX.getValue());
						break;
					case "FX":
						AX.changeValue(AX.getValue() - FX.getValue());
						break;
					default:
						AX.changeValue(AX.getValue() - Integer.decode(value));
						break;
					}
					break;
				case "BX":
					switch(value)
					{
					case "AX":
						BX.changeValue(BX.getValue() - AX.getValue());
						break;
					case "BX":
						BX.changeValue(BX.getValue() - BX.getValue());
						break;
					case "CX":
						BX.changeValue(BX.getValue() - CX.getValue());
						break;
					case "DX":
						BX.changeValue(BX.getValue() - DX.getValue());
						break;
					case "FX":
						BX.changeValue(BX.getValue() - FX.getValue());
						break;
					default:
						BX.changeValue(BX.getValue() - Integer.decode(value));
						break;
					}
					break;
				case "CX":
					switch(value)
					{
					case "AX":
						CX.changeValue(CX.getValue() - AX.getValue());
						break;
					case "BX":
						CX.changeValue(CX.getValue() - BX.getValue());
						break;
					case "CX":
						CX.changeValue(CX.getValue() - CX.getValue());
						break;
					case "DX":
						CX.changeValue(CX.getValue() - DX.getValue());
						break;
					case "FX":
						CX.changeValue(CX.getValue() - FX.getValue());
						break;
					default:
						CX.changeValue(CX.getValue() - Integer.decode(value));
						break;
					}
					break;
				case "DX":
					switch(value)
					{
					case "AX":
						DX.changeValue(DX.getValue() - AX.getValue());
						break;
					case "BX":
						DX.changeValue(DX.getValue() - BX.getValue());
						break;
					case "CX":
						DX.changeValue(DX.getValue() - CX.getValue());
						break;
					case "DX":
						DX.changeValue(DX.getValue() - DX.getValue());
						break;
					case "FX":
						DX.changeValue(DX.getValue() - FX.getValue());
						break;
					default:
						DX.changeValue(DX.getValue() - Integer.decode(value));
						break;
					}
					break;
				case "FX":
					switch(value)
					{
					case "AX":
						FX.changeValue(FX.getValue() - AX.getValue());
						break;
					case "BX":
						FX.changeValue(FX.getValue() - BX.getValue());
						break;
					case "CX":
						FX.changeValue(FX.getValue() - CX.getValue());
						break;
					case "DX":
						FX.changeValue(FX.getValue() - DX.getValue());
						break;
					case "FX":
						FX.changeValue(FX.getValue() - FX.getValue());
						break;
					default:
						FX.changeValue(FX.getValue() - Integer.decode(value));
						break;
					}
					break;
				}
			}
			else if(operator.equals("JE"))
			{
				System.out.println("JE");
				
				String address = code.split(" ")[1];
				
				if(FX.getValue() == 1)
					IP.changeValue(Integer.decode(address) - 1);
			}
			else if(operator.equals("JNZ"))
			{
				System.out.println("JNZ");
				
				String address = code.split(" ")[1];
				if(FX.getValue() != 1)
					IP.changeValue(Integer.decode(address) - 1);
			}
			else if(operator.equals("OUT"))
			{
				System.out.println("OUT");
				//TODO: Handle output service.
			}
			else if(operator.equals("IN"))
			{
				System.out.println("IN");
				//TODO: Handle input service.
			}
			else if(operator.equals("HLT"))
			{
				System.out.println("HLT");
				halt = true;
			}
			else
				System.out.println("[!]: Unrecognised operator at " + Main.hex(IP.getValue() + 0xC000) + " address.");
		}
	}
	
	public List<String> loadProgramToMemory(String dir, String file)
	{
		try
		{
			List<String> program = Files.readAllLines(FileSystems.getDefault().getPath(dir, file), StandardCharsets.UTF_8);
			
			return program;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
