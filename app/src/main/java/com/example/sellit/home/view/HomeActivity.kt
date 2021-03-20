package com.example.sellit.home.view

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellit.R
import com.example.sellit.common.AppConstants
import com.example.sellit.common.BaseActivity
import com.example.sellit.common.SpaceItemDecoration
import com.example.sellit.databinding.ActivityHomeBinding
import com.example.sellit.databinding.ItemClassifiedBinding
import com.example.sellit.detail.view.DetailActivity
import com.example.sellit.home.adapters.HomeRecyclerAdapter
import com.example.sellit.home.api.response.ClassifiedItem
import com.example.sellit.home.viewmodel.HomeViewModel
import com.example.sellit.utilities.Utilities


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(),
    HomeRecyclerAdapter.OnItemClickListener {

    private val adapter = HomeRecyclerAdapter()
    private var data: List<ClassifiedItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setViewModel(HomeViewModel::class.java)

        setRecyclerView()
        setObservers()
        setListeners()

        requestData()


    }

    private fun requestData() {
        viewModel.getClassified()
    }

    private fun setRecyclerView(): HomeRecyclerAdapter {

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dimen_main)))
        binding.recyclerView.itemAnimator = null
        return adapter
    }

    private fun setListeners() {


//        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (!binding.recyclerView.canScrollVertically(RecyclerView.VERTICAL) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                    viewModel.getClassifiedNextPage()
//
//                }
//            }
//        })


        adapter.listener = this
    }

    private fun setObservers() {
        viewModel.classifiedItemResult.observe(this, {
            adapter.submitList(it)
            this.data = it
        })


        viewModel.progressBarVisible.observe(this, {
            if (it) showProgressbar() else hideProgressbar()
        })

        viewModel.error.observe(this, {
            Utilities.showToast(this, it)
        })
    }

    override fun onPause() {
        hideSoftKeyboard()
        super.onPause()
    }


    override fun onItemClick(position: Int, binding: ItemClassifiedBinding) {

        val intent = Intent(this, DetailActivity::class.java)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(binding.imageView, getString(R.string.shared_transition_image)),
            Pair(binding.name, getString(R.string.shared_transition_name)),
            Pair(binding.price, getString(R.string.shared_transition_price))
        )


        intent.putExtra(AppConstants.DATA_KEY, data[position])
        startActivity(intent, options.toBundle())
    }


//    fun expand(v: View, duration: Int, targetHeight: Int) {
//        val prevHeight = v.height
//        v.visibility = View.VISIBLE
//        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
//        valueAnimator.addUpdateListener { animation ->
//            v.layoutParams.height = animation.animatedValue as Int
//            v.requestLayout()
//        }
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.duration = duration.toLong()
//        valueAnimator.start()
//
//    }
//
//    fun collapse(v: View, duration: Int, targetHeight: Int) {
//        val prevHeight = v.height
//        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.addUpdateListener { animation ->
//            v.layoutParams.height = animation.animatedValue as Int
//            v.requestLayout()
//        }
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.duration = duration.toLong()
//        valueAnimator.start()
//
//    }
}