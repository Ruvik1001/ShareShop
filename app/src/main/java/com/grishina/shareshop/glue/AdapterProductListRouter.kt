package com.grishina.shareshop.glue

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import com.grishina.product_list.ProductListRouter
import com.grishina.shareshop.R

class AdapterProductListRouter(
    private val context: Context,
    private var navController: NavController?
) : ProductListRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToProductListSettings(listToken: String) {
        val bundle = Bundle().apply {
            putString(
                context.getString(com.grishina.core.R.string.LIST_TOKEN_KEY_FROM_PRODUCT_LIST),
                listToken
            )
        }
        navController?.navigate(R.id.action_productListFragment_to_productListSettingsFragment, bundle)
    }

}