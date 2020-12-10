package com.tpisoftware.tpunavigationsample.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tpisoftware.tpunavigationsample.R

class Maple2 : Fragment() {

    var magicNumber = 0
    lateinit var magicNumberText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get value
        magicNumber = requireArguments().getInt("magicNumber")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_maple2, container, false)
        magicNumberText = v.findViewById(R.id.magic_number_text)
        magicNumberText.text = "The magic number is ${magicNumber.toString()}"
        return v
    }

}