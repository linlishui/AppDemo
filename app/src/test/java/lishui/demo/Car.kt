package lishui.demo

/**
 * @author lishui.lin
 * Created it on 21-2-26
 */
const val text = ""
class Car(brand: String ="No-name")  // 带有默认参数的变量值
{
    val firstProperty = "First property: $brand".also(::println)

    init {
        println("First initializer block that prints $brand")
    }

    val secondProperty = "Second property: ${brand.length}".also(::println)

    init {
        println("Second initializer block that prints ${brand.length}")
    }

    constructor(type: Int) : this("BYD") {
        println("Second constructor that prints type = $type")

    }

    fun transform(color: String): Int = when (color) {
        "Red" -> 0
        "Green" -> 1
        "Blue" -> 2
        else -> throw IllegalArgumentException("Invalid color param value")
    }
}

open class Base(val name: String) {

    init { println("Initializing Base") }

    open val size: Int =
        name.length.also { println("Initializing size in Base: $it") }
}

class Derived(
    name: String,
    val lastName: String
) : Base(name.capitalize().also { println("Argument for Base: $it") }) {

    init { println("Initializing Derived") }

    override val size: Int =
        (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}

class MyClass {
    companion object {
        val dog = "Tuanzi"
        fun create(): MyClass = MyClass()
    }
}

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // “this”对应该MutableList<Int>类型
    this[index1] = this[index2]
    this[index2] = tmp
}