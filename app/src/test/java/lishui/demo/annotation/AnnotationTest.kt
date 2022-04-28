package lishui.demo.annotation

/**
 * @author lishui.lin
 * Created it on 21-2-20
 */
class AnnotationTest {

    @DogFeed(foodValue = "feed dog field")
    val feedFieldType: Int = -1

    @DogFeed(foodValue = "feed dog method")
    fun testDogFeed() {
    }
}