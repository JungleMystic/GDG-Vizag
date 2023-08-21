package com.lrm.gdgvizag.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lrm.gdgvizag.fragments.eventDetailFragments.DetailsFragment
import com.lrm.gdgvizag.fragments.eventDetailFragments.EventAgendaFragment
import com.lrm.gdgvizag.fragments.eventDetailFragments.EventGalleryFragment
import com.lrm.gdgvizag.fragments.eventDetailFragments.EventSpeakersFragment

class FragmentViewPagerAdapter(
    fragment: Fragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> DetailsFragment()
            1 -> EventGalleryFragment()
            2 -> EventAgendaFragment()
            3 -> EventSpeakersFragment()
            else -> DetailsFragment()
        }
    }
}