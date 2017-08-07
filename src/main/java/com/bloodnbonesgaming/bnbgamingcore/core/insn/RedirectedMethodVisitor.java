package com.bloodnbonesgaming.bnbgamingcore.core.insn;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry.ASMAdditionRegistryWrapper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedirectedMethodVisitor{

	private final MethodVisitor methodVisitor;

	public void visitRedirectedFieldInsn(final int opcode, final String owner, String name, final String desc, final IClassTransformerModule module){
		final BNBGamingClassTransformer transformer = BNBGamingClassTransformer.lookupOwner(module);
		final String wrap = transformer.getName();
		final ASMAdditionRegistryWrapper wrapper = ASMAdditionRegistry.getWrapper(wrap);
		final Map<String, List<FieldNode>> fieldAdds = wrapper.getWrapperFieldAdditions();
		if(fieldAdds.containsKey(owner)){
			final List<FieldNode> fields = fieldAdds.get(owner);
			for(final FieldNode fNode:fields){
				if(fNode.name.replaceFirst(wrap, "").equals(name)){
					name = wrap+name;
					break;
				}
			}
		}
		this.methodVisitor.visitFieldInsn(opcode, owner, name, desc);
	}

	public void visitRedirectedMethodInsn(final int opcode, final String owner, String name, final String desc, final boolean itf, final IClassTransformerModule module){
		final BNBGamingClassTransformer transformer = BNBGamingClassTransformer.lookupOwner(module);
		final String wrap = transformer.getName();
		final ASMAdditionRegistryWrapper wrapper = ASMAdditionRegistry.getWrapper(wrap);
		final Map<String, List<MethodNode>> methodAdds = wrapper.getWrapperMethodAdditions();
		if(methodAdds.containsKey(owner)){
			final List<MethodNode> methods = methodAdds.get(owner);
			for(final MethodNode mNode:methods){
				if(mNode.name.replaceFirst(wrap, "").equals(name)){
					name = wrap+name;
					break;
				}
			}
		}
		this.methodVisitor.visitMethodInsn(opcode, owner, name, desc, itf);
	}

}
