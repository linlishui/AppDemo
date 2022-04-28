package lishui.demo.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author lishui.lin
 * Created it on 2021/5/6
 */
class DynamicProxy(private val obj: Any) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val result = method?.invoke(obj, args)
        println("${method?.name} method invoke")
        return result
    }
}

interface IBook {
    fun buy()
}

class Book : IBook {
    override fun buy() {
        println("buy a book now")
    }
}