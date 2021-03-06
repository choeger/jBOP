/*
 * Copyright (C) 2013 uebb.tu-berlin.de.
 * 
 * This file is part of JBOP (Java Bytecode OPtimizer).
 * 
 * JBOP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JBOP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBOP. If not, see <http://www.gnu.org/licenses/>.
 */
package de.tuberlin.uebb.jbop.optimizer.controlflow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import de.tuberlin.uebb.jbop.optimizer.ClassNodeBuilder;
import de.tuberlin.uebb.jbop.optimizer.array.FieldArrayValueInliner;
import de.tuberlin.uebb.jbop.optimizer.array.NonNullArrayValue;

/**
 * Tests for {@link ConstantIfInliner}.
 * 
 * @author Christopher Ewest
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstantIfInlinerTest {
  
  @Mock
  private FieldArrayValueInliner arrayValue;
  @InjectMocks
  private ConstantIfInliner constantIfInliner;
  private final List<NonNullArrayValue> nonNullArrayValues = new ArrayList<>();
  private ClassNodeBuilder builder;
  private MethodNode method;
  @Mock
  private NonNullArrayValue nonNullValue;
  
  /**
   * Init for every test.
   */
  @Before
  public void before() {
    when(arrayValue.getNonNullArrayValues()).thenReturn(nonNullArrayValues);
    nonNullArrayValues.add(nonNullValue);
    builder = ClassNodeBuilder.createClass("de.tuberlin.uebb.jbop.optimizer.controlflow.ConstantIfTestClass").//
        addMethod("testIf", "()V");//
    method = builder.getMethod("testIf");
    
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(1<2)
   * ...
   * </pre>
   */
  
  @Test
  public void testConstantIfInlinerIF_CMPEG() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPGE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(1<=2)
   * ...
   * </pre>
   */
  
  @Test
  public void testConstantIfInlinerIF_ICMPGT() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPGT, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(2>1)
   * ...
   * </pre>
   */
  
  @Test
  public void testConstantIfInlinerIF_ICMPLE() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPLE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(2>=1)
   * ...
   * </pre>
   */
  
  @Test
  public void testConstantIfInlinerIF_ICMPLT() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPLT, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(1==1)
   * ...
   * </pre>
   */
  @Test
  public void testConstantIfInlinerIF_ICMPNE() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPNE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(1!=2)
   * ...
   * </pre>
   */
  @Test
  public void testConstantIfInlinerIF_ICMPEQ() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPEQ, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(6, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(a)
   * ...
   * </pre>
   * 
   * where a is true
   */
  @Test
  public void testConstantIfInlinerIFEQ() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new JumpInsnNode(Opcodes.IFEQ, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(5, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(!a)
   * ...
   * </pre>
   * 
   * where a is false
   */
  @Test
  public void testConstantIfInlinerIFNE() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_0)).//
        addInsn(new JumpInsnNode(Opcodes.IFNE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(5, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(a==null)
   * ...
   * </pre>
   * 
   * where a is not null
   */
  @Test
  public void testConstantIfInlinerIFNULL() {
    // INIT
    when(
        nonNullValue.is(Matchers.<AbstractInsnNode> any(), Matchers.<AbstractInsnNode> any(),
            Matchers.<List<AbstractInsnNode>> any(), Matchers.<List<AbstractInsnNode>> any())).thenReturn(
        Boolean.valueOf(true));
    final LabelNode label = new LabelNode();
    builder.addInsn(new TypeInsnNode(Opcodes.NEW, Type.getDescriptor(Object.class))).//
        addInsn(new JumpInsnNode(Opcodes.IFNULL, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(5, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(a==null)
   * ...
   * </pre>
   * 
   * where a is null
   */
  @Test
  public void testConstantIfInlinerIFNONNULL() {
    // INIT
    final LabelNode label = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ACONST_NULL)).//
        addInsn(new JumpInsnNode(Opcodes.IFNONNULL, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(5, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(1>2)
   * ...
   * else
   * ...
   * </pre>
   */
  @Test
  public void testConstantIfInlinerIF_ICMPLEWithElseChooseIf() {
    // INIT
    final LabelNode label = new LabelNode();
    final LabelNode label2 = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPLE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(new JumpInsnNode(Opcodes.GOTO, label2)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label2).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(10, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(4, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
    assertEquals(Opcodes.NOP, optimized.get(1).getOpcode());
  }
  
  /**
   * Tests that constantIfInliner is working correctly.
   * 
   * Input is
   * 
   * <pre>
   * if(2>1)
   * ...
   * else
   * ...
   * </pre>
   */
  @Test
  public void testConstantIfInlinerIF_ICMPLEWithElseChooseElse() {
    // INIT
    final LabelNode label = new LabelNode();
    final LabelNode label2 = new LabelNode();
    builder.addInsn(new InsnNode(Opcodes.ICONST_1)).//
        addInsn(new InsnNode(Opcodes.ICONST_2)).//
        addInsn(new JumpInsnNode(Opcodes.IF_ICMPLE, label)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(new JumpInsnNode(Opcodes.GOTO, label2)).//
        addInsn(label).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(new InsnNode(Opcodes.NOP)).//
        addInsn(label2).//
        addInsn(new InsnNode(Opcodes.RETURN));
    
    // RUN
    assertEquals(10, method.instructions.size());
    final InsnList optimized = constantIfInliner.optimize(method.instructions, method);
    
    // ASSERT
    assertEquals(3, optimized.size());
    assertEquals(Opcodes.NOP, optimized.get(0).getOpcode());
    assertEquals(-1, optimized.get(1).getOpcode());
  }
}
