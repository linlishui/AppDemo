package lishui.demo

/**
 * @author lishui.lin
 * Created it on 21-2-25
 */
class KotlinCode constructor(val params: String, val alias: String = "alias") {


    val valueField = "valueField"
    var variableField = "variableField"

    val threadName by lazy {
        val threadName = Thread.currentThread().name
        println("KotlinTest get threadName by lazy: $threadName")
        threadName
    }

    var field2 = "field2"
        set(value) {
            field = "new_$value"
        }
        get() {
            return "get_$field"
        }

    private lateinit var lateInitStr: String

    constructor() : this("new_params") {
        println("KotlinTest run secondary constructor")
    }

    init {
        println("KotlinTest run in init block.")
    }

    fun initKt() {
        if (!this::lateInitStr.isInitialized) lateInitStr = "lateInitStr"
    }

    fun getClassName() = this::class.qualifiedName.also { println("current class name: $it") }

    companion object {
        init {
            println("KotlinTest run in companion object init block.")
        }

        const val constCompanionValField = "constCompanionValField"
        val companionValField = "companionValField"
        var companionVarField = "companionVarField"
    }
}