package b100.ASM;

import static b100.WeatherConfigMod.*;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import b100.WeatherConfigModConfig;
import net.minecraft.launchwrapper.IClassTransformer;

public class WeatherConfigTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		
		if(transformedName.equals("net.minecraft.world.World")) {
			boolean transformed = false;
			
			print("Transforming World class...");
			
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, 0);
			
			List<MethodNode> list = classNode.methods;
			
			for(MethodNode method : list) {
				if(method.name.equals("updateWeatherBody")) {
					transformed = transformUpdateWeatherBody(method);
				}
			}
			
			if(transformed) {
				print("Success!");
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}else {
				print("Could not transform!");
			}
		}
		
		return bytes;
	}
	
	public static boolean transformUpdateWeatherBody(MethodNode method) {
		boolean transformed = false;
		
		final InsnList instructions = method.instructions;
		final AbstractInsnNode first = instructions.getFirst();
		
		AbstractInsnNode node1 = find(first, (e) -> SIPUSH(e, 12000));
		AbstractInsnNode node2 = find(node1, (e) -> SIPUSH(e, 3600));
		AbstractInsnNode node3 = find(node2, (e) -> LDC(e, 168000));
		AbstractInsnNode node4 = find(node3, (e) -> SIPUSH(e, 12000));
		AbstractInsnNode node5 = find(node4, (e) -> SIPUSH(e, 12000));
		AbstractInsnNode node6 = find(node5, (e) -> SIPUSH(e, 12000));
		AbstractInsnNode node7 = find(node6, (e) -> LDC(e, 168000));
		AbstractInsnNode node8 = find(node7, (e) -> SIPUSH(e, 12000));
		
		if(node8 != null) {
			String className = WeatherConfigModConfig.class.getName().replace('.', '/');
			
			instructions.set(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getThunderTimeRand", "()I", false));
			instructions.set(node2, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getThunderTimeMin", "()I", false));
			instructions.set(node3, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getThunderDelayRand", "()I", false));
			instructions.set(node4, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getThunderDelayMin", "()I", false));
			instructions.set(node5, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getRainTimeRand", "()I", false));
			instructions.set(node6, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getRainTimeMin", "()I", false));
			instructions.set(node7, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getRainDelayRand", "()I", false));
			instructions.set(node8, new MethodInsnNode(Opcodes.INVOKESTATIC, className, "getRainDelayMin", "()I", false));
			
			transformed = true;
		}
		return transformed;
	}
	
	private static boolean SIPUSH(AbstractInsnNode instruction, int i) {
		if(instruction instanceof IntInsnNode) {
			IntInsnNode node = (IntInsnNode) instruction;
			return node.operand == i;
		}
		return false;
	}
	
	private static boolean LDC(AbstractInsnNode instruction, int i) {
		if(instruction instanceof LdcInsnNode) {
			LdcInsnNode node = (LdcInsnNode) instruction;
			if(node.cst instanceof Integer) {
				int j = (int) node.cst;
				return j == i;
			}
		}
		return false;
	}
	
	public static interface Condition<E> {
		public boolean isTrue(E e);
	}
	
	public static AbstractInsnNode find(AbstractInsnNode instruction, Condition<AbstractInsnNode> condition) {
		while(true) {
			if(instruction == null) {
				return null;
			}
			instruction = instruction.getNext();
			if(instruction == null) {
				return null;
			}
			if(condition.isTrue(instruction)) {
				return instruction;
			}
		}
	}
	
	public static void printInstructions(AbstractInsnNode instruction, int count) {
		for(int i=0; i < count; i++) {
			printInstruction(instruction);
			instruction = instruction.getNext();
			if(instruction == null) {
				break;
			}
		}
	}
	
	public static void printAllInstructions(MethodNode method) {
		AbstractInsnNode instruction = method.instructions.getFirst();
		while(true) {
			printInstruction(instruction);
			instruction = instruction.getNext();
			if(instruction == null) {
				break;
			}
		}
	}
	
	public static void printInstruction(AbstractInsnNode instruction) {
		if(instruction == null) {
			print("null");
			return;
		}
		
		String name = "--" + instruction.getClass().getSimpleName();
		Object value = "";
		int opcode = instruction.getOpcode();
		
		if(instruction instanceof LineNumberNode) {
			LineNumberNode node = (LineNumberNode) instruction;
			print("LINE " + node.line);
			return;
		}else if(instruction instanceof LdcInsnNode) {
			LdcInsnNode node = (LdcInsnNode) instruction;
			name = "LDC";
			value = node.cst;
			
		}else if(instruction instanceof IntInsnNode) {
			IntInsnNode node = (IntInsnNode) instruction;
			name = "SIPUSH";
			value = node.operand;
		}
		
		print("  [" + opcode + "] " + name + " " + value);
	}

}
