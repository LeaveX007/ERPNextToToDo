import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    routing {
        post("/") {
            val toDoText = call.receiveText()
            val toDo = Json.decodeFromString<ToDo>(toDoText)
            val apiAddService = ApiAddService()
            apiAddService.add(
                "site2.local",
                "ToDo",
                toDo,
                "228cc2946f6f330:e53dd148aca809c"
            )
            call.respond(HttpStatusCode.OK, "Todo erfolgreich erzeugt")
        }
    }
}