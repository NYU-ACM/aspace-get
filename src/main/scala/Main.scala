package asget

import java.io.IOException
import java.net.URI

import com.typesafe.config._

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.RequestBuilder

import org.json4s._
import org.json4s.native.JsonMethods._

object Main extends App {

  implicit val formats = DefaultFormats
  val conf = ConfigFactory.load()
  val host = conf.getString("host.url")
  val port = conf.getString("host.port")
  val client = HttpClients.createDefault
  val header = "X-ArchivesSpace-Session"
  val url = host + ":" + port
  val key = getKey()
  get(args(0))

  def getKey(): String = {
    val authenticate = RequestBuilder.post().setUri(new URI(url + "/users/" + conf.getString("host.username") + "/login")).addParameter("password", conf.getString("host.password")).build
    val response = client.execute(authenticate)
    val entity = response.getEntity
    val content = entity.getContent
    val data = scala.io.Source.fromInputStream(content).mkString
    val jarray = parse(data)
    val askey = (jarray \ "session").extract[String]
    EntityUtils.consume(entity)
    response.close
    askey 
  }

  def get(g: String) {
    val get = new HttpGet(url + g)
    get.addHeader(header, key)
    val response = client.execute(get)
    val entity = response.getEntity
    val content = entity.getContent
    val data = scala.io.Source.fromInputStream(content).mkString
    val json = parse(data)
    println(pretty(render(json)))
  }

}