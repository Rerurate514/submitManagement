package com.example.submitmanagement

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TableRow
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.submitmanagement.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var thisAc : Context

    private val mRealm = RealmControl()

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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thisAc = requireActivity()

        mRealm.realmDataBaseInit()

        showRealm()

        binding.addSubmitButton.setOnClickListener { resisterSubmitButtonTap()  }
    }

    private fun resisterSubmitButtonTap(){
        (activity as? MainActivity)!!.openSubmitResisterFragment()
    }

    @SuppressLint("SetTextI18n")
    fun showRealm(){
        val realmQuantity = mRealm.getRealmMax()
        binding.submitQuantity.text = "残り : ${realmQuantity + 1}"

        if(realmQuantity != -1) addViewCombinedFun()
    }

    private fun addViewCombinedFun(){
        val params = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        binding.addLinearLayout.removeAllViews()

        for(i in 0.. mRealm.getRealmMax()){
            val submitCardView = SubmitCardView(thisAc,null,mRealm.getSubmitDB(i)).also {
                it.layoutParams = params
                it.radius = 20f
            }
            binding.addLinearLayout.addView(submitCardView)
            submitCardView.generateView()

            val space = Space(thisAc).also{
                it.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT,20)
            }
            binding.addLinearLayout.addView(space)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}