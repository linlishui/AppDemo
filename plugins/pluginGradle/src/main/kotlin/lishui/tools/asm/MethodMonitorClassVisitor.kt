package lishui.tools.asm

import lishui.tools.MethodMonitorPlugin
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM7

/**
 * @author lishui.lin
 * Created it on 2021/8/4
 */
class MethodMonitorClassVisitor(
    classVisitor: ClassVisitor
) : ClassVisitor(ASM7, classVisitor), Opcodes {

    private var mPackage: String
    private var mCurClassName: String = ""
    private var isExcludeOtherPackage: Boolean = false

    init {
        mPackage = MethodMonitorPlugin.sTargetPackage
        if (mPackage.isNotEmpty() && mPackage.indexOf('.') != -1) {
            mPackage = mPackage.replace('.', '/')
        }
        isExcludeOtherPackage = mPackage.isNotEmpty()
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        name?.let { mCurClassName = it }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val desc = descriptor ?: ""
        if (isExcludeOtherPackage) {
            if (mCurClassName.startsWith(mPackage) && "<init>" != name) {
                return MethodMonitorLocalVisitor(methodVisitor, access, desc)
            }
        } else if ("<init>" != name){
            return MethodMonitorLocalVisitor(methodVisitor, access, desc)
        }
        return methodVisitor
    }
}