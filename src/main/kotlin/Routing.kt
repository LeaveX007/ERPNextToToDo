import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    routing {
        post("/") {
            val toDoText = call.receiveText()
            val toDo = Json.decodeFromString<ToDo>(toDoText)
            val apiAddService = ApiAddService()
            val httpClient = HttpClient(CIO)
            apiAddService.add(
                httpClient,
                "site2.local",
                "ToDo",
                toDo,
                "228cc2946f6f330:e53dd148aca809c"
            )
            call.respond(HttpStatusCode.OK, "Todo successfully added.")
        }
    }
}