package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lrm.gdgvizag.adapter.FragmentViewPagerAdapter
import com.lrm.gdgvizag.constants.TAG
import com.lrm.gdgvizag.databinding.FragmentEventDetailBinding
import com.lrm.gdgvizag.viewmodel.AppViewModel

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()
    private val navArgs: EventDetailFragmentArgs by navArgs()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentAdapter: FragmentViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = navArgs.eventId
        Log.i(TAG, "EventDetail onViewCreated: eventId -> $eventId")
        appViewModel.getEventDetailedData(eventId, requireActivity())

        appViewModel.eventDetail.observe(viewLifecycleOwner) {event ->
            if (event != null){
                binding.fragmentLabel.text = event.eventName
            }
        }

        tabLayout = binding.eventTabLayout
        viewPager2 = binding.eventViewPager
        fragmentAdapter = FragmentViewPagerAdapter(this)
        viewPager2.adapter = fragmentAdapter

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}