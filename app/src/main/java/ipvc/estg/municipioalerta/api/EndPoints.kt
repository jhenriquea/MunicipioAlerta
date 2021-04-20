package ipvc.estg.municipioalerta.api

import retrofit2.http.*

interface EndPoints {
    @FormUrlEncoded
    @POST("userverification")
    fun verificarUtilizador(
        @Field("username") username: String,
        @Field("password") password: String
    ): retrofit2.Call<User>
}