package com.bloodnbonesgaming.bnbgamingcore.core.insn;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;

import com.bloodnbonesgaming.bnbgamingcore.core.module.IClassTransformerModule;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry;
import com.bloodnbonesgaming.bnbgamingcore.core.util.ASMAdditionRegistry.ASMAdditionRegistryWrapper;
import com.bloodnbonesgaming.bnbgamingcore.core.util.BNBGamingClassTransformer;

/**
 * An implementation of {@link FieldInsnNode} that will attempt to redirect any references to fields added via the {@link ASMAdditionRegistry}.
 */
public class RedirectedFieldInsnNode extends FieldInsnNode{

	public RedirectedFieldInsnNode(final int opcode, final String owner, final String name, final String desc, final IClassTransformerModule module) {
		super(opcode, owner, name, desc);
		final BNBGamingClassTransformer transformer = BNBGamingClassTransformer.lookupOwner(module);
		final String wrap = transformer.getName();
		final ASMAdditionRegistryWrapper wrapper = ASMAdditionRegistry.getWrapper(wrap);
		final Map<String, List<FieldNode>> fieldAdds = wrapper.getWrapperFieldAdditions();
		if(fieldAdds.containsKey(owner)){
			final List<FieldNode> fields = fieldAdds.get(owner);
			for(final FieldNode fNode:fields){
				if(fNode.name.replaceFirst(wrap, "").equals(name)){
					this.name = wrap+this.name;
					break;
				}
			}
		}
	}

}
