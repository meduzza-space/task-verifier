package space.meduzza.taskverifier

import java.io.File

interface CompilationService {
    val language: Language
    fun compile(file: File):File
    fun execute(file: File, args:String):String
    fun getVersion():String

    enum class Language {
        JAVA,
        C,
        CPP,
        CS,
        PYTHON
    }
}
