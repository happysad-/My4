package myfour.src;

public enum Opcodes
{
	NOP(0), JMP(1), STO(2), INT(3), PUSH(4), POP(5), AND(6), OR(7),
	NEG(8), ADD(9), SUB(10), JE(11), JNZ(12), OUT(13), IN(14), HLT(15);
	
	private final int machine_code;
	
	Opcodes(int machine_code)
	{
		this.machine_code = machine_code;
	}
	
	public int getValue()
	{
		return machine_code;
	}
}
