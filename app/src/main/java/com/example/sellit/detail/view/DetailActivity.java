package com.example.sellit.detail.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sellit.R;
import com.example.sellit.common.AppConstants;
import com.example.sellit.common.BaseActivity;
import com.example.sellit.databinding.ActivityDetailBinding;
import com.example.sellit.detail.adapter.DetailPagerAdapter;
import com.example.sellit.detail.viewmodel.DetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity<ActivityDetailBinding, DetailViewModel> {


    private final DetailPagerAdapter adapter = new DetailPagerAdapter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        setViewModel(DetailViewModel.class);

        setupViewPager();
        setListeners();
        setObservers();

        viewModel.setProductData(
                getIntent().getParcelableExtra(AppConstants.DATA_KEY)
        );
        viewModel.setPositionIfNull(getIntent().getIntExtra(AppConstants.POSITION_KEY, 0));
    }


    private void setObservers() {
        viewModel.getImageData().observe(this, productData -> {

            List<androidx.core.util.Pair<String, String>> pairs = new ArrayList<>();
            int length = productData.getImageUrls().size();
            for (int i = 0; i < length; i++) {

                pairs.add(new androidx.core.util.Pair<>(productData.getImageIds().get(i), productData.getImageUrls().get(i)));
            }
            adapter.submitList(pairs);

            binding.setProduct(productData);
        });
        viewModel.getPosition().observe(this, integer -> binding.viewPager.setCurrentItem(integer));
    }

    private void setupViewPager() {
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager2(binding.viewPager);

    }

    private void setListeners() {
        binding.toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewModel.setPosition(position);

            }


        });

    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}