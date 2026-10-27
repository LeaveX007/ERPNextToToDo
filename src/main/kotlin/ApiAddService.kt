import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

class ApiAddService {

    suspend fun add(
        url: String,
        docType: String,
        toDo: ToDo,
        token: String
    ): HttpResponse {
        return httpClient.post {
            url {
                protocol = URLProtocol.HTTP
                host = url
                path("/api/resource/$docType")
            }
            headers {
                append(HttpHeaders.Authorization, "token $token")
                append(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                )
            }
            setBody(toDo)
        }
    }

}