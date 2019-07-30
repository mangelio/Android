package io.mangel.issuemanager.activities.overview

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import io.mangel.issuemanager.activities.AbstractLoadingViewModel
import io.mangel.issuemanager.factories.ApplicationFactory
import io.mangel.issuemanager.models.ConstructionSite
import kotlinx.android.synthetic.main.activity_overview.view.*

data class OverviewViewModel<T>(private val context: T, private val view: View, private val payload: Payload) :
    AbstractLoadingViewModel()
        where T : Context, T : OverviewViewModel.Overview {

    private val viewHolder = ViewHolder(view)

    init {
        val locationAdapter =
            ConstructionSiteAdapter(payload.constructionSites, context, payload.applicationFactory.fileService)
        viewHolder.constructionSiteRecyclerView.adapter = locationAdapter
    }

    interface Overview {
        fun setAbnahmeModus(value: Boolean)

        fun navigate(constructionSite: ConstructionSite)
    }

    override fun getLoadingIndicator(): ProgressBar {
        return viewHolder.loadingProgressBar
    }

    fun refreshConstructionSites() {
        viewHolder.constructionSiteRecyclerView.adapter?.notifyDataSetChanged()
    }

    class Payload(val applicationFactory: ApplicationFactory, val constructionSites: List<ConstructionSite>)

    class ViewHolder(view: View) {
        val constructionSiteRecyclerView: RecyclerView = view.construction_sites
        val loadingProgressBar: ProgressBar = view.loading
    }
}