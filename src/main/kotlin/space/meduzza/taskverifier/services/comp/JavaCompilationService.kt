package space.meduzza.taskverifier.services.comp


import org.springframework.stereotype.Service
import space.meduzza.taskverifier.CompilationService
import java.io.File
import java.lang.RuntimeException

@Service
class JavaCompilationService : CompilationService {
    override val language = CompilationService.Language.JAVA

    override fun execute(file: File, arguments:String): String {
        val p = ProcessBuilder("java", file.nameWithoutExtension, arguments)
                .directory(file.parentFile)
                .start()
        p.waitFor()
        return p.inputStream.bufferedReader().readText().trim()
    }

    override fun compile(file: File):File{
        val p = ProcessBuilder("javac", file.absolutePath).start()
        when(p.waitFor()){
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
        val text = proc.errorStream.bufferedReader().lines().findFirst().get()
        return text
    }

}