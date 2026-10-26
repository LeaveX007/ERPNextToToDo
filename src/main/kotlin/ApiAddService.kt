import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.serialization.json.Json

class ApiAddService {

    suspend fun add(
        client: HttpClient,
        url: String,
        docType: String,
        toDo: ToDo,
        token: String
    ): HttpResponse {
        return client.post {
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
            setBody(Json.encodeToString(toDo))
        }
    }

}