package hu.bme.aut.android.classmanageractivity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.android.classmanageractivity.Fragments.HFEventsFragment
import hu.bme.aut.android.classmanageractivity.Fragments.MainEventsFragment
import hu.bme.aut.android.classmanageractivity.Fragments.ViszgaEventsFragment
import hu.bme.aut.android.classmanageractivity.Fragments.ZHEventsFragment

class EventsPageAdapter ( val fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> MainEventsFragment(fm)
        1 -> ZHEventsFragment(fm)
        2 -> HFEventsFragment(fm)
        3 -> ViszgaEventsFragment(fm)
        else -> MainEventsFragment(fm)
    }

    override fun getCount() : Int = NUM_PAGES

    companion object{
        const val NUM_PAGES = 4
    }
}