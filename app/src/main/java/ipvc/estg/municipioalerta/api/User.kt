package ipvc.estg.municipioalerta.api

data class User(
    val id: Int,
    val username: String,
    val password: String
)

data class Anomalias(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val latitude: Double,
    val longitude: Double,
    val foto: String,
    val tipo: String,
    val login_id: Int
)