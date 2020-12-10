package com.tpisoftware.tpunavigationsample.extension

import android.content.Intent
import android.util.Log
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupWithNavController(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent
): LiveData<NavController> {

    val TAG = BottomNavigationView::class.java.simpleName

    // 紀錄 graph id 跟對應的 tag
    val graphIdToTagMap = SparseArray<String>()
    // 被選中的 nav controller
    val selectedNavController = MutableLiveData<NavController>()

    var firstFragmentGraphId = 0

    // 為每個 navigation graph 產生 NavHostFragment
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)

        // 建立或取得 NavHostFragment
        val navHostFragment = obtainNavHostFragment(
            fragmentManager,
            fragmentTag,
            navGraphId,
            containerId
        )

        // 取得 graph id
        val graphId = navHostFragment.navController.graph.id

        // 標記一下第一頁是誰
        if (index == 0) {
            firstFragmentGraphId = graphId
        }

        // 把 id 跟 tag 存到 map 中
        graphIdToTagMap[graphId] = fragmentTag

        // 根據是否選到該頁面決定 attach 或 detach NavHostFragment
        if (this.selectedItemId == graphId) {
            // 更新 livedata 儲存的 navController
            selectedNavController.value = navHostFragment.navController
            attachNavHostFragment(fragmentManager, navHostFragment, index == 0)
        } else {
            detachNavHostFragment(fragmentManager, navHostFragment)
        }
    }

    // 拿選到的 tag
    var selectedItemTag = graphIdToTagMap[this.selectedItemId]
    // 對照第一頁的 tag
    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
    // 目前是否是第一頁
    var isOnFirstFragment = selectedItemTag == firstFragmentTag

    // 當底下選單被選到的時候
    setOnNavigationItemSelectedListener { item ->
        // 狀態已經被儲存的話就不做事情
        if (fragmentManager.isStateSaved) {
            Log.i(TAG, "state saved")
            false
        } else {
            // 新的目標 tag
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            // 如果目前位置的 tag 跟新的目標 tag 不同
            if (selectedItemTag != newlySelectedItemTag) {
                Log.i(TAG, "selected item is new one")
                // pop 包含首頁的的 fragment
                fragmentManager.popBackStack(
                    firstFragmentTag,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                        as NavHostFragment

                if (firstFragmentTag != newlySelectedItemTag) {
                    // 進行 fragment transaction 把 back stack 清空
                    // 設定目前選到的頁面並且把 back stack 固定設定為第一頁
                    //Log.i(TAG, "attach: $newlySelectedItemTag")
                    val transaction = fragmentManager.beginTransaction()

                    transaction.attach(selectedFragment)
                        .setPrimaryNavigationFragment(selectedFragment)

                    graphIdToTagMap.forEach { index, fragmentTagIterator ->
                        if (fragmentTagIterator != newlySelectedItemTag) {
                            //Log.i(TAG, "detach: $fragmentTagIterator")
                            transaction.detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                        }
                    }
                    //Log.i(TAG, "attach: $firstFragmentTag")
                    transaction
                        .addToBackStack(firstFragmentTag)
                        .setReorderingAllowed(true)
                        .commit()

                }
                // 紀錄目前位置
                selectedItemTag = newlySelectedItemTag
                isOnFirstFragment = selectedItemTag == firstFragmentTag
                selectedNavController.value = selectedFragment.navController
                true
            } else {
                false
            }
        }
    }

    // 最後確認一下 bottom navigation 顯示的是目前的操作的位置
    fragmentManager.addOnBackStackChangedListener {
        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
            this.selectedItemId = firstFragmentGraphId
        }
    }
    return selectedNavController
}

private fun obtainNavHostFragment(
    fragmentManager: FragmentManager,
    fragmentTag: String,
    navGraphId: Int,
    containerId: Int
): NavHostFragment {
    // 如果有 NavHostFragment, 就回傳
    val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
    existingFragment?.let { return it }

    // 不然的話建立一個
    val navHostFragment = NavHostFragment.create(navGraphId)
    fragmentManager.beginTransaction()
        .add(containerId, navHostFragment, fragmentTag)
        .commitNow()
    return navHostFragment
}

private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
    val backStackCount = backStackEntryCount
    for (index in 0 until backStackCount) {
        if (getBackStackEntryAt(index).name == backStackName) {
            return true
        }
    }
    return false
}

private fun detachNavHostFragment(
    fragmentManager: FragmentManager,
    navHostFragment: NavHostFragment
) {
    fragmentManager.beginTransaction()
        .detach(navHostFragment)
        .commitNow()
}

private fun attachNavHostFragment(
    fragmentManager: FragmentManager,
    navHostFragment: NavHostFragment,
    isPrimaryNavFragment: Boolean
) {
    fragmentManager.beginTransaction()
        .attach(navHostFragment)
        .apply {
            if (isPrimaryNavFragment) {
                setPrimaryNavigationFragment(navHostFragment)
            }
        }
        .commitNow()

}

private fun getFragmentTag(index: Int) = "bottomNavigation#$index"