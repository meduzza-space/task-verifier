package space.meduzza.taskverifier.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.multipart.MultipartFile

@Controller
class IndexController {

    @RequestMapping("/", method = [RequestMethod.GET])
        fun index(model: Model): String{
        return "index"
    }

    @RequestMapping("/check-code", method = [RequestMethod.GET])
    fun checkCode(code: MultipartFile, model: Model): String{
        println(code.originalFilename)
        println(code.size)
        return "index"
    }
}