package com.blueray.respect_new.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect.adapters.MyTeamAdapter
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.FragmentMyTeamBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.GetMyTeamQutationsData
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MyTeamFragment : BaseFragment<FragmentMyTeamBinding, AppViewModel>() {

    override val viewModel by viewModels<AppViewModel>()
    private lateinit var barChart: BarChart
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyTeamBinding {
        return FragmentMyTeamBinding.inflate(inflater, container, false)
    }

    private lateinit var myTeamAdapter: MyTeamAdapter
    private var drawerController: DrawerController? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DrawerController) {
            drawerController = context
        } else {
            throw RuntimeException("$context must implement DrawerController")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }
        barChart =  binding.barChart
        Glide.with(requireActivity())
            .load(HelperUtils.getUserImage(requireActivity()))
            .placeholder(R.drawable.ic_eye)
            .centerInside()
            .into(binding.includeTab.barUserImage)
        viewModel.retrieveMyTeam(HelperUtils.getUID(requireActivity()))
        viewModel.retrieveMyTeamQuotations(HelperUtils.getUID(requireActivity()))
        getMyTeam()
        getMyTeamQuotations()


    }

    private fun getMyTeam() {
        viewModel.getMyTeam().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    myTeamAdapter = MyTeamAdapter(result.data.msg.data)
                    binding.recycler.setHasFixedSize(true)
                    binding.recycler.adapter = myTeamAdapter
                }

                is NetworkResults.Error -> {

                }

                else -> {

                }
            }
        }
    }

    private fun getMyTeamQuotations() {
        viewModel.getMyTeamQuotations().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    setupBarChart(result.data.msg.data)
                }

                is NetworkResults.Error -> {

                }

                else -> {

                }
            }
        }
    }

    private fun setupBarChart(dataList: List<GetMyTeamQutationsData>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        dataList.forEachIndexed { index, data ->
            entries.add(BarEntry(index.toFloat(), data.quotations_count.toFloat()))
            labels.add(data.full_name)
        }

        val dataSet = BarDataSet(entries, "Quotations")
        dataSet.colors = listOf(Color.MAGENTA)
        val barData = BarData(dataSet)
        barData.barWidth = 0.5f // Adjust this value as needed

        barChart.data = barData

        // Customize the chart
        barChart.description.text = ""
        barChart.setFitBars(true)
        barChart.animateY(1000)

        // Customize X Axis
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.axisMinimum = -0.5f // Adjust to show the first bar fully
        xAxis.axisMaximum = entries.size - 0.5f // Adjust to show the last bar fully
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        // Customize Left Y Axis
        val leftYAxis = barChart.axisLeft
        leftYAxis.setDrawGridLines(false)
        leftYAxis.setDrawLabels(true)

        // Customize Right Y Axis
        val rightYAxis = barChart.axisRight
        rightYAxis.setDrawGridLines(false)
        rightYAxis.setDrawLabels(false)

        barChart.invalidate() // refresh
    }
}