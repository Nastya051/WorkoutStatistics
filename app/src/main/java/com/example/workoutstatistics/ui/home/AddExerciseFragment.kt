package com.example.workoutstatistics.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.workoutstatistics.Exercise
import com.example.workoutstatistics.MainActivity
import com.example.workoutstatistics.Maindb
import com.example.workoutstatistics.Workout
import com.example.workoutstatistics.databinding.FragmentExerciseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class AddExerciseFragment : Fragment() {

    private lateinit var _binding: FragmentExerciseBinding

    lateinit var viewModel: AddExerciseViewModel

    private val binding get() = _binding
    private var lastIdWorkout: Workout? = null
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val application = requireNotNull(this.activity).application
        val dao = Maindb.getdb(application).getDao()
        val viewModelFactory = AddExerViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(AddExerciseViewModel::class.java)

        //обработка нажатия switch
        _binding.switchNeg.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding.editTextNegative.visibility = View.VISIBLE
            } else {
                _binding.editTextNegative.visibility = View.GONE
            }
        }
        getLastWorkout()
        return root
    }

    fun getLastWorkout() = viewLifecycleOwner.lifecycleScope.launch {
        val profile = viewModel.getLast()
//        Log.d("MyLog", "myMethod ${profile?.idWorkout}")
        lastIdWorkout = profile
    }

    fun addNewWorkout(date: String) = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.addWorkout(date)
//        Log.d("MyLog", "call myMethod")
        getLastWorkout()
//        Log.d("MyLog", "after myMethod ${lastIdWorkout?.idWorkout}")
    }

    fun addExercise() {
        _binding.apply {
            if (!editTextKg.text.isNullOrEmpty() && !editTextRepeat.text.isNullOrEmpty()) {
                var negative = 0
                if (editTextNegative.text.toString() != "") {
                    negative = editTextNegative.text.toString().toInt()
                }
                val exer = lastIdWorkout?.idWorkout?.let { it1 ->
                    Exercise(
                        null, spinner.selectedItem.toString(),
                        editTextKg.text.toString().toDouble(),
                        editTextRepeat.text.toString().toInt(),
                        negative,
                        it1
                    )
                }
                if (exer != null) {
                    viewModel.addExercise(exer)
                }
            } else {
                Toast.makeText(
                    activity as MainActivity,
                    "Все поля должны быть заполнены!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        _binding.apply {
            var date = calendarView.date
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            selectedDate = formatter.format(date)
            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                var DATE = Date(year - 1900, month, dayOfMonth)
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                selectedDate = formatter.format(DATE)
                Log.d("MyLog", "$selectedDate")
            }
            //кнопка для добавления нового упражнения
            fab.setOnClickListener {
                getLastWorkout()
                if (lastIdWorkout != null) {
                    Log.d("MyLog", "exers is exist")
                    if (selectedDate == lastIdWorkout?.date) {
                        Log.d("MyLog", "data odinakovaya ${lastIdWorkout?.date} $selectedDate")
                        addExercise()
                    } else {
                        Log.d("MyLog", "data raznaya")
                        addNewWorkout(selectedDate)
                        getLastWorkout()
                        Log.d("MyLog", "idWork ${lastIdWorkout?.idWorkout}")
                        Log.d("MyLog", "dobavlyaem ex")
                        addExercise()
                        Log.d("MyLog", "dobavili nnn ${lastIdWorkout?.idWorkout}")
                    }
                } else {
//                    Log.d("MyLog", "exers is not exist")
                    addNewWorkout(selectedDate)
                    getLastWorkout()
                    addExercise()
                }
                Toast.makeText(activity as MainActivity, "Упражнение добавлено!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}