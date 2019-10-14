package space.meduzza.taskverifier.services.comp

import org.springframework.stereotype.Service
import space.meduzza.taskverifier.CompilationService
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger

@Service
class CCompilationService : CompilationService {
    val c = AtomicInteger()
    override val language = CompilationService.Language.C

    override fun compile(file: File): File {
        val id = c.getAndIncrement()
        val p = ProcessBuilder("C:\\MinGW\\bin\\gcc", "-o", "C:\\Users\\Андрей\\Desktop\\$id", file.absolutePath)
                .start()

        val waitFor = p.waitFor()
        val errText = p.errorStream.bufferedReader().readText()
        println(errText)
        println(waitFor)
//        when(waitFor){
//            1->{
//                val errText = p.errorStream.bufferedReader().readText()
//                println(errText)
//                throw RuntimeException("compilation error")
//            }
//            2->{
//                val errText = p.errorStream.bufferedReader().readText()
//                println(errText)
//                throw RuntimeException("file not found")
//            }
//        }
        return File(file.parentFile, "test$id.exe")
    }

    override fun execute(file: File, args: String): String {
        val p = ProcessBuilder(file.absolutePath, args)
//                .directory(file.parentFile)
                .start()
        p.waitFor()
        return p.inputStream.bufferedReader().readText().trim()
    }

    override fun getVersion(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}