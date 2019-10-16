package space.meduzza.taskverifier.services.comp

import org.springframework.stereotype.Service
import space.meduzza.taskverifier.CompilationService
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger

@Service
class CPPCompilationService : CompilationService {
    val c = AtomicInteger()
    override val language = CompilationService.Language.CPP

    override fun compile(file: File): File {
        val id = c.getAndIncrement()
        val p = ProcessBuilder("g++", "-o", "test-$id", file.absolutePath)
                .start()

        val waitFor = p.waitFor()
        val errText = p.errorStream.bufferedReader().readText()
        println(errText)
        println(waitFor)
        return File(file.parentFile, "test-$id.exe")
    }

    override fun execute(file: File, args: String): String {
        val p = ProcessBuilder(file.absolutePath, args)
                .start()
        p.waitFor()
        return p.inputStream.bufferedReader().readText().trim()
    }

    override fun getVersion(): String {
        val p = ProcessBuilder("g++", "--version")
        val proc = p.start()
        proc.waitFor()
        val text = proc.inputStream.bufferedReader().lines().findFirst().get()
        return text
    }
}