import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
object LiftExample {

  implicit val formats = DefaultFormats

  case class FormatExample(name: String, age: String, country: String)

  val myJson = parse("""{"name":"Saadou","age":"23","country":"Algeria"}""")


  def main(args: Array[String]): Unit = {
    val test = myJson.extract[FormatExample]
    println(test.name)
    println(test.age)
    println(test.country)
    ///////////////////////////////////////////////////////////////////////////////
    val myListJNumber = List(JDouble(12),JDouble(13),JDouble(14))
    val myJArray = JArray(myListJNumber)
    println(compactRender(myJArray))
    ///////////////////////////////////////////////////////////////////////////////
    val json = "FormatExample" -> ("name" -> "Julia") ~ ("age" -> "1") ~ ("country"-> "Algeria")
    val stringJson = compactRender(json \ "FormatExample")
    val test2 = parse(stringJson).extract[FormatExample]
    println(test2.name)
    println(test2.age)
    println(test2.country)
    //this is no longuer available , to convert a Object into JSON we have to passe a JValue to compactRender method
    // compact(render(json))

  }

}
