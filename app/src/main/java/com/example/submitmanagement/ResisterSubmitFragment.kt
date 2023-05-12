package com.example.submitmanagement

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.submitmanagement.databinding.FragmentResisterSubmitBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResisterSubmitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResisterSubmitFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentResisterSubmitBinding? = null
    private val binding get() = _binding!!

    private lateinit var thisAc : Context

    private var yearData : String = ""
    private var monthData : String = ""
    private var dayData : String = ""
    private var hourData : String = ""
    private var minuteData : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResisterSubmitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisAc = requireActivity()

        binding.submitTermDayCalenderShowButton.setOnClickListener { submitTermDayShowCalenderButtonTap() }
        binding.submitTermHourShowButton.setOnClickListener { submitTermHourShowButtonTap() }
        binding.submitResisterButton.setOnClickListener { submitResisterCompleteButtonTap() }
        binding.returnButton.setOnClickListener { returnButtonTap() }
    }

    private fun submitTermDayShowCalenderButtonTap() =
        calenderShow()

    private fun submitTermHourShowButtonTap() =
        clockShow()

    private fun returnButtonTap() =
        deleteResisterSubmitFragment()

    private fun submitResisterCompleteButtonTap() {
        submitResisterComplete()
        fragmentFinish()
    }

    private fun deleteResisterSubmitFragment() =
        fragmentFinish()


    @SuppressLint("SetTextI18n")
    private fun calenderShow(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            thisAc,
            { _, collectYear, collectMonth, collectDay -> //when we got month's value is 0
                yearData = zeroPadInt(collectYear)
                monthData = zeroPadInt(collectMonth + 1)
                dayData = zeroPadInt(collectDay)
                binding.submitTermDayShowText.text = "${collectYear}年${collectMonth + 1}月${collectDay}日"
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun clockShow(){
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            thisAc,
            {   _, collectHour, collectMinute ->
                hourData = zeroPadInt(collectHour)
                minuteData = zeroPadInt(collectMinute)
                binding.submitTermHourShowText.text = "${collectHour}時${collectMinute}分"
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun submitResisterComplete(){
        val ymdHm : String = //YYYYMMDDHHMM
                    yearData +
                    monthData +
                    dayData +
                    hourData +
                    minuteData

        val submitData = Submit(
            binding.subjectNameEditText.text.toString(),
            binding.submitContentsNameEditText.text.toString(),
            ymdHm,
            binding.remarksEditText.text.toString()
        )

        RealmControl(submitData).controlSubmitDB()
    }

    /**
     * 数を入れて、その数が一桁な場合は先頭に0をつけます。　　　　　　　例:1 -> 01, 5 -> 05, 15 -> 15
     * @param num 任意のInt型
     * @return [num] + 0のString型
     */
    private fun zeroPadInt(num: Int): String{
        if(num < 10) return "0$num"
        return num.toString()
    }

    private fun fragmentFinish(){
        (thisAc as MainActivity).showRealmRecordByCard()
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResisterSubmitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResisterSubmitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
