package com.bloodnbonesgaming.bnbgamingcore.core.insn;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry.ASMAdditionRegistryWrapper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;

/**
 * An implementation of {@link MethodInsnNode} that will attempt to redirect any references to methods added via the {@link ASMAdditionRegistry}.
 */
public class RedirectedMethodInsnNode extends MethodInsnNode{

	public RedirectedMethodInsnNode(final int opcode, final String owner, final String name, final String desc, final boolean itf, final IClassTransformerModule module) {
		super(opcode, owner, name, desc, itf);
		final BNBGamingClassTransformer transformer = BNBGamingClassTransformer.lookupOwner(module);
		final String wrap = transformer.getName();
		final ASMAdditionRegistryWrapper wrapper = ASMAdditionRegistry.getWrapper(wrap);
		final Map<String, List<MethodNode>> methodAdds = wrapper.getWrapperMethodAdditions();
		if(methodAdds.containsKey(owner)){
			final List<MethodNode> methods = methodAdds.get(owner);
			for(final MethodNode mNode:methods){
				if(mNode.name.replaceFirst(wrap, "").equals(name)){
					this.name = wrap+this.name;
					break;
				}
			}
		}
	}

}
