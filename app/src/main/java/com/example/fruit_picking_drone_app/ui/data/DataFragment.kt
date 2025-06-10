package com.example.fruit_picking_drone_app.ui.data

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.fruit_picking_drone_app.databinding.FragmentDataBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*
import java.util.*

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DataViewModel by viewModels()
    private var refreshJob: Job? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("CHART_DEBUG", "=== DataFragment onViewCreated ===")

        // Get user ID
        userId = requireContext()
            .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            .getString("username", null)
        Log.d("CHART_DEBUG", "User ID: $userId")

        // 1) prep an empty chart
        setupLineChart(emptyList(), emptyList())

        // 2) observe your Room data
        viewModel.harvests.observe(viewLifecycleOwner) { list ->
            Log.d("CHART_DEBUG", "Observe got ${list.size} items: $list")
            if (list.isEmpty()) {
                Log.d("CHART_DEBUG", "List is empty, showing toast")
                Toast.makeText(requireContext(), "No chart data available", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("CHART_DEBUG", "Creating chart with ${list.size} entries")
                // map to entries + labels
                val entries = list.mapIndexed { i, h -> Entry(i.toFloat(), h.count.toFloat()) }
                val labels  = list.map { h ->
                    java.text.SimpleDateFormat("MM/dd", java.util.Locale.getDefault())
                        .format(java.util.Date(h.timestamp * 1000))
                }
                Log.d("CHART_DEBUG", "Entries: $entries")
                Log.d("CHART_DEBUG", "Labels: $labels")
                setupLineChart(entries, labels)
            }
        }

        // 3) Fetch data immediately
        Log.d("CHART_DEBUG", "Fetching data immediately...")
        userId?.let { 
            Log.d("CHART_DEBUG", "Calling refreshHarvests with userId: $it")
            viewModel.refreshHarvests(it) 
        }

        // 4) Start automatic refresh
        Log.d("CHART_DEBUG", "Starting auto refresh...")
        startAutoRefresh()

        // 5) Observe lifecycle to stop/start refresh
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        Log.d("CHART_DEBUG", "Fragment resumed, starting auto refresh")
                        startAutoRefresh()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        Log.d("CHART_DEBUG", "Fragment paused, stopping auto refresh")
                        stopAutoRefresh()
                    }
                    else -> {}
                }
            }
        })
    }

    private fun startAutoRefresh() {
        stopAutoRefresh() // Stop any existing refresh job
        
        refreshJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                try {
                    Log.d("CHART_DEBUG", "Auto refresh triggered")
                    userId?.let { 
                        Log.d("CHART_DEBUG", "Calling refreshHarvests with userId: $it")
                        viewModel.refreshHarvests(it)
                    }
                    delay(5000) // Wait 5 seconds
                } catch (e: Exception) {
                    Log.e("CHART_DEBUG", "Error in auto refresh", e)
                    delay(5000) // Still wait 5 seconds even if there's an error
                }
            }
        }
    }

    private fun stopAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }

    private fun setupLineChart(entries: List<Entry>, xLabels: List<String>) {
        val chart: LineChart = binding.barChart

        // give the whole chart a little extra padding on top
        chart.setExtraOffsets(0f, 24f, 0f, 24f)

        // move the legend to the bottom, outside of the plot
        chart.legend.apply {
            verticalAlignment   = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation         = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            yOffset = 8f
        }

        val dataSet = LineDataSet(entries, "Chestnuts Collected")
        dataSet.apply {
            color = android.graphics.Color.BLUE
            setCircleColor(android.graphics.Color.BLUE)
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(true)
            setDrawValues(true)
            mode = LineDataSet.Mode.LINEAR
        }
        
        val lineData = LineData(dataSet)
        chart.data = lineData

        chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(xLabels)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            labelRotationAngle = -30f
            yOffset = 8f
        }
        chart.axisRight.isEnabled = false
        chart.axisLeft.granularity = 1f
        chart.description.isEnabled = false
        chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoRefresh()
        _binding = null
    }
}