package com.example.myjourney.retro

import com.example.myjourney.models.Comment
import com.example.myjourney.models.Htmlpage
import com.example.myjourney.models.Prayer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET


interface MyAPI {
    @GET("/comments")
    suspend fun getComments(): Response<List<Comment>>

    @GET("")
    suspend fun getHour(): Response<List<Prayer>>
    @GET("/horaire-priere-rabat.html")
    suspend fun getPage(): Response<Htmlpage>
}