package com.andback.pocketfridge.data.api

import com.andback.pocketfridge.data.model.BaseResponse
import com.andback.pocketfridge.data.model.RecipeEntity
import com.andback.pocketfridge.data.model.RecipeStepEntity
import io.reactivex.Single
import org.jetbrains.annotations.NotNull
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipe")
    fun getRecipes(): Single<BaseResponse<List<RecipeEntity>>>

    @GET("recipe/{recipeId}")
    fun getRecipeSteps(@Path("recipeId") @NotNull recipeId: Int): Single<BaseResponse<List<RecipeStepEntity>>>
}