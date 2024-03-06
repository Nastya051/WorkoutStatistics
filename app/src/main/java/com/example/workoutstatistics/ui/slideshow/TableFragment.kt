package com.example.workoutstatistics.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.workoutstatistics.MainActivity
import com.example.workoutstatistics.databinding.FragmentTableBinding
import kotlinx.coroutines.launch
import com.example.workoutstatistics.ExercisesInWorkout

class TableFragment : Fragment() {

    private lateinit var _binding: FragmentTableBinding

    lateinit var viewModel: TableViewModel

    private val binding get() = _binding
    var exWList: List<ExercisesInWorkout>? = null
    var cntW = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dao = (activity as MainActivity).db.getDao()
        val viewModelFactory = TableViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(TableViewModel::class.java)
        return ComposeView(requireContext()).apply {
            setContent {
                getCountOfWorkouts()
                getFullListExercises()
                TableScreen()
            }
        }
    }

    fun getFullListExercises() = viewLifecycleOwner.lifecycleScope.launch {
        val profile = viewModel.getExWorks()
//        Log.d("MyLog", "myExWorks ${profile[0].exercises[0].name}")
        exWList = profile
    }

    fun getCountOfWorkouts() = viewLifecycleOwner.lifecycleScope.launch {
        val profile = viewModel.getCntWorks()
//        Log.d("MyLog", "myMethodCnt ${profile}")
        cntW = profile
    }

    @Composable
    fun RowScope.TableCell(
        text: String,
        weight: Float,
    ) {
        Text(
            text = text,
            Modifier
                .weight(weight)
                .padding(5.dp)
                .fillMaxHeight()
        )
    }

    fun getTonnaList(): List<Double>{
        var tonnaList = ArrayList<Double>()
        var tonna = 0.0
        for (q in 0 until cntW) {
            for (i in 0 until exWList!![q].exercises.size){
                val summa: Double =
                    (exWList!![q].exercises[i].weigth * exWList!![q].exercises[i].repeat) + (exWList!![q].exercises[i].weigth * exWList!![q].exercises[i].negative / 2)
                tonna += summa
            }
            tonnaList.add(tonna)
            tonna = 0.0
        }
        return  tonnaList
    }

    @Composable
    fun TableScreen() {
        getCountOfWorkouts()
        getFullListExercises()
        val myColor: Color = Color(0xFF7FDF92)
        val myColor2: Color = Color(0xFF84E0C4)
        val tonnazh = getTonnaList()
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            for (q in 0 until cntW) {
                item {
                    Row(Modifier.background(myColor)) {
                        TableCell(text = "${exWList!![q].workout.date}", weight = 1.0f)
                    }
                }
                item {
                    Row(Modifier.background(myColor2)) {
                        TableCell(text = "Упражнение", weight = .3f)
                        TableCell(text = "Кг", weight = .15f)
                        TableCell(text = "Повторы", weight = .2f)
                        TableCell(text = "Негатив.", weight = .2f)
                        TableCell(text = "Сумма", weight = .15f)
                    }
                }
                for (i in 0 until exWList!![q].exercises.size) {
                    item {
                        Row(Modifier.background(Color.White)) {
                            TableCell(text = "${exWList!![q].exercises[i].name}", weight = .3f)
                            TableCell(text = "${exWList!![q].exercises[i].weigth}", weight = .15f)
                            TableCell(text = "${exWList!![q].exercises[i].repeat}", weight = .2f)
                            TableCell(text = "${exWList!![q].exercises[i].negative}", weight = .2f)
                            val summa: Double =
                                (exWList!![q].exercises[i].weigth * exWList!![q].exercises[i].repeat) + (exWList!![q].exercises[i].weigth * exWList!![q].exercises[i].negative / 2)
                            TableCell(text = "$summa", weight = .15f)
                        }
                    }
                }
                item {
                    Row(Modifier.background(myColor2)) {
                        TableCell(text = "Тоннаж", weight = .5f)
                        TableCell(text = "${tonnazh[q]}", weight = .5f)
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}