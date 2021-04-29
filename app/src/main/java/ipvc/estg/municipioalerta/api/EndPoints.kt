package ipvc.estg.municipioalerta.api

import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("userverification")
    fun verificarUtilizador(
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Call<User>

    @GET("anomalias")
    fun getAllAnomalias(): retrofit2.Call<List<Anomalias>>

    @FormUrlEncoded
    @POST("inserirAnomalia")
    fun inserirAnomalia(
        @Field("titulo") titulo: String,
        @Field("descricao") descricao: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("foto") foto: String,
        @Field("login_id") login_id: Int
    ): retrofit2.Call<Anomalias>
}