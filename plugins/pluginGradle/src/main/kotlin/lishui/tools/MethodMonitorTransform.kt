package lishui.tools

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import lishui.tools.asm.MethodMonitorClassVisitor
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.*
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream


/**
 * @author lishui.lin
 * Created it on 2021/8/4
 */
class MethodMonitorTransform : Transform() {

    override fun getName(): String = TAG

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = true

    override fun transform(transformInvocation: TransformInvocation?) {
        println("--------- $TAG transform begin ---------")
        super.transform(transformInvocation)
        transformInvocation?.run {
            //如果不是增量，就删除之前所有产生的输出，重新来过
            if (!isIncremental) {
                outputProvider.deleteAll()
            }

            //遍历所有的输入，每一个输入里面包含jar和directory两种输入类型的文件集合
            inputs.forEach {
                it.jarInputs.forEach { jarInput ->
                    if (isIncremental) {
                        handleJarIncremental(
                            jarInput = jarInput,
                            outputProvider = outputProvider
                        )
                    } else {
                        handleJar(
                            jarInput = jarInput,
                            outputProvider = outputProvider
                        )
                    }
                }

                it.directoryInputs.forEach { directoryInput ->
                    if (isIncremental) {
                        handleDirectoryIncremental(
                            directoryInput = directoryInput,
                            outputProvider = outputProvider
                        )
                    } else {
                        handleDirectory(
                            directoryInput = directoryInput,
                            outputProvider = outputProvider
                        )
                    }
                }
            }
        }
        println("--------- $TAG transform end   ---------")
    }

    private fun handleJarIncremental(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        // 根据Jar文件输入状态处理增量更新
        jarInput.status?.apply {
            when (this) {
                Status.ADDED, Status.CHANGED -> handleJar(jarInput, outputProvider)
                Status.REMOVED -> outputProvider.deleteAll()
                else -> return@apply
            }
        }
    }

    private fun handleJar(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val srcJar: File = jarInput.file
        //使用TransformOutputProvider的getContentLocation方法根据输入构造输出位置
        val destJar: File = outputProvider.getContentLocation(
            jarInput.name,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )

        //遍历srcJar的所有内容, 在遍历的过程中把srcJar中的内容一条一条地复制到destJar
        //如果发现这个内容条目是class文件，就把它通过asm修改后再复制到destJar中
        foreachJarWithTransform(srcJar, destJar)
    }

    private fun foreachJarWithTransform(srcJar: File, destJar: File) {
        var srcJarFile: JarFile? = null
        var entryInputStream: InputStream? = null
        var destJarFileOs: JarOutputStream? = null
        try {
            srcJarFile = JarFile(srcJar)
            destJarFileOs = JarOutputStream(FileOutputStream(destJar))

            val enumeration: Enumeration<JarEntry> = srcJarFile.entries()
            while (enumeration.hasMoreElements()) {

                val jarEntry: JarEntry = enumeration.nextElement()

                entryInputStream = srcJarFile.getInputStream(jarEntry)
                destJarFileOs.putNextEntry(JarEntry(jarEntry.name))

                if (jarEntry.name.endsWith(".class")) {
                    // 如果是class文件，则通过asm修改字节码
                    val classReader = ClassReader(entryInputStream)
                    val classWriter = ClassWriter(0)
                    val methodMonitorClassVisitor = MethodMonitorClassVisitor(classWriter)
                    classReader.accept(methodMonitorClassVisitor, ClassReader.EXPAND_FRAMES);
                    // 把修改后的class文件复制到destJar中
                    destJarFileOs.write(classWriter.toByteArray())
                } else {
                    // no-op
                    destJarFileOs.write(IOUtils.toByteArray(entryInputStream))
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            entryInputStream?.apply {
                IOUtils.closeQuietly(this)
            }
            srcJarFile?.apply {
                IOUtils.closeQuietly(this)
            }
            destJarFileOs?.apply {
                IOUtils.closeQuietly(this)
            }
        }
    }

    private fun handleDirectoryIncremental(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        //通过DirectoryInput的getChangedFiles方法获取改变过的文件集合，每一个文件对应一个Status
        //通过DirectoryInput的getChangedFiles方法获取改变过的文件集合，每一个文件对应一个Status
        val changedFileMap = directoryInput.changedFiles
        //遍历所有改变过的文件
        //遍历所有改变过的文件
        for ((file, status) in changedFileMap) {
            val destDirectory = outputProvider.getContentLocation(
                directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY
            )
            when (status) {
                Status.ADDED, Status.CHANGED -> transformSingleFile(
                    file,
                    getDestFile(file, directoryInput.file, destDirectory)
                )
                Status.REMOVED -> FileUtils.forceDelete(
                    getDestFile(
                        file,
                        directoryInput.file,
                        destDirectory
                    )
                )
                Status.NOTCHANGED -> { }
                else -> { }
            }
        }
    }

    private fun handleDirectory(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        val srcDirectory = directoryInput.file
        val destDirectory = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        foreachDirectoryRecurseWithTransform(srcDirectory, destDirectory)
    }

    private fun foreachDirectoryRecurseWithTransform(srcDirectory: File, destDirectory: File) {
        if (!srcDirectory.isDirectory) {
            return
        }

        val files = srcDirectory.listFiles()
        files?.forEach {
            if (it.isFile) {
                val destFile = getDestFile(it, srcDirectory, destDirectory)
                transformSingleFile(it, destFile)
            } else {
                foreachDirectoryRecurseWithTransform(it, destDirectory)
            }
        }
    }

    @Throws(IOException::class)
    private fun getDestFile(srcFile: File, srcDirectory: File, destDirectory: File): File {
        val srcDirPath = srcDirectory.absolutePath
        val destDirPath = destDirectory.absolutePath
        //找到源输入文件对应的输出文件位置
        val destFilePath = srcFile.absolutePath.replace(srcDirPath, destDirPath)
        //构造源输入文件对应的输出文件
        val destFile = File(destFilePath)
        FileUtils.touch(destFile)
        return destFile
    }

    /**
     * (srcFile -> destFile)
     * 把srcFile文件复制到destFile中，如果srcFile是class文件，则把它经过asm修改后再复制到destFile中
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun transformSingleFile(srcFile: File, destFile: File) {
        FileInputStream(srcFile).use { srcFileIs ->
            FileOutputStream(destFile).use { destFileOs ->
                if (srcFile.name.endsWith(".class")) {
                    val classReader: ClassReader =
                        ClassReader(srcFileIs)
                    val classWriter = ClassWriter(0)
                    val monitorClassVisitor = MethodMonitorClassVisitor(classWriter)
                    classReader.accept(
                        monitorClassVisitor,
                        ClassReader.EXPAND_FRAMES
                    )
                    destFileOs.write(classWriter.toByteArray())
                } else {
                    destFileOs.write(IOUtils.toByteArray(srcFileIs))
                }
            }
        }
    }

    companion object {
        const val TAG = "MethodMonitorTransform"
    }
}