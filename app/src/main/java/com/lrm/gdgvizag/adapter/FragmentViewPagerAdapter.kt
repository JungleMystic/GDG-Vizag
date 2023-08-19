package com.lrm.gdgvizag.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lrm.gdgvizag.fragments.eventDetailFragments.DetailsFragment
import com.lrm.gdgvizag.fragments.eventDetailFragments.EventGalleryFragment

class FragmentViewPagerAdapter(
    fragment: Fragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            DetailsFragment()
        } else {
            EventGalleryFragment()
        }
    }
}