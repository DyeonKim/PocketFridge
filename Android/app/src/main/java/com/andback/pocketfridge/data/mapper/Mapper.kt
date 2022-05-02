package com.andback.pocketfridge.data.mapper

import android.util.Log
import com.andback.pocketfridge.data.model.IngreEntity
import com.andback.pocketfridge.domain.model.Ingredient
import com.andback.pocketfridge.present.utils.DateConverter
import com.andback.pocketfridge.present.utils.Storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

object IngreMapper {
    private const val TAG = "Mapper_debuk"

    operator fun invoke(entity: IngreEntity): Ingredient {
        return Ingredient(
            category = entity.subCategoryId,
            purchasedDate = entity.foodIngredientDate,
            expiryDate = entity.foodIngredientExp,
            name = entity.foodIngredientName,
            fridgeId = entity.refrigeratorId,
            storage = Storage.valueOf(entity.foodIngredientWay),
            leftDay = getLeftDay(entity.foodIngredientExp, SimpleDateFormat("yyyy-MM-dd").format(Date()))
        )
    }

    operator fun invoke(ingredient: Ingredient): IngreEntity {
        return IngreEntity(
            subCategoryId = ingredient.category,
            foodIngredientDate = ingredient.purchasedDate,
            foodIngredientExp = ingredient.expiryDate,
            foodIngredientName = ingredient.name,
            foodIngredientWay = ingredient.storage.value,
            refrigeratorId = ingredient.fridgeId
        )
    }

    private fun getLeftDay(stringDate: String, nowStringDate: String): Int {
        val inputDate = DateConverter.toDate(stringDate).time
        val nowDate = DateConverter.toDate(nowStringDate).time
        val result = ceil((inputDate - nowDate) / (24 * 60 * 60 * 1000).toDouble()).toInt()
        Log.d(TAG, "getLeftDay: ${result}")
        return result
    }
}