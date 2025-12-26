package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentTemplatesBinding
import java.io.File


class TemplatesFragment : Fragment() {
    private lateinit var binding: FragmentTemplatesBinding
    private val filePickRequestCode = 1
    private var currentFileName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.templateFileAndToolsMain.setOnClickListener {
            openFilePicker("myFileMainAndTools.html")
        }

        binding.templateFileEquipmentsAndSpareParts.setOnClickListener {
            openFilePicker("myFileEquipmentAndSpareParts.html")
        }

        binding.templateFileCheckForms.setOnClickListener {
            openFilePicker("myFileCheckForms.html")
        }
        checkFilesAndSetIcons()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == filePickRequestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null && currentFileName != null) {
                saveFileToInternalStorage(uri, currentFileName!!)
                updateIcons(currentFileName!!)
            }
        }
    }

    private fun updateIcons(fileName: String) {
        val context = requireContext()
        val fileExists = checkFileExists(context, fileName)
        val drawableId = if (fileExists) R.drawable.file_successful else R.drawable.file_missing

        when (fileName) {
            "myFileMainAndTools.html" -> {
                binding.templateFileAndToolsMain.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0)
                if (fileExists) binding.templateFileAndToolsMain.setText(fileName)
            }
            "myFileEquipmentAndSpareParts.html" -> {
                binding.templateFileEquipmentsAndSpareParts.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0)
                if (fileExists) binding.templateFileEquipmentsAndSpareParts.setText(fileName)
            }
            "myFileCheckForms.html" -> {
                binding.templateFileCheckForms.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0)
                if (fileExists) binding.templateFileCheckForms.setText(fileName)
            }

        }
    }

    private fun openFilePicker(fileName: String) {
        currentFileName = fileName
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, filePickRequestCode)
    }

    private fun checkFileExists(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }
    private fun saveFileToInternalStorage(uri: Uri, fileName: String) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream?.close()
    }
    private fun checkFilesAndSetIcons() {
        val fileNames = listOf("myFileMainAndTools.html", "myFileEquipmentAndSpareParts.html", "myFileCheckForms.html")
        for (fileName in fileNames) {
            updateIcons(fileName)
        }
    }


}