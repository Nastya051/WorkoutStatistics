package com.example.workoutstatistics.ui.gallery


import co.yml.charts.common.model.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.workoutstatistics.MainActivity
import com.example.workoutstatistics.databinding.FragmentGraphicBinding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.example.workoutstatistics.Exercise
import com.example.workoutstatistics.ExercisesInWorkout
import kotlinx.coroutines.launch

class GraphicFragment : Fragment() {
    private lateinit var _binding: FragmentGraphicBinding

    private val binding get() = _binding

    lateinit var viewModel: GraphicViewModel
    var exWList: List<ExercisesInWorkout>? = null
    var exersListName: List<Exercise>? = null
    var zhimListWeigth: ArrayList<Double> = ArrayList<Double>()
    var numWork = 0
    var nameEx = ""
    var textState: MutableState<String>? = null
    var min = 0
    var max = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dao = (activity as MainActivity).db.getDao()
        val viewModelFactory = GraphicViewModelFactory(dao)
        viewModel = ViewModelProvider(
            (activity as MainActivity), viewModelFactory
        ).get(GraphicViewModel::class.java)
        return ComposeView(requireContext()).apply {
            setContent {
                var exState = remember { mutableStateOf("") }
                textState = remember { mutableStateOf("non") }
                Column {
                    exState.value = ChipGroupCompose(context)
                    nameEx = exState.value
                    myMethod()
                    myMethodName()
                    when (textState!!.value) {
                        "non" -> {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround){
                                Text(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp), text = "Минимум: Максимум:")
                            }
                        }
                        "exist" -> {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround){
                                Text(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp), text = "Минимум: $min Максимум: $max")
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        when (exState.value) {
                            "Жим лежа" -> drawChart()
                            "Тяга блока (трицепс)" -> drawChart()
                            "Тяга блока (трицепс) на одну руку" -> drawChart()
                            "Подъем гантелей на бицепс стоя" -> drawChart()
                            "Подтягивания" -> drawChart()
                            "Махи гантелей в стороны" -> drawChart()
                        }
                        textState?.value = "exist"
                    }
                }
            }
        }
    }


    fun myMethod() = viewLifecycleOwner.lifecycleScope.launch {
        val profile = viewModel.getExWorks()
        exWList = profile
//        Log.d("MyLog", "${exWList!!.size}")
    }

    fun myMethodName() = viewLifecycleOwner.lifecycleScope.launch {
        val profile = viewModel.getExByName(nameEx)//, numWork)
        exersListName = profile
//        Log.d("MyLog", "${exersListName!!.size}")
    }

    fun getPoints(): List<Point> {
        val list = ArrayList<Point>()
        for (i in 0 until zhimListWeigth.size) {
            list.add(
                Point(
                    (i).toFloat(),
                    (zhimListWeigth[i]).toFloat()
                )
            )
        }
        return list
    }

    @Composable
    fun drawChart() {
        numWork = 50
        myMethod()
        myMethodName()
        var dataList: ArrayList<String> = ArrayList()

        if(exWList.isNullOrEmpty())
            return
        if(exWList!![0].exercises.isNullOrEmpty())
            return

        for(i in 0 until exWList!!.size){
            for(j in 0 until exWList!![i].exercises.size){
                if(exWList!![i].exercises[j].name==nameEx){
                    zhimListWeigth.add(((exWList!![i].exercises[j].weigth * exWList!![i].exercises[j].repeat + exWList!![i].exercises[j].weigth * exWList!![i].exercises[j].negative / 2)))
                    dataList.add(exWList!![i].workout.date)
                }

            }
        }
        dataList.add("0")

        if(zhimListWeigth.isEmpty())
            return

        min = 0
        max = 0
        val maxWeight = getMax()//zhimListWeigth.max()
        val minWeigth = getMin()//zhimListWeigth.min()
        min = minWeigth.toInt()
        max = maxWeight.toInt()
        val steps = zhimListWeigth.size

        val pointsList = getPoints()

        val xAxisData = AxisData.Builder()
            .axisStepSize(75.dp)
            .backgroundColor(Color.White)
            .steps(pointsList.size)
            .labelData {
                dataList[it]
            }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color.White)
            .labelAndAxisLinePadding(5.dp)
            .labelData { i ->
                val yScale = (maxWeight - minWeigth) / steps
                String.format("%.1f", ((i * yScale) + minWeigth))
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsList,
                        LineStyle(color = Color.Green),
                        IntersectionPoint(),
                        SelectionHighlightPoint(color = Color.Green),
                        ShadowUnderLine(color = Color.Gray),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.White
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            lineChartData = lineChartData
        )

        zhimListWeigth.clear()
    }

    private fun getMax(): Double{
        var max = 0.0
        zhimListWeigth.forEach{
            if(max<it){
                max=it
            }
        }
        return max
    }

    private fun getMin(): Double{
        var min = 1000.0
        zhimListWeigth.forEach{
            if(min>it){
                min=it
            }
        }
        return min
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}