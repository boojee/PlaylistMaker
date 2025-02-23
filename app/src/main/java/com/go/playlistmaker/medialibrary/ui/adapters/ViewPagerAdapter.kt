package com.go.playlistmaker.medialibrary.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getPageTitle(position: Int): String = titles[position]
}