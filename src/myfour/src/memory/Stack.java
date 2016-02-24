package myfour.src.memory;

import java.util.ArrayList;
import java.util.List;

public class Stack
{
	private Register[] STACK;
	private Register SP;
	
	public Stack()
	{
		STACK = new Register[0xFF];
		SP = new Register("SP");
	}
	
	public void push(Register register)
	{
		STACK[SP.getValue()] = register;
		SP.increment();
	}
	
	public Register pop()
	{
		Register register = STACK[SP.getValue()];
		SP.decrement();
		return register;
	}
	
	public void pushAll(Register... registers)
	{
		if(SP.getValue() < 0xFF)
		{
			STACK[SP.getValue()] = registers[0]; //AX
			SP.increment();
			STACK[SP.getValue()] = registers[1]; //BX
			SP.increment();
			STACK[SP.getValue()] = registers[2]; //CX
			SP.increment();
			STACK[SP.getValue()] = registers[3]; //DX
			SP.increment();
			STACK[SP.getValue()] = registers[4]; //FX
			SP.increment();
			STACK[SP.getValue()] = registers[5]; //IP
			SP.increment();
		}
	}
	
	public List<Register> popAll()
	{
		List<Register> registers = new ArrayList<Register>();
		
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		registers.add(STACK[SP.getValue()]);	SP.decrement();
		
		return registers;
	}

}
