package space.meduzza.taskverifier


import org.springframework.stereotype.Service
import java.io.File
import java.lang.RuntimeException
import javax.tools.ToolProvider

@Service
class JavaCompilationService : CompilationService {
    override val language = CompilationService.Language.JAVA

    override fun execute(file: File, arguments:String): String {
        val p = ProcessBuilder("java", file.nameWithoutExtension, arguments)
                .directory(file.parentFile)
                .start()
        p.waitFor()
        return p.inputStream.bufferedReader().readText()
    }

    override fun compile(file: File):File{
        val p = ProcessBuilder("javac", file.absolutePath).start()
        val code = p.waitFor()
        println(code)
        when(code){
            1->{
                val errText = p.errorStream.bufferedReader().readText()
                println(errText)
                throw RuntimeException("compilation error")
            }
            2->{
                val errText = p.errorStream.bufferedReader().readText()
                println(errText)
                throw RuntimeException("file not found")
            }
        }


        return File(file.parentFile, "${file.nameWithoutExtension}.class")
    }

    override fun getVersion(): String {
        val p = ProcessBuilder("java", "-version")
        val proc = p.start()
        proc.waitFor()
        val text = proc.errorStream.bufferedReader().readText()
        return text.split('\n')[0].split(" ")[2].replace("\"", "")
    }

}