package com.tpisoftware.tpunavigationsample.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.tpisoftware.tpunavigationsample.R

class Maple : Fragment() {

    lateinit var magicNumberEdit: EditText
    lateinit var enterBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_maple, container, false)
        magicNumberEdit = v.findViewById(R.id.magic_number_edit)
        enterBtn = v.findViewById(R.id.edit_enter_btn)
        enterBtn.setOnClickListener {
            var magicNumber = 0
            val text = magicNumberEdit.text.toString()
            if (text.isNotEmpty()) {
                magicNumber = Integer.parseInt(text)
            }
            val bundle = bundleOf("magicNumber" to magicNumber)
            findNavController().navigate(R.id.action_maple_to_maple2, bundle)
        }
        return v
    }
    
}