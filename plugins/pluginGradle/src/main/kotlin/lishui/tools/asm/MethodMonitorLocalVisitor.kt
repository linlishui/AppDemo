package lishui.tools.asm

import lishui.tools.MethodMonitorPlugin
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter

/**
 * @author lishui.lin
 * Created it on 2021/8/4
 */
class MethodMonitorLocalVisitor(
    methodVisitor: MethodVisitor,
    access: Int,
    desc: String
) : LocalVariablesSorter(ASM7, access, desc, methodVisitor), Opcodes {

    private var startTime = 0
    private var endTime: Int = 0
    private var costTime: Int = 0
    private var thisMethodStack: Int = 0

    override fun visitCode() {
        super.visitCode()
        //long startTime = System.currentTimeMillis();
        mv.visitMethodInsn(
            INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        startTime = newLocal(Type.LONG_TYPE)
        mv.visitVarInsn(LSTORE, startTime)
    }

    override fun visitInsn(opcode: Int) {
        if (opcode == RETURN) {
            //long endTime = System.currentTimeMillis();
            mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false
            )
            endTime = newLocal(Type.LONG_TYPE)
            mv.visitVarInsn(LSTORE, endTime)

            //long costTime = endTime - startTime;
            mv.visitVarInsn(LLOAD, endTime)
            mv.visitVarInsn(LLOAD, startTime)
            mv.visitInsn(LSUB);
            costTime = newLocal(Type.LONG_TYPE)
            mv.visitVarInsn(LSTORE, costTime)

            // 判断costTime是否大于sThreshold
            mv.visitVarInsn(LLOAD, costTime)
            // 阀值由TimeCostPlugin的扩展属性threshold控制
            mv.visitLdcInsn(MethodMonitorPlugin.sTimeThreadHold)
            mv.visitInsn(LCMP)

            //if costTime <= sThreshold,就跳到end标记处，否则继续往下执行
            val end = Label()
            mv.visitJumpInsn(IFLE, end)

            //StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[0];
            mv.visitTypeInsn(NEW, "java/lang/Exception")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "java/lang/Exception",
                "<init>",
                "()V",
                false
            )
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Exception",
                "getStackTrace",
                "()[Ljava/lang/StackTraceElement;",
                false
            )
            mv.visitInsn(ICONST_0)
            mv.visitInsn(AALOAD)
            thisMethodStack = newLocal(Type.getType(StackTraceElement::class.java))
            mv.visitVarInsn(ASTORE, thisMethodStack)

            //Log.e("rain", String.format（"===> %s.%s(%s:%s)方法耗时 %d ms", thisMethodStack.getClassName(), thisMethodStack.getMethodName(),thisMethodStack.getFileName(),thisMethodStack.getLineNumber(),costTime));
            mv.visitLdcInsn("MethodMonitor")
            mv.visitLdcInsn("===> %s.%s(%s:%s)\u65b9\u6cd5\u8017\u65f6 %d ms")
            mv.visitInsn(ICONST_5)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
            mv.visitInsn(DUP)
            mv.visitInsn(ICONST_0)
            mv.visitVarInsn(ALOAD, thisMethodStack)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StackTraceElement",
                "getClassName",
                "()Ljava/lang/String;",
                false
            )
            mv.visitInsn(AASTORE)
            mv.visitInsn(DUP)
            mv.visitInsn(ICONST_1)
            mv.visitVarInsn(ALOAD, thisMethodStack)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StackTraceElement",
                "getMethodName",
                "()Ljava/lang/String;",
                false
            )
            mv.visitInsn(AASTORE)
            mv.visitInsn(DUP)
            mv.visitInsn(ICONST_2)
            mv.visitVarInsn(ALOAD, thisMethodStack)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StackTraceElement",
                "getFileName",
                "()Ljava/lang/String;",
                false
            )
            mv.visitInsn(AASTORE)
            mv.visitInsn(DUP)
            mv.visitInsn(ICONST_3)
            mv.visitVarInsn(ALOAD, thisMethodStack)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StackTraceElement",
                "getLineNumber",
                "()I",
                false
            )
            mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/Integer",
                "valueOf",
                "(I)Ljava/lang/Integer;",
                false
            )
            mv.visitInsn(AASTORE)
            mv.visitInsn(DUP)
            mv.visitInsn(ICONST_4)
            mv.visitVarInsn(LLOAD, costTime)
            mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/Long",
                "valueOf",
                "(J)Ljava/lang/Long;",
                false
            )
            mv.visitInsn(AASTORE)
            mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/String",
                "format",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                false
            )
            mv.visitMethodInsn(
                INVOKESTATIC,
                "android/util/Log",
                "e",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false
            )
            mv.visitInsn(POP)

            //end标记处，即方法的末尾
            mv.visitLabel(end)
        }
        super.visitInsn(opcode)
    }
}