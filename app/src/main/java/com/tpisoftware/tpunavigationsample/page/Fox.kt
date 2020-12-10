package com.tpisoftware.tpunavigationsample.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.tpisoftware.tpunavigationsample.R

class Fox : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fox, container, false)
        v.findViewById<Button>(R.id.next_btn).setOnClickListener {
            findNavController().navigate(R.id.action_fox_to_fox2)
        }
        return v
    }
}