package com.go.playlistmaker.medialibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.playlistmaker.R
import com.go.playlistmaker.medialibrary.ui.adapters.ViewPagerAdapter
import com.go.playlistmaker.databinding.FragmentLibraryBinding
import com.go.playlistmaker.favorites.ui.fragments.FavoritesFragment
import com.go.playlistmaker.playlists.ui.fragments.PlaylistsFragment
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(FavoritesFragment.newInstance(), getString(R.string.favorites))
        adapter.addFragment(PlaylistsFragment.newInstance(), getString(R.string.playlists))

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }
}