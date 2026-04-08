package com.example.festimob.data.api

import com.example.festimob.data.api.festival.FestivalAddResponse
import com.example.festimob.data.api.festival.FestivalAddWrapper
import com.example.festimob.data.api.festival.FestivalNetwork
import com.example.festimob.data.api.festival.FestivalUpdateRequest
import com.example.festimob.data.api.games.CreateJeuRequest
import com.example.festimob.data.api.games.JeuDto
import com.example.festimob.data.api.games.MecanismDto
import com.example.festimob.data.api.admin.login.LoginRequest
import com.example.festimob.data.api.admin.login.LoginResponse
import com.example.festimob.data.api.admin.logout.LogoutResponse
import com.example.festimob.data.api.admin.register.RegisterRequest
import com.example.festimob.data.api.admin.register.RegisterResponse
import com.example.festimob.data.api.admin.user.DeleteUserResponse
import com.example.festimob.data.api.admin.user.UpdateUserRoleRequest
import com.example.festimob.data.api.admin.user.UpdateUserRoleResponse
import com.example.festimob.data.api.admin.user.UsersResponse
import com.example.festimob.data.api.reservation.AddGamesRequest
import com.example.festimob.data.api.reservation.Editeur
import com.example.festimob.data.api.reservation.Log
import com.example.festimob.data.api.reservation.LogAddRequest
import com.example.festimob.data.api.reservation.Reservation
import com.example.festimob.data.api.reservation.ReservationAddRequest
import com.example.festimob.data.api.reservation.ReservationEditRequest
import com.example.festimob.data.api.zone.FestivalDeleteRequest
import com.example.festimob.data.api.zone.ZoneDeleteRequest
import com.example.festimob.data.api.zone.ZoneTarif
import com.example.festimob.data.api.zone.ZoneTarifAddWrapper
import com.example.festimob.data.api.editor.AddEditorRequest
import com.example.festimob.data.api.editor.AddEditorResponse
import com.example.festimob.data.api.editor.EditorResponse
import com.example.festimob.data.api.zone.ZonePlanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT

interface APIService {
    // Editors
    @GET("api/editeurs/all")
    suspend fun getEditors(): List<EditorResponse>

    @POST("api/editeurs/add")
    suspend fun addEditor(@Body request: AddEditorRequest): AddEditorResponse

    @GET("api/editeurs/{id}")
    suspend fun getEditor(@Path("id") id: Int): EditorResponse

    // Games (used in app)
    @GET("api/jeux/all")
    suspend fun getJeux(): List<JeuDto>

    @GET("api/mecanisms/all")
    suspend fun getMecanisms(): List<MecanismDto>

    @POST("api/editeurs/{editeurId}/jeux")
    suspend fun createJeuForEditeur(
        @Path("editeurId") editeurId: Int,
        @Body body: CreateJeuRequest
    ): JeuDto

    // Festivals
    @GET("api/festivals/all")
    suspend fun getFestivalsRaw(): List<FestivalNetwork>

    @POST("api/festivals/add")
    suspend fun addFestival(@Body wrapper: FestivalAddWrapper): Response<FestivalAddResponse>

    @POST("api/festivals/deleteone")
    suspend fun deleteFestival(@Body body: FestivalDeleteRequest): Response<Unit>

    @PUT("api/festivals/update-full")
    suspend fun updateFestival(@Body festival: FestivalUpdateRequest): retrofit2.Response<Unit>

    // Zones
    // Zones
    @GET("api/zoneTarif/getzones")
    suspend fun getAllZones(): List<ZoneTarif>

    @POST("api/ZoneTarif/add")
    suspend fun addZone(@Body wrapper: ZoneTarifAddWrapper): Response<Unit>

    @POST("api/zoneTarif/delete")
    suspend fun deleteZonesByFestival(@Body body: ZoneDeleteRequest): Response<Unit>

    @GET("api/zonePlan/all")
    suspend fun getAllZonePlans(): List<ZonePlanResponse>

    // Admin/auth
    @GET("api/admin/users")
    suspend fun getUsers(): UsersResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @PATCH("api/admin/users/{id}/role")
    suspend fun updateUserRole(
        @Path("id") userId: Int,
        @Body request: UpdateUserRoleRequest
    ): UpdateUserRoleResponse

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): DeleteUserResponse

    // ─── Réservations ──────────────────────────────────────────

    @GET("api/festivals/{id}/reservations/all")
    suspend fun getReservations(@Path("id") festivalId: Int): List<Reservation>

    @POST("api/festivals/{id}/reservations/add")
    suspend fun addReservation(
        @Path("id") festivalId: Int,
        @Body reservation: ReservationAddRequest
    ): Response<Reservation>

    @PUT("api/festivals/{id}/reservation/{id_r}/edit")
    suspend fun editReservation(
        @Path("id") festivalId: Int,
        @Path("id_r") reservationId: Int,
        @Body reservation: ReservationEditRequest
    ): Response<Unit>

    @POST("api/festivals/{id}/reservation/{id_r}/delete")
    suspend fun deleteReservation(
        @Path("id") festivalId: Int,
        @Path("id_r") reservationId: Int
    ): Response<Unit>

    @POST("api/festivals/{id}/reservations/{id_r}/addgames")
    suspend fun addGames(
        @Path("id") festivalId: Int,
        @Path("id_r") reservationId: Int,
        @Body body: AddGamesRequest
    ): Response<Unit>

    // ─── Logs ──────────────────────────────────────────────────

    @GET("api/festivals/{id}/logs/all")
    suspend fun getLogs(@Path("id") festivalId: Int): List<Log>

    @POST("api/festivals/{id}/logs/add")
    suspend fun addLog(
        @Path("id") festivalId: Int,
        @Body log: LogAddRequest
    ): Response<Log>

    // ─── Éditeurs ──────────────────────────────────────────────

    @GET("api/editeurs/all")
    suspend fun getEditeurs(): List<Editeur>

    @GET("api/auth/whoami")
    suspend fun whoAmI(): LoginResponse

}
