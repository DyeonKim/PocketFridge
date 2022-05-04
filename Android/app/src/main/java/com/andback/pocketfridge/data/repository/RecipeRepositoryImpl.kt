package com.andback.pocketfridge.data.repository

import com.andback.pocketfridge.data.model.BaseResponse
import com.andback.pocketfridge.data.model.CookingIngreEntity
import com.andback.pocketfridge.data.model.RecipeEntity
import com.andback.pocketfridge.data.model.RecipeStepEntity
import com.andback.pocketfridge.data.repository.Recipe.RecipeRemoteDataSource
import com.andback.pocketfridge.domain.repository.RecipeRepository
import io.reactivex.Single
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    override fun getRecipes(): Single<BaseResponse<List<RecipeEntity>>> {
        return recipeRemoteDataSource.getRecipes()
    }

    override fun getRecipeSteps(recipeId: Int): Single<BaseResponse<List<RecipeStepEntity>>> {
        return recipeRemoteDataSource.getRecipeSteps(recipeId)
    }

    override fun getCookingIngres(recipeId: Int): Single<BaseResponse<List<CookingIngreEntity>>> {
        return recipeRemoteDataSource.getCookingIngres(recipeId)
    }
}