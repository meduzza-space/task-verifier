package space.meduzza.taskverifier

import org.junit.Assert
import org.junit.Test
import space.meduzza.taskverifier.services.comp.CCompilationService
import java.io.File

class CompilationTest {

    @Test
    fun cCompilator(){
        val service = CCompilationService()
        val f = File("test.c")
        val out = f.outputStream()
        val input = CompilationTest::class.java.classLoader.getResourceAsStream("Main.c")!!
        out.write(input.readBytes())
        out.close()
        input.close()
        val compile = service.compile(f)
        val execute = service.execute(compile, "test")
        Assert.assertEquals("test", execute)
        compile.delete()
    }
}