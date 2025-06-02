package com.example.fruit_picking_drone_app.ui.data

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fruit_picking_drone_app.databinding.FragmentDataBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()
    }

    private fun setupBarChart() {
        val barChart: BarChart = binding.barChart

        val entries = listOf(
            BarEntry(0f, 12f),
            BarEntry(1f, 18f),
            BarEntry(2f, 26f),
            BarEntry(3f, 14f),
            BarEntry(4f, 9f),
            BarEntry(5f, 22f),
            BarEntry(6f, 17f)
        )

        val dataSet = BarDataSet(entries, "Chestnuts Collected")
        dataSet.color = Color.parseColor("#FF9800")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barChart.data = barData

        val xAxisLabels = listOf("May 24", "May 25", "May 26", "May 27", "May 28", "May 29", "May 30")
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.labelRotationAngle = -30f

        barChart.axisRight.isEnabled = false
        barChart.axisLeft.granularity = 1f
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.invalidate() // refresh
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}