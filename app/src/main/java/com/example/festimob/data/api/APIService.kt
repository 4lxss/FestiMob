package com.example.festimob.data.api

import com.example.festimob.data.api.models.admin.login.LoginRequest
import com.example.festimob.data.api.models.admin.login.LoginResponse
import com.example.festimob.data.api.models.admin.logout.LogoutResponse
import com.example.festimob.data.api.models.admin.register.RegisterRequest
import com.example.festimob.data.api.models.admin.register.RegisterResponse
import com.example.festimob.data.api.models.admin.user.DeleteUserResponse
import com.example.festimob.data.api.models.admin.user.UpdateUserRoleRequest
import com.example.festimob.data.api.models.admin.user.UpdateUserRoleResponse
import com.example.festimob.data.api.models.admin.user.UsersResponse
import com.example.festimob.data.api.models.editor.AddEditorRequest
import com.example.festimob.data.api.models.editor.AddEditorResponse
import com.example.festimob.data.api.models.editor.EditorResponse
import com.example.festimob.data.api.models.zoneplan.ZonePlanResponse
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
}
